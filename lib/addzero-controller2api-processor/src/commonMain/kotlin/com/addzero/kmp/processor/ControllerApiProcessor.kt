package com.addzero.kmp.processor

import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.context.SettingContext.settings
import com.addzero.kmp.util.isJimmerEntity
import com.addzero.kmp.processor.type.TypeMappingManager
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import java.io.File

/**
 * KSP处理器：解析Controller符号生成Ktorfit接口
 *
 * 使用两阶段处理：
 * 1. process() 阶段：收集元数据
 * 2. finish() 阶段：生成代码
 */
class ControllerApiProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    // 存储收集到的控制器元数据
    private val collectedControllers = mutableListOf<ControllerMetadata>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        SettingContext.initialize(options)
        logger.warn("解析Controller符号生成Ktorfit接口初始化配置: ${SettingContext.settings}")

        val controllerSymbols = resolver
            .getSymbolsWithAnnotation("org.springframework.web.bind.annotation.RestController")
            .filterIsInstance<KSClassDeclaration>()

        if (!controllerSymbols.iterator().hasNext()) {
            return emptyList()
        }

        // 第一阶段：收集元数据，使用 validate() 进行多轮处理
        val invalidSymbols = mutableListOf<KSClassDeclaration>()

        controllerSymbols.forEach { controller ->

            if (controller.validate()) {
                // 符号有效，尝试收集元数据
                collectControllerMetadata(controller)
            } else {
                // 符号无效，推迟到下一轮处理
                logger.info("控制器 ${controller.simpleName.asString()} 暂时无法解析，推迟到下一轮处理")
                invalidSymbols.add(controller)
            }
        }

        // 返回无效的符号，让 KSP 在下一轮重新处理
        return invalidSymbols
    }

    override fun finish() {
        // 第二阶段：生成代码
        logger.info("开始生成代码，共收集到 ${collectedControllers.size} 个控制器")
        collectedControllers.forEach { metadata ->
            generateKtorfitInterfaceFromMetadata(metadata)
        }
        collectedControllers.clear()
    }

    /**
     * 从元数据生成 Ktorfit 接口（第二阶段）
     */
    private fun generateKtorfitInterfaceFromMetadata(metadata: ControllerMetadata) {
        try {
            logger.info("生成 Ktorfit 接口: ${metadata.originalClassName}")

            // 将元数据转换为 ControllerInfo（兼容现有的生成逻辑）
            val controllerInfo = ControllerInfo(
                originalClassName = metadata.originalClassName,
                packageName = metadata.packageName,
                basePath = metadata.basePath,
                methods = metadata.methods.map { methodMetadata ->
                    MethodInfo(
                        name = methodMetadata.name,
                        httpMethod = methodMetadata.httpMethod,
                        path = methodMetadata.path,
                        returnType = methodMetadata.returnTypeString,
                        parameters = methodMetadata.parameters.map { paramMetadata ->
                            ParameterInfo(
                                name = paramMetadata.name,
                                type = paramMetadata.typeString,
                                isRequestBody = paramMetadata.isRequestBody,
                                isPathVariable = paramMetadata.isPathVariable,
                                isRequestParam = paramMetadata.isRequestParam,
                                isRequestPart = paramMetadata.isRequestPart
                            )
                        }
                    )
                }
            )

            // 使用现有的生成逻辑
            generateKtorfitInterface(controllerInfo)

        } catch (e: Exception) {
            logger.error("生成 Ktorfit 接口时发生错误 ${metadata.originalClassName}: ${e.message}")
        }
    }



    /**
     * 收集控制器元数据（第一阶段）
     */
    private fun collectControllerMetadata(controller: KSClassDeclaration) {
        try {
            logger.info("收集控制器元数据: ${controller.qualifiedName?.asString()}")

            val metadata = extractControllerMetadata(controller)
            collectedControllers.add(metadata)

        } catch (e: Exception) {
            logger.error("收集控制器元数据时发生错误 ${controller.qualifiedName?.asString()}: ${e.message}")
        }
    }

    /**
     * 提取控制器元数据（只收集字符串信息，避免类型解析问题）
     */
    private fun extractControllerMetadata(controller: KSClassDeclaration): ControllerMetadata {
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

        val springMvcFunctions = allFunctions.filter { it.isPublic() && it.hasSpringMvcAnnotation() && it.validate() }
        logger.info("Found ${springMvcFunctions.size} valid Spring MVC annotated functions")

        val methods = springMvcFunctions.mapNotNull { function ->
            try {
                logger.info("Processing method: ${function.simpleName.asString()}")
                extractMethodMetadata(function, basePath)
            } catch (e: Exception) {
                logger.warn("跳过方法 ${function.simpleName.asString()}: ${e.message}")
                null
            }
        }

        return ControllerMetadata(
            originalClassName = className,
            packageName = packageName,
            basePath = basePath,
            methods = methods
        )
    }

    /**
     * 提取方法元数据（只收集字符串信息，避免类型解析问题）
     */
    private fun extractMethodMetadata(function: KSFunctionDeclaration, basePath: String): MethodMetadata {
        val methodName = function.simpleName.asString()

        // 提取HTTP方法和路径
        val httpInfo = extractHttpMapping(function)
        val fullPath = if (basePath.isNotEmpty()) "$basePath${httpInfo.path}" else httpInfo.path

        // 提取参数（只收集字符串信息）
        val parameters = function.parameters.map { param ->
            try {
                val paramName = param.name?.asString() ?: ""

                // 使用类型映射管理器进行类型转换
                val typeString = TypeMappingManager.mapParameterType(param)

                ParameterMetadata(
                    name = paramName,
                    typeString = typeString,
                    isRequestBody = hasAnnotation(param, "RequestBody"),
                    isPathVariable = hasAnnotation(param, "PathVariable"),
                    isRequestParam = hasAnnotation(param, "RequestParam"),
                    isRequestPart = hasAnnotation(param, "RequestPart")
                )
            } catch (e: Exception) {
                logger.error("处理参数 ${param.name?.asString()} 时发生严重错误: ${e.message}")
                // 即使发生错误，也要保留参数，使用基本信息
                ParameterMetadata(
                    name = param.name?.asString() ?: "unknownParam",
                    typeString = "Any",
                    isRequestBody = hasAnnotation(param, "RequestBody"),
                    isPathVariable = hasAnnotation(param, "PathVariable"),
                    isRequestParam = hasAnnotation(param, "RequestParam"),
                    isRequestPart = hasAnnotation(param, "RequestPart")
                )
            }
        }

        // 使用类型映射管理器进行返回类型转换
        val returnTypeString = TypeMappingManager.mapReturnType(function)

        return MethodMetadata(
            name = methodName,
            httpMethod = httpInfo.method,
            path = fullPath,
            parameters = parameters,
            returnTypeString = returnTypeString
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
     * 提取类型字符串（安全的字符串提取，遇到 Jimmer 实体自动转换为同构体）
     */
    private fun extractTypeString(type: KSType): String {
        try {
            val declaration = type.declaration
            val qualifiedName = declaration.qualifiedName?.asString()

            // 检查是否为 Jimmer 实体，如果是则转换为同构体类型
            if (isJimmerEntity(declaration)) {
                val entitySimpleName = declaration.simpleName.asString()
                return "com.addzero.kmp.generated.isomorphic.${entitySimpleName}Iso"
            }

            // 特殊类型映射
            val mappedType = mapSpecialTypes(qualifiedName ?: "")
            if (mappedType != null) {
                return mappedType
            }

            return when {
                // 处理泛型类型
                type.arguments.isNotEmpty() -> {
                    val baseType = qualifiedName ?: ""
                    val mappedBaseType = mapSpecialTypes(baseType) ?: baseType
                    val typeArgs = type.arguments.joinToString(", ") { arg ->
                        when (val argType = arg.type?.resolve()) {
                            null -> "*"
                            else -> extractTypeString(argType)
                        }
                    }
                    "$mappedBaseType<$typeArgs>"
                }
                // 处理基础类型
                else -> qualifiedName ?: declaration.simpleName.asString()
            }
        } catch (e: Exception) {
            logger.error("获取类型名称时发生错误: ${e.message}")
            // 回退到简单名称
            return type.declaration.simpleName.asString()
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
     * 转换原始类型字符串为合适的 Kotlin 类型
     * 处理无法解析的类型，尝试转换为 Iso 类型或保持原样
     */
    private fun convertRawTypeString(rawTypeString: String): String {
        // 移除泛型参数，获取基础类型名
        val baseTypeName = rawTypeString.substringBefore('<').trim()

        // 检查是否包含非ASCII字符（如中文），如果是则使用 Any 类型
        if (baseTypeName.any { !it.isLetterOrDigit() && it != '.' && it != '_' && it != '$' }) {
            logger.warn("类型名称包含特殊字符，使用 Any 类型: $baseTypeName")
            return "kotlin.Any"
        }

        // 检查是否是已知的 DTO 类型，转换为 Iso 类型
        return when {
            // 如果是以 DTO 结尾的类型，转换为 Iso 类型
            baseTypeName.endsWith("DTO") -> {
                val entityName = baseTypeName.removeSuffix("DTO")
                "com.addzero.kmp.generated.isomorphic.${entityName}Iso"
            }

            // 如果是以 Response 结尾的类型，转换为 Iso 类型
            baseTypeName.endsWith("Response") -> {
                val entityName = baseTypeName.removeSuffix("Response")
                "com.addzero.kmp.generated.isomorphic.${entityName}Iso"
            }

            // 如果包含包名，提取简单类名并转换
            baseTypeName.contains('.') -> {
                val simpleName = baseTypeName.substringAfterLast('.')
                when {
                    simpleName.endsWith("DTO") -> {
                        val entityName = simpleName.removeSuffix("DTO")
                        "com.addzero.kmp.generated.isomorphic.${entityName}Iso"
                    }
                    simpleName.endsWith("Response") -> {
                        val entityName = simpleName.removeSuffix("Response")
                        "com.addzero.kmp.generated.isomorphic.${entityName}Iso"
                    }
                    else -> rawTypeString // 保持原样
                }
            }

            // 其他情况保持原样
            else -> rawTypeString
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
                        extractTypeString(argType)
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
                        "kotlin.collections.List<${extractTypeString(argType)}>"
                    } ?: "kotlin.collections.List<kotlin.Any>"
                } else {
                    "kotlin.collections.List<kotlin.Any>"
                }
            }
            else -> extractTypeString(type)
        }
    }

    private fun generateKtorfitInterface(controllerInfo: ControllerInfo) {
        val apiClassName = controllerInfo.originalClassName.replace("Controller", "Api")
        val fileName = "${apiClassName}.kt"

        try {
            // 从SettingContext获取配置
            val outputDir = settings.apiClientOutputDir


            logger.info("api客户端输出目录为: $outputDir")

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
                packageName = settings.apiClientPackageName,
                fileName = apiClassName
            )

            file.use { outputStream ->
                val code = generateKtorfitCode(controllerInfo, apiClassName, "com.addzero.kmp.api")
                outputStream.write(code.toByteArray())
            }

            logger.info("Fallback: Generated Ktorfit interface to build directory: com.addzero.kmp.generated.api.$apiClassName")
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
     * 收集需要导入的类型（安全版本，过滤错误类型）
     */
    private fun collectImports(controllerInfo: ControllerInfo): Set<String> {
        val imports = mutableSetOf<String>()

        // 添加Ktorfit相关导入
        imports.add("de.jensklingenberg.ktorfit.http.*")

        // 检查是否需要Ktor相关导入
        var needsMultiPartImport = false

        // 收集方法中使用的类型
        controllerInfo.methods.forEach { method ->
            // 收集返回类型（安全处理）
            if (method.returnType != "Unit" && method.returnType.contains(".")) {
                val baseType = method.returnType.substringBeforeLast("<")
                if (isValidTypeForImport(baseType)) {
                    imports.add(baseType)
                }

                // 检查是否包含MultiPartFormDataContent
                if (method.returnType.contains("MultiPartFormDataContent")) {
                    needsMultiPartImport = true
                }
            }

            // 收集参数类型（安全处理）
            method.parameters.forEach { param ->
                if (param.type.contains(".")) {
                    val baseType = param.type.substringBeforeLast("<")
                    if (isValidTypeForImport(baseType)) {
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
     * 检查类型是否适合导入
     */
    private fun isValidTypeForImport(typeName: String): Boolean {
        return !typeName.startsWith("kotlin.") &&
               !typeName.contains("<ERROR") &&
               !typeName.any { !it.isLetterOrDigit() && it != '.' && it != '_' && it != '$' } &&
               typeName.isNotBlank()
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
 * 输出目录: ${SettingContext.settings.apiClientOutputDir}
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
        return ControllerApiProcessor(environment.codeGenerator, environment.logger,environment.options)
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

// ===== 元数据类（第一阶段收集的数据）=====
data class ParameterMetadata(
    val name: String,
    val typeString: String,
    val isRequestBody: Boolean = false,
    val isPathVariable: Boolean = false,
    val isRequestParam: Boolean = false,
    val isRequestPart: Boolean = false
)

data class MethodMetadata(
    val name: String,
    val httpMethod: String,
    val path: String,
    val parameters: List<ParameterMetadata>,
    val returnTypeString: String
)

data class ControllerMetadata(
    val originalClassName: String,
    val packageName: String,
    val basePath: String,
    val methods: List<MethodMetadata>
)
