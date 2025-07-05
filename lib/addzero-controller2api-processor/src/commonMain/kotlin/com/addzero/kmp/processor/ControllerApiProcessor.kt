package com.addzero.kmp.processor

import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.context.apiclientOutPutDir
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import java.io.File

/**
 * KSP处理器：解析Controller符号生成Ktorfit接口
 */
class ControllerApiProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val controllerSymbols = resolver
            .getSymbolsWithAnnotation("org.springframework.web.bind.annotation.RestController")
            .filterIsInstance<KSClassDeclaration>()

        if (!controllerSymbols.iterator().hasNext()) {
            return emptyList()
        }

        controllerSymbols.forEach { controller ->
            processController(controller)
        }

        return controllerSymbols.filterNot { it.validate() }.toList()
    }

    private fun processController(controller: KSClassDeclaration) {
        try {
            logger.info("Processing controller: ${controller.qualifiedName?.asString()}")

            val controllerInfo = extractControllerInfo(controller)
            generateKtorfitInterface(controllerInfo)

        } catch (e: Exception) {
            logger.error("Error processing controller ${controller.qualifiedName?.asString()}: ${e.message}")
        }
    }

    private fun extractControllerInfo(controller: KSClassDeclaration): ControllerInfo {
        val className = controller.simpleName.asString()
        val packageName = controller.packageName.asString()

        // 提取RequestMapping基础路径
        val requestMapping = controller.annotations.find {
            it.shortName.asString() == "RequestMapping"
        }
        val basePath = if (requestMapping != null) {
            getPathFromAnnotation(requestMapping)
        } else {
            ""
        }

        // 提取所有带有Spring MVC注解的方法
        val allFunctions = controller.getAllFunctions().toList()
        logger.info("Controller ${className} has ${allFunctions.size} total functions")

        val springMvcFunctions = allFunctions.filter { it.isPublic() && it.hasSpringMvcAnnotation() }
        logger.info("Found ${springMvcFunctions.size} Spring MVC annotated functions")

        val methods = springMvcFunctions.map { function ->
            logger.info("Processing method: ${function.simpleName.asString()}")
            extractMethodInfo(function, basePath)
        }

        return ControllerInfo(
            originalClassName = className,
            packageName = packageName,
            basePath = basePath,
            methods = methods
        )
    }

    private fun extractMethodInfo(function: KSFunctionDeclaration, basePath: String): MethodInfo {
        val methodName = function.simpleName.asString()

        // 提取HTTP方法和路径
        val httpInfo = extractHttpMapping(function)
        val fullPath = if (basePath.isNotEmpty()) "$basePath${httpInfo.path}" else httpInfo.path

        // 提取参数
        val parameters = function.parameters.map { param ->
            val resolvedType = param.type.resolve()
            ParameterInfo(
                name = param.name?.asString() ?: "",
                type = getFullQualifiedTypeName(resolvedType),
                isRequestBody = hasAnnotation(param, "RequestBody"),
                isPathVariable = hasAnnotation(param, "PathVariable"),
                isRequestParam = hasAnnotation(param, "RequestParam"),
                isRequestPart = hasAnnotation(param, "RequestPart")
            )
        }

        // 提取返回类型
        val returnType = function.returnType?.resolve()?.let { resolvedType ->
            processReturnType(resolvedType)
        } ?: "Unit"

        return MethodInfo(
            name = methodName,
            httpMethod = httpInfo.method,
            path = fullPath,
            parameters = parameters,
            returnType = returnType
        )
    }

    private fun extractHttpMapping(function: KSFunctionDeclaration): HttpMappingInfo {
        val annotations = function.annotations.toList()

        // 检查各种HTTP映射注解
        for (annotation in annotations) {
            when (annotation.shortName.asString()) {
                "GetMapping" -> return HttpMappingInfo("GET", getPathFromAnnotation(annotation))
                "PostMapping" -> return HttpMappingInfo("POST", getPathFromAnnotation(annotation))
                "PutMapping" -> return HttpMappingInfo("PUT", getPathFromAnnotation(annotation))
                "DeleteMapping" -> return HttpMappingInfo("DELETE", getPathFromAnnotation(annotation))
                "PatchMapping" -> return HttpMappingInfo("PATCH", getPathFromAnnotation(annotation))
                "RequestMapping" -> {
                    val method = annotation.arguments.find { it.name?.asString() == "method" }
                        ?.value?.toString()?.substringAfter(".")?.removeSurrounding("\"") ?: "GET"
                    return HttpMappingInfo(method, getPathFromAnnotation(annotation))
                }
            }
        }

        return HttpMappingInfo("GET", "")
    }

    private fun getPathFromAnnotation(annotation: KSAnnotation): String {
        val pathValue = annotation.arguments.find {
            it.name?.asString() == "value" || it.name?.asString() == "path"
        }?.value?.toString() ?: ""

        // 清理路径值，移除多余的引号和方括号
        return pathValue
            .removeSurrounding("\"")
            .removeSurrounding("[", "]")
            .removeSurrounding("\"")
            .trim()
    }

    private fun hasAnnotation(param: KSValueParameter, annotationName: String): Boolean {
        return param.annotations.any { it.shortName.asString() == annotationName }
    }

    /**
     * 获取类型的全限定名
     */
    private fun getFullQualifiedTypeName(type: KSType): String {
        val declaration = type.declaration
        val qualifiedName = declaration.qualifiedName?.asString()

        // 特殊类型映射
        val mappedType = mapSpecialTypes(qualifiedName ?: declaration.simpleName.asString())
        if (mappedType != null) {
            return mappedType
        }

        return when {
            // 处理泛型类型
            type.arguments.isNotEmpty() -> {
                val baseType = qualifiedName ?: declaration.simpleName.asString()
                val mappedBaseType = mapSpecialTypes(baseType) ?: baseType
                val typeArgs = type.arguments.joinToString(", ") { arg ->
                    when (val argType = arg.type?.resolve()) {
                        null -> "*"
                        else -> getFullQualifiedTypeName(argType)
                    }
                }
                "$mappedBaseType<$typeArgs>"
            }
            // 处理基础类型
            qualifiedName != null -> qualifiedName
            // 回退到简单名称
            else -> declaration.simpleName.asString()
        }
    }

    /**
     * 映射特殊类型
     */
    private fun mapSpecialTypes(typeName: String): String? {
        return when (typeName) {
            // Spring MultipartFile -> Ktor MultiPartFormDataContent
            "org.springframework.web.multipart.MultipartFile",
            "MultipartFile" -> "io.ktor.client.request.forms.MultiPartFormDataContent"

            // Spring ResponseEntity -> 直接使用泛型参数类型
            "org.springframework.http.ResponseEntity",
            "ResponseEntity" -> null // 需要特殊处理，提取泛型参数

            // 其他Spring类型映射
            "org.springframework.data.domain.Page" -> "kotlin.collections.List" // Page -> List
            "org.springframework.data.domain.Pageable" -> null // Pageable通常不需要在客户端

            else -> null
        }
    }

    /**
     * 处理返回类型，特别处理ResponseEntity
     */
    private fun processReturnType(type: KSType): String {
        val declaration = type.declaration
        val qualifiedName = declaration.qualifiedName?.asString()

        return when (qualifiedName) {
            "org.springframework.http.ResponseEntity" -> {
                // ResponseEntity<T> -> T
                if (type.arguments.isNotEmpty()) {
                    val firstArg = type.arguments.first()
                    firstArg.type?.resolve()?.let { argType ->
                        getFullQualifiedTypeName(argType)
                    } ?: "kotlin.Any"
                } else {
                    "kotlin.Any"
                }
            }
            "org.springframework.data.domain.Page" -> {
                // Page<T> -> List<T>
                if (type.arguments.isNotEmpty()) {
                    val firstArg = type.arguments.first()
                    firstArg.type?.resolve()?.let { argType ->
                        "kotlin.collections.List<${getFullQualifiedTypeName(argType)}>"
                    } ?: "kotlin.collections.List<kotlin.Any>"
                } else {
                    "kotlin.collections.List<kotlin.Any>"
                }
            }
            else -> getFullQualifiedTypeName(type)
        }
    }

    private fun generateKtorfitInterface(controllerInfo: ControllerInfo) {
        val apiClassName = controllerInfo.originalClassName.replace("Controller", "Api")
        val fileName = "${apiClassName}.kt"

        try {
            // 从SettingContext获取配置
            val settings = SettingContext.settings
            val outputDir = settings.apiclientOutPutDir
            val packageName = settings.apiClientPackageName

            // 创建输出目录
            val outputDirFile = File(outputDir)
            if (!outputDirFile.exists()) {
                outputDirFile.mkdirs()
                logger.info("Created output directory: $outputDir")
            }

            // 生成文件到源码目录
            val outputFile = File(outputDirFile, fileName)
            val code = generateKtorfitCode(controllerInfo, apiClassName, packageName)

            outputFile.writeText(code)

            logger.info("Generated Ktorfit interface: $packageName.$apiClassName")
            logger.info("Output file: ${outputFile.absolutePath}")

        } catch (e: Exception) {
            logger.error("Failed to generate Ktorfit interface for ${controllerInfo.originalClassName}: ${e.message}")
            // 回退到使用CodeGenerator
            fallbackToCodeGenerator(controllerInfo, apiClassName)
        }
    }

    /**
     * 回退方案：使用CodeGenerator生成到build目录
     */
    private fun fallbackToCodeGenerator(controllerInfo: ControllerInfo, apiClassName: String) {
        try {
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = "com.addzero.kmp.api",
                fileName = apiClassName
            )

            file.use { outputStream ->
                val code = generateKtorfitCode(controllerInfo, apiClassName, "com.addzero.kmp.api")
                outputStream.write(code.toByteArray())
            }

            logger.info("Fallback: Generated Ktorfit interface to build directory: com.addzero.kmp.api.$apiClassName")
        } catch (e: Exception) {
            logger.error("Fallback generation also failed: ${e.message}")
        }
    }

    private fun generateKtorfitCode(controllerInfo: ControllerInfo, apiClassName: String, packageName: String): String {
        // 收集所有需要导入的类型
        val imports = collectImports(controllerInfo)

        return generateCodeWithTemplate(controllerInfo, apiClassName, packageName, imports)
    }

    /**
     * 使用模板字符串生成单个方法
     */
