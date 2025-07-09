package com.addzero.kmp.processor

import com.addzero.kmp.context.SettingContext
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate

/**
 * Compose参数打包处理器
 * 类似Vue的$attrs功能，为Compose函数生成参数打包
 */
class ComposeAttrsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
//        SettingContext.initialize(options)

        // 1. 查找所有带有@ComposeAttrs注解的函数
        val attrsAnnotatedFunctions = resolver
            .getSymbolsWithAnnotation("com.addzero.kmp.annotation.ComposeAttrs")
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { it.validate() }
            .toList()

        // 2. 查找原生Compose组件并自动生成$attrs
        val nativeComposeFunctions = findNativeComposeFunctions(resolver)

        val allFunctions = attrsAnnotatedFunctions + nativeComposeFunctions

        if (allFunctions.isEmpty()) {
            logger.warn("没有找到需要生成attrs的函数")
            return emptyList()
        }

        logger.warn("找到${attrsAnnotatedFunctions.size}个注解标记的函数和${nativeComposeFunctions.size}个原生Compose函数，总共${allFunctions.size}个需要生成attrs")

        // 为每个函数生成$attrs相关代码
        allFunctions.forEach { function ->
            generateAttrsForFunction(function, isNativeFunction = function in nativeComposeFunctions)
        }

        return allFunctions.filterNot { it.validate() }
    }

    /**
     * 查找原生Compose组件函数
     */
    private fun findNativeComposeFunctions(resolver: Resolver): List<KSFunctionDeclaration> {
        val targetComponents = listOf(
            "Text", "Button", "Card", "TextField", "OutlinedTextField",
            "IconButton", "FloatingActionButton", "Switch", "Checkbox",
            "RadioButton", "Slider", "LinearProgressIndicator",
            "CircularProgressIndicator", "Box", "Column", "Row",
            "LazyColumn", "LazyRow"
        )

        val foundFunctions = mutableListOf<KSFunctionDeclaration>()

        // 查找所有@Composable函数
        resolver.getSymbolsWithAnnotation("androidx.compose.runtime.Composable")
            .filterIsInstance<KSFunctionDeclaration>()
            .forEach { function ->
                val functionName = function.simpleName.asString()
                val packageName = function.packageName.asString()

                // 检查是否是我们要处理的原生组件
                if (functionName in targetComponents &&
                    (packageName.startsWith("androidx.compose.material3") ||
                     packageName.startsWith("androidx.compose.foundation"))) {
                    foundFunctions.add(function)
                    logger.warn("找到原生Compose组件: $packageName.$functionName")
                }
            }

        return foundFunctions
    }

    /**
     * 为单个函数生成$attrs相关代码
     */
    private fun generateAttrsForFunction(function: KSFunctionDeclaration, isNativeFunction: Boolean = false) {
        val functionName = function.simpleName.asString()
        val originalPackageName = function.packageName.asString()

        // 对于原生函数，使用固定的生成包名
        val targetPackageName = if (isNativeFunction) {
            "com.addzero.kmp.generated.attrs"
        } else {
            originalPackageName
        }

        logger.warn("正在为函数 $originalPackageName.$functionName 生成attrs代码到包 $targetPackageName")

        // 分析函数参数
        val parameters = analyzeParameters(function, isNativeFunction)

        // 生成代码
        val generatedCode = generateAttrsCode(functionName, targetPackageName, parameters, isNativeFunction, originalPackageName)

        // 写入文件
        writeGeneratedCode(functionName, targetPackageName, generatedCode, function, isNativeFunction)
    }

    /**
     * 分析函数参数
     */
    private fun analyzeParameters(function: KSFunctionDeclaration, isNativeFunction: Boolean = false): List<ParameterInfo> {
        return function.parameters.mapNotNull { param ->
            val paramName = param.name?.asString() ?: ""

            // 检查是否被@AttrsExclude排除
            val isExcluded = param.annotations.any {
                it.shortName.asString() == "AttrsExclude"
            }

            // 对于原生函数，自动排除一些常见的参数
            val isAutoExcluded = if (isNativeFunction) {
                paramName in listOf("modifier", "content", "onClick", "onValueChange",
                                   "onCheckedChange", "onSelectionChange", "interactionSource")
            } else {
                false
            }

            if (isExcluded || isAutoExcluded) {
                if (isAutoExcluded) {
                    logger.warn("自动排除原生函数参数: $paramName")
                }
                null
            } else {
                ParameterInfo(
                    name = paramName,
                    type = param.type.resolve(),
                    hasDefaultValue = param.hasDefault,
                    isNullable = param.type.resolve().isMarkedNullable
                )
            }
        }
    }

    /**
     * 生成$attrs相关代码
     */
    private fun generateAttrsCode(
        functionName: String,
        packageName: String,
        parameters: List<ParameterInfo>,
        isNativeFunction: Boolean = false,
        originalPackageName: String = ""
    ): String {
        val attrsClassName = "${functionName}Attrs"

        // 生成数据类
        val dataClassCode = generateAttrsDataClass(attrsClassName, parameters)

        // 生成扩展属性
        val extensionPropertyCode = generateAttrsExtensionProperty(functionName, attrsClassName)

        // 生成辅助函数
        val helperFunctionsCode = generateHelperFunctions(functionName, attrsClassName, parameters)

        // 为原生函数生成额外的导入和使用示例
        val imports = if (isNativeFunction) {
            generateNativeImports(originalPackageName, functionName)
        } else {
            "import androidx.compose.runtime.Composable"
        }

        val usageExample = if (isNativeFunction) {
            generateNativeUsageExample(functionName, attrsClassName, originalPackageName)
        } else {
            ""
        }

        return """
package $packageName

$imports

/**
 * $functionName 函数的参数打包类
 * 类似Vue的attrs功能
 * ${if (isNativeFunction) "原生Compose组件: $originalPackageName.$functionName" else ""}
 * 由KSP自动生成
 */
$dataClassCode

$extensionPropertyCode

$helperFunctionsCode

$usageExample
        """.trimIndent()
    }

    /**
     * 生成Attrs数据类
     */
    private fun generateAttrsDataClass(className: String, parameters: List<ParameterInfo>): String {
        val properties = parameters.joinToString(",\n    ") { param ->
            val defaultValue = if (param.hasDefaultValue) {
                when {
                    param.isNullable -> " = null"
                    param.type.declaration.simpleName.asString() == "String" -> " = \"\""
                    param.type.declaration.simpleName.asString() == "Boolean" -> " = false"
                    param.type.declaration.simpleName.asString() == "Int" -> " = 0"
                    param.type.declaration.simpleName.asString() == "Float" -> " = 0f"
                    param.type.declaration.simpleName.asString() == "Double" -> " = 0.0"
                    else -> ""
                }
            } else ""

            "val ${param.name}: ${param.type.getQualifiedTypeString()}$defaultValue"
        }

        return """
data class $className(
    $properties
)
        """.trimIndent()
    }

    /**
     * 生成$attrs扩展属性
     */
    private fun generateAttrsExtensionProperty(functionName: String, attrsClassName: String): String {
        return """
/**
 * $functionName 函数的${attrsClassName}扩展属性
 * 类似Vue的${attrsClassName}，包含所有传递给组件的属性
 */
val $attrsClassName.${'$'}attrs: $attrsClassName
    get() = this
        """.trimIndent()
    }

    /**
     * 生成辅助函数
     */
    private fun generateHelperFunctions(
        functionName: String,
        attrsClassName: String,
        parameters: List<ParameterInfo>
    ): String {
        // 生成构建器函数
        val builderFunction = generateBuilderFunction(functionName, attrsClassName, parameters)

        // 生成应用函数
        val applyFunction = generateApplyFunction(functionName, attrsClassName, parameters)

        return """
$builderFunction

$applyFunction
        """.trimIndent()
    }

    /**
     * 生成构建器函数
     */
    private fun generateBuilderFunction(
        functionName: String,
        attrsClassName: String,
        parameters: List<ParameterInfo>
    ): String {
        val paramList = parameters.joinToString(",\n    ") { param ->
            val defaultValue = if (param.hasDefaultValue) {
                when {
                    param.isNullable -> " = null"
                    param.type.declaration.simpleName.asString() == "String" -> " = \"\""
                    param.type.declaration.simpleName.asString() == "Boolean" -> " = false"
                    param.type.declaration.simpleName.asString() == "Int" -> " = 0"
                    param.type.declaration.simpleName.asString() == "Float" -> " = 0f"
                    param.type.declaration.simpleName.asString() == "Double" -> " = 0.0"
                    else -> ""
                }
            } else ""

            "${param.name}: ${param.type.getQualifiedTypeString()}$defaultValue"
        }

        val constructorArgs = parameters.joinToString(",\n        ") { param ->
            "${param.name} = ${param.name}"
        }

        return """
/**
 * 构建 $attrsClassName 的便捷函数
 */
fun ${functionName.lowercase()}Attrs(
    $paramList
): $attrsClassName {
    return $attrsClassName(
        $constructorArgs
    )
}
        """.trimIndent()
    }

    /**
     * 生成应用函数
     */
    private fun generateApplyFunction(
        functionName: String,
        attrsClassName: String,
        parameters: List<ParameterInfo>
    ): String {
        return """
/**
 * 将${attrsClassName}应用到${functionName}函数的便捷扩展
 */
@Composable
fun $attrsClassName.applyTo${functionName}(
    content: @Composable () -> Unit = {}
) {
    // 这里可以根据实际需要实现应用逻辑
    // 例如调用原始的${functionName}函数并传递所有参数
}
        """.trimIndent()
    }

    /**
     * 生成原生函数的导入语句
     */
    private fun generateNativeImports(originalPackageName: String, functionName: String): String {
        return """
import androidx.compose.runtime.Composable
import $originalPackageName.$functionName
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
        """.trimIndent()
    }

    /**
     * 生成原生函数的使用示例
     */
    private fun generateNativeUsageExample(functionName: String, attrsClassName: String, originalPackageName: String): String {
        val lowerFunctionName = functionName.lowercase()
        return """
/**
 * 使用示例：
 *
 * ```kotlin
 * // 创建${functionName}的属性包
 * val ${lowerFunctionName}Attrs = ${lowerFunctionName}Attrs(
 *     // 设置属性...
 * )
 *
 * // 使用属性包调用原生${functionName}组件
 * ${functionName}(
 *     // 展开属性包的所有属性
 *     // 注意：需要手动实现属性展开逻辑
 * )
 * ```
 */
        """.trimIndent()
    }

    /**
     * 写入生成的代码到文件
     */
    private fun writeGeneratedCode(
        functionName: String,
        packageName: String,
        generatedCode: String,
        function: KSFunctionDeclaration,
        isNativeFunction: Boolean = false
    ) {
        val fileName = "${functionName}Attrs"
        val packagePath = packageName.replace(".", "/")

        try {
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(false, function.containingFile!!),
                packageName = packageName,
                fileName = fileName
            )

            file.write(generatedCode.toByteArray())
            file.close()

            val fileType = if (isNativeFunction) "原生组件" else "自定义组件"
            logger.warn("成功生成${fileType}文件: $packagePath/$fileName.kt")
        } catch (e: Exception) {
            logger.error("生成文件失败: ${e.message}")
        }
    }
}

/**
 * 参数信息数据类
 */
data class ParameterInfo(
    val name: String,
    val type: KSType,
    val hasDefaultValue: Boolean,
    val isNullable: Boolean
)

/**
 * KSType扩展函数，获取完整类型字符串
 */
fun KSType.getQualifiedTypeString(): String {
    val baseType = this.declaration.qualifiedName?.asString()
        ?: this.declaration.simpleName.asString()

    val genericArgs = if (this.arguments.isNotEmpty()) {
        this.arguments.joinToString(", ") { arg ->
            arg.type?.resolve()?.let {
                it.declaration.qualifiedName?.asString() ?: it.declaration.simpleName.asString()
            } ?: "*"
        }
    } else null

    val nullableSuffix = if (this.nullability == Nullability.NULLABLE) "?" else ""

    return when {
        genericArgs != null -> "$baseType<$genericArgs>$nullableSuffix"
        else -> "$baseType$nullableSuffix"
    }
}

/**
 * 处理器提供者
 */
class ComposeAttrsProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ComposeAttrsProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}
