package com.addzero.kmp.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate

private const val propsXd = "var"

/**
 * Compose参数打包处理器
 * 类似Vue的$attrs功能，为原生Compose函数生成参数打包
 */
class ComposeAttrsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 从KSP选项中获取后缀配置，默认为"State"
        val suffix = options["COMPOSE_ATTRS_SUFFIX"] ?: "State"

        // 1. 查找所有带有@ComposeAssist注解的函数
        val assistAnnotatedFunctions = resolver
            .getSymbolsWithAnnotation("com.addzero.kmp.annotation.ComposeAssist")
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { it.validate() }
            .toList()

        // 2. 生成预定义的原生组件State
        generatePredefinedNativeComponents(resolver, suffix)

        if (assistAnnotatedFunctions.isEmpty()) {
            logger.warn("找到0个注解标记的函数，已生成原生组件${suffix}")
        } else {
            logger.warn("找到${assistAnnotatedFunctions.size}个注解标记的函数，已生成原生组件${suffix}")

            // 为注解标记的函数生成相关代码
            assistAnnotatedFunctions.forEach { function ->
                generateAssistForFunction(function, suffix)
            }
        }

        return assistAnnotatedFunctions.filterNot { it.validate() }
    }

    /**
     * 生成预定义的原生组件
     */
    private fun generatePredefinedNativeComponents(resolver: Resolver, suffix: String) {
        logger.warn("开始扫描原生Compose组件的${suffix}")

        // 扫描所有@Composable函数
        val nativeComposeFunctions = findNativeComposeFunctions(resolver)

        logger.warn("找到${nativeComposeFunctions.size}个原生Compose组件")

        // 为每个组件生成代码
        nativeComposeFunctions.forEach { function ->
            generateNativeComponentFromFunction(function, suffix)
        }
    }

    /**
     * 查找原生Compose组件函数
     */
    private fun findNativeComposeFunctions(resolver: Resolver): List<KSFunctionDeclaration> {
        val targetPackages = listOf(
            "androidx.compose.material3",
//            "androidx.compose.foundation.layout",
//            "androidx.compose.foundation.lazy"
        )

        val targetComponents = setOf(
            "Text", "Button", "Card", "TextField", "OutlinedTextField",
            "IconButton", "FloatingActionButton", "Switch", "Checkbox",
            "RadioButton", "Slider", "LinearProgressIndicator",
            "CircularProgressIndicator", "Box", "Column", "Row",
            "LazyColumn", "LazyRow"
        )

        return resolver.getSymbolsWithAnnotation("androidx.compose.runtime.Composable")
            .filterIsInstance<KSFunctionDeclaration>()
            .filter { function ->
                val functionName = function.simpleName.asString()
                val packageName = function.packageName.asString()

                functionName in targetComponents &&
                targetPackages.any { packageName.startsWith(it) }
            }
            .toList()
            .also { functions ->
                functions.forEach { function ->
                    logger.warn("找到原生组件: ${function.packageName.asString()}.${function.simpleName.asString()}")
                }
            }
    }

    /**
     * 从真实函数生成原生组件代码
     */
    private fun generateNativeComponentFromFunction(function: KSFunctionDeclaration, suffix: String) {
        val componentName = function.simpleName.asString()
        logger.warn("正在为原生组件 $componentName 生成${suffix}代码")

        // 分析函数参数
        val parameters = analyzeNativeFunctionParameters(function)

        logger.warn("组件 $componentName 有 ${parameters.size} 个参数")

        // 生成代码
        val generatedCode = generateNativeDataClassCode(componentName, parameters, suffix)

        // 写入文件
        writeNativeGeneratedCode(componentName, generatedCode, suffix)
    }

    /**
     * 分析原生函数参数
     */
    private fun analyzeNativeFunctionParameters(function: KSFunctionDeclaration): List<NativeParameterInfo> {
        return function.parameters.mapNotNull { param ->
            val paramName = param.name?.asString() ?: ""
            val paramType = param.type.resolve()

            // 自动排除一些常见的参数
            val isAutoExcluded = paramName in listOf(
                "modifier", "interactionSource"
            )

            if (isAutoExcluded) {
                logger.warn("自动排除参数: $paramName")
                null
            } else {
                val typeString = paramType.getQualifiedTypeString()
                val hasDefault = param.hasDefault
                val defaultValue = generateDefaultValue(paramType, paramName, hasDefault)
                val isComposable = isComposableParameter(paramType)

                logger.warn("参数: $paramName, 类型: $typeString, 默认值: $hasDefault, Composable: $isComposable")

                NativeParameterInfo(
                    name = paramName,
                    type = typeString,
                    hasDefaultValue = hasDefault,
                    defaultValue = defaultValue,
                    isComposable = isComposable
                )
            }
        }
    }

    /**
     * 检查参数是否是@Composable函数
     */
    private fun isComposableParameter(type: KSType): Boolean {
        // 检查是否是函数类型
        if (type.declaration.simpleName.asString().startsWith("Function")) {
            // 检查函数类型的注解
            return type.annotations.any { annotation ->
                annotation.shortName.asString() == "Composable"
            }
        }
        return false
    }

    /**
     * 生成默认值
     */
    private fun generateDefaultValue(type: KSType, paramName: String, hasDefault: Boolean): String {
        if (!hasDefault) {
            return when (type.declaration.simpleName.asString()) {
                "String" -> "\"\""
                "Boolean" -> "false"
                "Int" -> "0"
                "Float" -> "0f"
                "Double" -> "0.0"
                else -> "null"
            }
        }

        // 根据参数名和类型推断默认值
        return when {
            paramName.contains("color", ignoreCase = true) && type.toString().contains("Color") -> {
                if (paramName.contains("container", ignoreCase = true)) "Color.Transparent"
                else "Color.Unspecified"
            }
            paramName.contains("elevation", ignoreCase = true) -> "null"
            paramName.contains("border", ignoreCase = true) -> "null"
            paramName.contains("padding", ignoreCase = true) -> "PaddingValues(0.dp)"
            paramName == "enabled" -> "true"
            paramName == "text" -> "\"\""
            paramName == "${propsXd}ue" -> "\"\""
            paramName.contains("size", ignoreCase = true) && type.toString().contains("TextUnit") -> "TextUnit.Unspecified"
            type.declaration.simpleName.asString() == "String" -> "\"\""
            type.declaration.simpleName.asString() == "Boolean" -> "true"
            type.declaration.simpleName.asString() == "Int" -> "Int.MAX_VALUE"
            type.isMarkedNullable -> "null"
            else -> "TODO(\"需要为${type}类型提供默认值\")"
        }
    }

    /**
     * 分析函数的泛型参数
     */
    private fun analyzeGenericParameters(function: KSFunctionDeclaration): List<GenericParameterInfo> {
        return function.typeParameters.map { typeParam ->
            val name = typeParam.name.asString()
            val allBounds = typeParam.bounds.map { bound ->
                bound.resolve().getQualifiedTypeString()
            }.toList()

            // 过滤掉默认的kotlin.Any?边界，只保留显式声明的边界
            val explicitBounds = allBounds.filter { bound ->
                bound != "kotlin.Any?" && bound != "kotlin.Any"
            }

            logger.warn("找到泛型参数: $name, 所有边界: $allBounds, 显式边界: $explicitBounds")

            GenericParameterInfo(
                name = name,
                bounds = explicitBounds
            )
        }
    }

    /**
     * 为注解标记的函数生成相关代码
     */
    private fun generateAssistForFunction(function: KSFunctionDeclaration, suffix: String) {
        val functionName = function.simpleName.asString()
        val packageName = function.packageName.asString()

        logger.warn("正在为函数 $packageName.$functionName 生成${suffix}代码")

        // 分析函数参数
        val parameters = analyzeParameters(function, false)

        // 生成代码
        val generatedCode = generateAssistCode(functionName, packageName, parameters, suffix, function)

        // 写入文件
        writeGeneratedCode(functionName, packageName, generatedCode, function, suffix)
    }

    /**
     * 分析函数参数
     */
    private fun analyzeParameters(function: KSFunctionDeclaration, isNativeFunction: Boolean = false): List<ParameterInfo> {
        return function.parameters.mapNotNull { param ->
            val paramName = param.name?.asString() ?: ""

            // 检查是否被@AssistExclude排除
            val isExcluded = param.annotations.any {
                it.shortName.asString() == "AssistExclude"
            }

            // 对于原生函数，自动排除一些常见的参数
            val isAutoExcluded = if (isNativeFunction) {
                paramName in listOf(
                    // UI修饰符
                    "modifier",
                    // 事件回调
                    "onClick", "onValueChange", "onCheckedChange", "onSelectionChange",
                    "onLongClick", "onDoubleClick", "onFocusChanged", "onKeyEvent",
                    // @Composable内容
                    "content", "label", "placeholder", "leadingIcon", "trailingIcon",
                    "supportingText", "prefix", "suffix", "icon", "text",
                    // 交互相关
                    "interactionSource", "keyboardOptions", "keyboardActions",
                    "visualTransformation", "singleLine"
                )
            } else {
                false
            }

            if (isExcluded || isAutoExcluded) {
                if (isAutoExcluded) {
                    logger.warn("自动排除原生函数参数: $paramName")
                }
                null
            } else {
                val resolvedType = param.type.resolve()

                // 检查原始类型字符串的可空性，而不是依赖KSP的isMarkedNullable
                val originalTypeString = param.type.toString()
                val isActuallyNullable = originalTypeString.endsWith("?")

                logger.warn("参数 $paramName: 原始类型=$originalTypeString, 解析类型=${resolvedType.getQualifiedTypeString()}, 可空=$isActuallyNullable")

                ParameterInfo(
                    name = paramName,
                    type = resolvedType,
                    hasDefaultValue = param.hasDefault,
                    isNullable = isActuallyNullable
                )
            }
        }
    }

    /**
     * 生成相关代码
     */
    private fun generateAssistCode(
        functionName: String,
        packageName: String,
        parameters: List<ParameterInfo>,
        suffix: String,
        function: KSFunctionDeclaration
    ): String {
        val className = "${functionName}${suffix}"

        // 分析函数的泛型参数
        val genericParameters = analyzeGenericParameters(function)

        // 生成数据类
        val dataClassCode = generateAssistDataClass(className, parameters, genericParameters)

        // 生成remember函数
        val rememberFunctionCode = generateRememberFunction(functionName, className, parameters, genericParameters)

        // 生成Widget辅助函数
        val widgetFunctionCode = generateWidgetFunction(functionName, className, parameters, genericParameters, packageName)

        return """
package $packageName
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState

/**
 * $functionName 函数的辅助工具集
 * 类似Vue的attrs功能，提供完整的Compose开发支持
 * 由KSP自动生成
 */
$dataClassCode

$rememberFunctionCode

$widgetFunctionCode
        """.trimIndent()
    }

    /**
     * 生成Assist数据类
     */
    private fun generateAssistDataClass(className: String, parameters: List<ParameterInfo>, genericParameters: List<GenericParameterInfo> = emptyList()): String {
        // 创建泛型参数名映射，用于简化类型引用
        val genericParamNames = genericParameters.map { it.name }.toSet()

        // 生成构造函数参数
        val constructorParams = parameters.joinToString(",\n    ") { param ->
            val defaultValue = if (param.hasDefaultValue) {
                getActualDefaultValue(param)
            } else ""

            val baseTypeString = simplifyTypeString(param.type.getQualifiedTypeString(), genericParamNames)
            val cleanTypeString = baseTypeString.removeSuffix("?")
            val finalTypeString = if (param.isNullable) "$cleanTypeString?" else cleanTypeString

            "${param.name}: $finalTypeString$defaultValue"
        }

        // 生成MutableState属性声明
        val stateProperties = parameters.joinToString("\n    ") { param ->
            val baseTypeString = simplifyTypeString(param.type.getQualifiedTypeString(), genericParamNames)
            val cleanTypeString = baseTypeString.removeSuffix("?")
            val finalTypeString = if (param.isNullable) "$cleanTypeString?" else cleanTypeString

            "private val _${param.name} = mutableStateOf(${param.name})"
        }

        // 生成公开的var属性
        val publicProperties = parameters.joinToString("\n    ") { param ->
            val baseTypeString = simplifyTypeString(param.type.getQualifiedTypeString(), genericParamNames)
            val cleanTypeString = baseTypeString.removeSuffix("?")
            val finalTypeString = if (param.isNullable) "$cleanTypeString?" else cleanTypeString

            """var ${param.name}: $finalTypeString
        get() = _${param.name}.value
        set(value) { _${param.name}.value = value }"""
        }

        // 生成泛型参数声明
        val genericDeclaration = if (genericParameters.isNotEmpty()) {
            val genericParams = genericParameters.joinToString(", ") { generic ->
                if (generic.bounds.isNotEmpty()) {
                    "${generic.name} : ${generic.bounds.joinToString(" & ")}"
                } else {
                    generic.name
                }
            }
            "<$genericParams>"
        } else ""

        return """
/**
 * 响应式状态类，支持Compose重组
 */
class $className$genericDeclaration(
    $constructorParams
) {
    $stateProperties

    $publicProperties
}
        """.trimIndent()
    }

    /**
     * 简化类型字符串，将全限定的泛型类型简化为简单名称
     */
    private fun simplifyTypeString(typeString: String, genericParamNames: Set<String>): String {
        var simplified = typeString

        // 对于每个泛型参数，将全限定名简化为简单名
        genericParamNames.forEach { paramName ->
            // 匹配类似 "com.addzero.kmp.demo.GenericDisplay.T" 的模式并替换为 "T"
            simplified = simplified.replace(Regex("""[\w.]+\.$paramName\b"""), paramName)
            // 也处理可能的其他格式，如 "com.addzero.kmp.demo.GenericDisplay.T?"
            simplified = simplified.replace(Regex("""[\w.]+\.$paramName(\?)?"""), "$paramName$1")
        }

        // 简化常见的kotlin类型
        simplified = simplified.replace("kotlin.String", "String")
        simplified = simplified.replace("kotlin.Boolean", "Boolean")
        simplified = simplified.replace("kotlin.Int", "Int")
        simplified = simplified.replace("kotlin.Float", "Float")
        simplified = simplified.replace("kotlin.Double", "Double")
        simplified = simplified.replace("kotlin.Any?", "Any?")
        simplified = simplified.replace("kotlin.Number", "Number")

        // 处理函数类型：将 kotlin.Function1<T, R> 转换为 (T) -> R
        simplified = simplified.replace(Regex("""kotlin\.Function1<([^,]+),\s*([^>]+)>"""), "($1) -> $2")
        simplified = simplified.replace(Regex("""kotlin\.Function2<([^,]+),\s*([^,]+),\s*([^>]+)>"""), "($1, $2) -> $3")
        simplified = simplified.replace(Regex("""kotlin\.Function0<([^>]+)>"""), "() -> $1")

        return simplified
    }

    /**
     * 获取参数的真实默认值
     */
    private fun getActualDefaultValue(param: ParameterInfo): String {
        // 如果参数有默认值，我们需要尝试获取真实的默认值
        // 由于KSP限制，我们无法直接获取默认值，所以根据类型和名称推断

        val typeName = param.type.declaration.simpleName.asString()
        val paramName = param.name

        return when {
            // 根据参数名推断默认值
            paramName == "displayName" && typeName == "String" -> " = \"Data\""
            paramName == "format" && typeName == "String" -> " = \"%.2f\""
            paramName == "showValue" && typeName == "Boolean" -> " = true"

            // 根据类型推断默认值
            param.isNullable -> " = null"
            typeName == "String" -> " = \"\""
            typeName == "Boolean" -> " = false"
            typeName == "Int" -> " = 0"
            typeName == "Float" -> " = 0f"
            typeName == "Double" -> " = 0.0"

            // 对于泛型类型，不添加默认值（保持原始的非空性）
            param.type.declaration.simpleName.asString().length == 1 -> ""

            else -> ""
        }
    }



    /**
     * 生成辅助函数
     */
    private fun generateHelperFunctions(
        functionName: String,
        className: String,
        parameters: List<ParameterInfo>,
        suffix: String
    ): String {
        // 生成构建器函数
        val builderFunction = generateBuilderFunction(functionName, className, parameters, suffix)

        return builderFunction
    }

    /**
     * 生成构建器函数
     */
    private fun generateBuilderFunction(
        functionName: String,
        className: String,
        parameters: List<ParameterInfo>,
        suffix: String
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
 * 构建 $className 的便捷函数
 */
fun ${functionName.lowercase()}${suffix}(
    $paramList
): $className {
    return $className(
        $constructorArgs
    )
}
        """.trimIndent()
    }

    /**
     * 生成Widget辅助函数
     */
    private fun generateWidgetFunction(
        functionName: String,
        className: String,
        parameters: List<ParameterInfo>,
        genericParameters: List<GenericParameterInfo>,
        packageName: String
    ): String {
        // 生成泛型参数声明（放在fun后面）
        val genericDeclaration = if (genericParameters.isNotEmpty()) {
            val genericParams = genericParameters.joinToString(", ") { generic ->
                if (generic.bounds.isNotEmpty()) {
                    "${generic.name} : ${generic.bounds.joinToString(" & ")}"
                } else {
                    generic.name
                }
            }
            "<$genericParams> "
        } else ""

        // 生成参数传递
        val parameterPassing = parameters.joinToString(",\n        ") { param ->
            "${param.name} = state.${param.name}"
        }

        // 生成State类型参数（只使用简单的泛型名）
        val stateTypeParams = if (genericParameters.isNotEmpty()) {
            "<${genericParameters.joinToString(", ") { it.name }}>"
        } else ""

        return """
/**
 * $functionName 的Widget辅助函数
 * 只接受打包好的State参数，自动展开所有属性
 */
@Composable
fun ${genericDeclaration}${functionName}Widget(
    state: $className$stateTypeParams
) {
    $functionName(
        $parameterPassing
    )
}
        """.trimIndent()
    }

    /**
     * 生成remember函数
     */
    private fun generateRememberFunction(
        functionName: String,
        className: String,
        parameters: List<ParameterInfo>,
        genericParameters: List<GenericParameterInfo> = emptyList()
    ): String {
        // 生成泛型参数声明
        val genericDeclaration = if (genericParameters.isNotEmpty()) {
            val genericParams = genericParameters.joinToString(", ") { generic ->
                if (generic.bounds.isNotEmpty()) {
                    "${generic.name} : ${generic.bounds.joinToString(" & ")}"
                } else {
                    generic.name
                }
            }
            "<$genericParams>"
        } else ""
        // 创建泛型参数名映射，用于简化类型引用
        val genericParamNames = genericParameters.map { it.name }.toSet()

        val paramList = parameters.joinToString(",\n    ") { param ->
            // 获取真实的默认值
            val defaultValue = if (param.hasDefaultValue) {
                getActualDefaultValue(param)
            } else ""

            // 构建正确的类型字符串
            val baseTypeString = simplifyTypeString(param.type.getQualifiedTypeString(), genericParamNames)
            // 移除KSP可能错误添加的可空性标记
            val cleanTypeString = baseTypeString.removeSuffix("?")
            // 根据我们检测到的真实可空性添加?
            val finalTypeString = if (param.isNullable) "$cleanTypeString?" else cleanTypeString

            "${param.name}: $finalTypeString$defaultValue"
        }

        val constructorArgs = parameters.joinToString(",\n        ") { param ->
            "${param.name} = ${param.name}"
        }

        // 调整泛型声明位置（放在fun后面）
        val adjustedGenericDeclaration = if (genericParameters.isNotEmpty()) {
            val genericParams = genericParameters.joinToString(", ") { generic ->
                if (generic.bounds.isNotEmpty()) {
                    "${generic.name} : ${generic.bounds.joinToString(" & ")}"
                } else {
                    generic.name
                }
            }
            "<$genericParams> "
        } else ""

        val returnClass = """$className${if (genericParameters.isNotEmpty()) "<${genericParameters.joinToString(", ") { it.name }}>" else ""}"""
        return """
/**
 * 记住 $className 状态的便捷函数
 */
@Composable
fun ${adjustedGenericDeclaration}remember${functionName}State(
    $paramList
): $returnClass {
    return remember {
        $className(
            $constructorArgs
        )
    }
}
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
        suffix: String
    ) {
        val fileName = "${functionName}${suffix}"
        val packagePath = packageName.replace(".", "/")

        try {
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(false, function.containingFile!!),
                packageName = packageName,
                fileName = fileName
            )

            file.write(generatedCode.toByteArray())
            file.close()

            logger.warn("成功生成自定义组件文件: $packagePath/$fileName.kt")
        } catch (e: Exception) {
            logger.error("生成文件失败: ${e.message}")
        }
    }

    /**
     * 生成原生组件的代码（使用data class + @get:Composable方式）
     */
    private fun generateNativeDataClassCode(componentName: String, parameters: List<NativeParameterInfo>, suffix: String): String {
        val className = "${componentName}${suffix}"
        val packageName = "com.addzero.kmp.generated.attrs"

        // 生成data class
        val dataClassCode = generateNativeDataClass(className, parameters)

        // 生成构建器函数
        val builderFunctionCode = generateNativeBuilderFunction(componentName, className, parameters, suffix)

        // 生成remember函数
        val rememberFunctionCode = generateNativeRememberFunction(componentName, className, parameters)

        return """
package $packageName
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * $componentName 原生组件的参数配置类
 * 使用data class + @get:Composable支持所有类型参数
 * 类似Vue的attrs功能
 * 由KSP自动生成
 */
$dataClassCode

$builderFunctionCode

$rememberFunctionCode
        """.trimIndent()
    }

    /**
     * 生成data class
     */
    private fun generateNativeDataClass(className: String, parameters: List<NativeParameterInfo>): String {
        val properties = parameters.joinToString(",\n    ") { param ->
            if (param.isComposable) {
                // 对于@Composable参数，使用@get:Composable的getter属性
                "$propsXd ${param.name}: ${param.type}\n        @Composable get() = ${param.defaultValue}"
            } else {
                // 普通参数
                val defaultPart = if (param.hasDefaultValue) " = ${param.defaultValue}" else ""
                "$propsXd ${param.name}: ${param.type}$defaultPart"
            }
        }

        return """
data class $className(
    $properties
)
        """.trimIndent()
    }

    /**
     * 生成旧的lambda闭包类（保留作为备用）
     */
    private fun generateNativeLambdaClass(className: String, componentName: String, parameters: List<NativeParameterInfo>): String {
        val applyFunction = generateApplyFunction(componentName, parameters)

        return """
/**
 * $componentName 组件的参数配置类
 * 使用lambda闭包存储参数配置，支持所有类型的参数包括@Composable函数
 */
class $className {
    // 参数存储
${generateParameterProperties(parameters)}

    // 事件回调存储
    var onClick: (() -> Unit)? = null
    var onValueChange: ((String) -> Unit)? = null
    var onCheckedChange: ((Boolean) -> Unit)? = null

    // @Composable内容存储
    var content: (@Composable () -> Unit)? = null
    var label: (@Composable () -> Unit)? = null
    var placeholder: (@Composable () -> Unit)? = null
    var leadingIcon: (@Composable () -> Unit)? = null
    var trailingIcon: (@Composable () -> Unit)? = null

    /**
     * 配置参数的DSL函数
     */
    fun configure(block: $className.() -> Unit): $className {
        return this.apply(block)
    }

$applyFunction
}
        """.trimIndent()
    }

    /**
     * 生成参数属性
     */
    private fun generateParameterProperties(parameters: List<NativeParameterInfo>): String {
        return parameters.joinToString("\n") { param ->
            "    var ${param.name}: ${param.type} = ${param.defaultValue}"
        }
    }

    /**
     * 生成apply函数
     */
    private fun generateApplyFunction(componentName: String, parameters: List<NativeParameterInfo>): String {
        val parameterList = parameters.joinToString(",\n        ") { param ->
            "${param.name} = this.${param.name}"
        }

        return when (componentName) {
            "Text" -> """
    /**
     * 应用配置并创建Text组件
     */
    @Composable
    fun applyToText(
        modifier: Modifier = Modifier
    ) {
        androidx.compose.material3.Text(
            $parameterList,
            modifier = modifier
        )
    }"""

            "Button" -> """
    /**
     * 应用配置并创建Button组件
     */
    @Composable
    fun applyToButton(
        modifier: Modifier = Modifier
    ) {
        androidx.compose.material3.Button(
            onClick = this.onClick ?: {},
            $parameterList,
            modifier = modifier
        ) {
            this.content?.invoke() ?: Text("Button")
        }
    }"""

            "Card" -> """
    /**
     * 应用配置并创建Card组件
     */
    @Composable
    fun applyToCard(
        modifier: Modifier = Modifier
    ) {
        androidx.compose.material3.Card(
            $parameterList,
            modifier = modifier
        ) {
            this.content?.invoke()
        }
    }"""

            else -> """
    /**
     * 应用配置到${componentName}组件
     * 注意：需要手动实现具体的组件调用
     */
    @Composable
    fun applyTo${componentName}(
        modifier: Modifier = Modifier
    ) {
        // 参数已配置，可以在这里调用原生${componentName}组件
        // 例如：androidx.compose.material3.${componentName}(...)
    }"""
        }
    }



    /**
     * 生成原生组件的构建器函数
     */
    private fun generateNativeBuilderFunction(componentName: String, className: String, parameters: List<NativeParameterInfo>, suffix: String): String {
        val lowerComponentName = componentName.lowercase()
        val paramList = parameters.joinToString(",\n    ") { param ->
            val defaultPart = if (param.hasDefaultValue) " = ${param.defaultValue}" else ""
            "${param.name}: ${param.type}$defaultPart"
        }

        val constructorArgs = parameters.joinToString(",\n        ") { param ->
            "${param.name} = ${param.name}"
        }

        return """
/**
 * 构建 $className 的便捷函数
 */
fun ${lowerComponentName}${suffix}(
    $paramList
): $className {
    return $className(
        $constructorArgs
    )
}
        """.trimIndent()
    }

    /**
     * 生成原生组件的旧构建器函数（保留作为备用）
     */
    private fun generateNativeLambdaBuilderFunction(componentName: String, className: String, parameters: List<NativeParameterInfo>, suffix: String): String {
        val lowerComponentName = componentName.lowercase()

        return """
/**
 * 构建 $className 的便捷函数
 * 使用DSL风格配置参数
 */
fun ${lowerComponentName}${suffix}(
    block: $className.() -> Unit = {}
): $className {
    return $className().apply(block)
}

/**
 * 构建 $className 的便捷函数（带参数）
 */
fun ${lowerComponentName}${suffix}(
${generateBuilderParameters(parameters)},
    block: $className.() -> Unit = {}
): $className {
    return $className().apply {
${generateBuilderAssignments(parameters)}
        block()
    }
}
        """.trimIndent()
    }

    /**
     * 生成构建器参数
     */
    private fun generateBuilderParameters(parameters: List<NativeParameterInfo>): String {
        return parameters.joinToString(",\n") { param ->
            val defaultPart = if (param.hasDefaultValue) " = ${param.defaultValue}" else ""
            "    ${param.name}: ${param.type}$defaultPart"
        }
    }

    /**
     * 生成构建器赋值
     */
    private fun generateBuilderAssignments(parameters: List<NativeParameterInfo>): String {
        return parameters.joinToString("\n") { param ->
            "        this.${param.name} = ${param.name}"
        }
    }

    /**
     * 生成原生组件的remember函数
     */
    private fun generateNativeRememberFunction(componentName: String, className: String, parameters: List<NativeParameterInfo>): String {
        val paramList = parameters.joinToString(",\n    ") { param ->
            val defaultPart = if (param.hasDefaultValue) " = ${param.defaultValue}" else ""
            "${param.name}: ${param.type}$defaultPart"
        }

        val constructorArgs = parameters.joinToString(",\n        ") { param ->
            "${param.name} = ${param.name}"
        }

        return """
/**
 * 记住 $className 状态的便捷函数
 */
@Composable
fun remember${componentName}State(
    $paramList
): MutableState< $className> {
    return remember {
    
    val state = mutableStateOf(
        $className(
            $constructorArgs
        )
    
    )
              state
 
    }
}
        """.trimIndent()
    }

    /**
     * 写入原生组件生成的代码到文件
     */
    private fun writeNativeGeneratedCode(componentName: String, generatedCode: String, suffix: String) {
        val fileName = "${componentName}${suffix}"
        val packageName = "com.addzero.kmp.generated.attrs"
        val packagePath = packageName.replace(".", "/")

        try {
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies.ALL_FILES,
                packageName = packageName,
                fileName = fileName
            )

            file.write(generatedCode.toByteArray())
            file.close()

            logger.warn("成功生成原生组件文件: $packagePath/$fileName.kt")
        } catch (e: Exception) {
            logger.error("生成原生组件文件失败: ${e.message}")
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
 * 原生组件参数信息数据类
 */
data class NativeParameterInfo(
    val name: String,
    val type: String,
    val hasDefaultValue: Boolean,
    val defaultValue: String = "",
    val isComposable: Boolean = false
)

/**
 * 泛型参数信息数据类
 */
data class GenericParameterInfo(
    val name: String,
    val bounds: List<String> = emptyList()
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