//    private fun generateMethodWithTemplate(method: MethodInfo): String {
//        val methodDoc = generateMethodDocumentation(method)
//        val httpAnnotation = generateHttpAnnotation(method)
////        val headersAnnotation = if (method.httpMethod in listOf("POST", "PUT", "PATCH")) {
////            "    @Headers(\"Content-Type: application/json\")\n"
////        } else {
////            ""
////        }
//        val methodSignature = generateMethodSignature(method)
//
//        return """
//$methodDoc
//$httpAnnotation$methodSignature
//        """.trimIndent()
//    }

    /**
     * 生成方法文档
     */
//    private fun generateMethodDocumentation(method: MethodInfo): String {
//        val paramDocs = if (method.parameters.isNotEmpty()) {
//            val paramLines = method.parameters.joinToString("\n") { param ->
//                val paramType = when {
//                    param.isRequestBody -> "RequestBody"
//                    param.isPathVariable -> "PathVariable"
//                    param.isRequestParam -> "RequestParam"
//                    else -> "Query"
//                }
//                "     *   - ${param.name}: ${param.type} ($paramType)"
//            }
//            "\n     * 参数:\n$paramLines"
//        } else {
//            ""
//        }
//
//        return """
//    /**
//     * ${method.name}
//     * HTTP方法: ${method.httpMethod}
//     * 路径: ${method.path}$paramDocs
//     * 返回类型: ${method.returnType}
//     */""".trimIndent()
//    }

    /**
     * 生成HTTP注解
     */
//    private fun generateHttpAnnotation(method: MethodInfo): String {
//        return if (method.path.isNotEmpty()) {
//            "    @${method.httpMethod}(\"${method.path}\")"
//        } else {
//            "    @${method.httpMethod}"
//        }
//    }

    /**
     * 生成方法签名
     */
//    private fun generateMethodSignature(method: MethodInfo): String {
//        val parameters = method.parameters.joinToString(",\n        ") { param ->
//            val annotation = when {
//                param.isRequestBody -> "@Body"
//                param.isPathVariable -> "@Path(\"${param.name}\")"
//                param.isRequestParam -> "@Query(\"${param.name}\")"
//                else -> "@Query(\"${param.name}\")"
//            }
//            "$annotation ${param.name}: ${param.type}"
//        }
//
//        val paramString = if (parameters.isNotEmpty()) {
//            "\n        $parameters\n    "
//        } else {
//            ""
//        }
//
//        return "    suspend fun ${method.name}($paramString): ${method.returnType}"
//    }

    /**
     * 收集需要导入的类型
     */
    private fun collectImports(controllerInfo: ControllerInfo): Set<String> {
        val imports = mutableSetOf<String>()

        // 添加Ktorfit相关导入
        imports.add("de.jensklingenberg.ktorfit.http.*")

        // 检查是否需要Ktor相关导入
        var needsMultiPartImport = false

        // 收集方法中使用的类型
        controllerInfo.methods.forEach { method ->
            // 收集返回类型
            if (method.returnType != "Unit" && method.returnType.contains(".")) {
                val baseType = method.returnType.substringBeforeLast("<")
                if (!baseType.startsWith("kotlin.")) {
                    imports.add(baseType)
                }

                // 检查是否包含MultiPartFormDataContent
                if (method.returnType.contains("MultiPartFormDataContent")) {
                    needsMultiPartImport = true
                }
            }

            // 收集参数类型
            method.parameters.forEach { param ->
                if (param.type.contains(".")) {
                    val baseType = param.type.substringBeforeLast("<")
                    if (!baseType.startsWith("kotlin.")) {
                        imports.add(baseType)
                    }
                }

                // 检查是否包含MultiPartFormDataContent
                if (param.type.contains("MultiPartFormDataContent")) {
                    needsMultiPartImport = true
                }
            }
        }

        // 添加Ktor MultiPart相关导入
        if (needsMultiPartImport) {
            imports.add("io.ktor.client.request.forms.MultiPartFormDataContent")
            imports.add("io.ktor.client.request.forms.formData")
            imports.add("io.ktor.http.Headers")
            imports.add("io.ktor.http.HttpHeaders")
        }

        return imports
    }

    /**
     * 使用模板字符串生成代码
     */
    private fun generateCodeWithTemplate(
        controllerInfo: ControllerInfo,
        apiClassName: String,
        packageName: String,
        imports: Set<String>
    ): String {
        val importStatements = imports.joinToString("\n") { "import $it" }
        val methodsCode = controllerInfo.methods.joinToString("\n\n") { method ->
            generateMethodWithTemplate(method)
        }

        return """
package $packageName

$importStatements

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: ${controllerInfo.packageName}.${controllerInfo.originalClassName}
 * 基础路径: ${controllerInfo.basePath}
 * 输出目录: ${SettingContext.settings.apiclientOutPutDir}
 */
interface $apiClassName {

$methodsCode

}
        """.trimIndent()
    }

    /**
     * 使用模板字符串生成单个方法
     */
    private fun generateMethodWithTemplate(method: MethodInfo): String {
        val methodDoc = generateMethodDocumentation(method)
        val httpAnnotation = generateHttpAnnotation(method)
//        val headersAnnotation = generateHeadersAnnotation(method)
        val methodSignature = generateMethodSignature(method)

        return """
$methodDoc
$httpAnnotation$methodSignature
        """.trimIndent()
    }

    /**
     * 生成Headers注解
     */
    private fun generateHeadersAnnotation(method: MethodInfo): String {
        return when {
            // 检查是否包含文件上传参数或@RequestPart参数
            method.parameters.any {
                it.type.contains("MultiPartFormDataContent") || it.isRequestPart
            } -> {
                "    @Headers(\"Content-Type: multipart/form-data\")\n"
            }
            // 普通的POST/PUT/PATCH请求
            method.httpMethod in listOf("POST", "PUT", "PATCH") -> {
                "    @Headers(\"Content-Type: application/json\")\n"
            }
            else -> ""
        }
    }

    /**
     * 生成方法文档
     */
    private fun generateMethodDocumentation(method: MethodInfo): String {
        val paramDocs = if (method.parameters.isNotEmpty()) {
            val paramLines = method.parameters.joinToString("\n") { param ->
                val paramType = when {
                    param.isRequestBody -> "RequestBody"
                    param.isPathVariable -> "PathVariable"
                    param.isRequestParam -> "RequestParam"
                    param.isRequestPart -> "RequestPart"
                    else -> "Query"
                }
                "     *   - ${param.name}: ${param.type} ($paramType)"
            }
            "\n     * 参数:\n$paramLines"
        } else {
            ""
        }

        return """
    /**
     * ${method.name}
     * HTTP方法: ${method.httpMethod}
     * 路径: ${method.path}$paramDocs
     * 返回类型: ${method.returnType}
     */""".trimIndent()
    }

    /**
     * 生成HTTP注解
     */
    private fun generateHttpAnnotation(method: MethodInfo): String {
        return if (method.path.isNotEmpty()) {
            "    @${method.httpMethod}(\"${method.path}\")"
        } else {
            "    @${method.httpMethod}"
        }
    }

    /**
     * 生成方法签名
     */
    private fun generateMethodSignature(method: MethodInfo): String {
        val parameters = method.parameters.joinToString(",\n        ") { param ->
            val annotation = when {
                param.isRequestBody -> "@Body"
                param.isRequestPart -> "@Body"
                param.isPathVariable -> "@Path(\"${param.name}\")"
                param.isRequestParam -> "@Query(\"${param.name}\")"
                else -> "@Query(\"${param.name}\")"
            }
            "$annotation ${param.name}: ${param.type}"
        }

        val paramString = if (parameters.isNotEmpty()) {
            "\n        $parameters\n    "
        } else {
            ""
        }

        return "    suspend fun ${method.name}($paramString): ${method.returnType}"
    }



    // 数据类定义
    data class ControllerInfo(
        val originalClassName: String,
        val packageName: String,
        val basePath: String,
        val methods: List<MethodInfo>
    )

    data class MethodInfo(
        val name: String,
        val httpMethod: String,
        val path: String,
        val parameters: List<ParameterInfo>,
        val returnType: String
    )

    data class ParameterInfo(
        val name: String,
        val type: String,
        val isRequestBody: Boolean,
        val isPathVariable: Boolean,
        val isRequestParam: Boolean,
        val isRequestPart: Boolean
    )

    data class HttpMappingInfo(
        val method: String,
        val path: String
    )
}

/**
 * KSP处理器提供者
 */
class ControllerApiProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ControllerApiProcessor(environment.codeGenerator, environment.logger)
    }
}

/**
 * 扩展函数：检查函数是否为public
 */
private fun KSFunctionDeclaration.isPublic(): Boolean {
    return modifiers.contains(Modifier.PUBLIC) ||
            (!modifiers.contains(Modifier.PRIVATE) && !modifiers.contains(Modifier.PROTECTED))
}

/**
 * 扩展函数：检查函数是否有Spring MVC注解
 */
private fun KSFunctionDeclaration.hasSpringMvcAnnotation(): Boolean {
    val springMvcAnnotations = setOf(
        "GetMapping",
        "PostMapping",
        "PutMapping",
        "DeleteMapping",
        "PatchMapping",
        "RequestMapping"
    )

    return annotations.any { annotation ->
        springMvcAnnotations.contains(annotation.shortName.asString())
    }
}
