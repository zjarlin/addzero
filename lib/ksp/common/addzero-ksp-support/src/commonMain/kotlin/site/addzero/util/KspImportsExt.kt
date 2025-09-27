package site.addzero.util

import com.google.devtools.ksp.symbol.*


/**
 * 获取 KSDeclaration 的所有导包语句
 * 分析类型声明及其相关类型，收集所有需要导入的包
 */
val KSDeclaration.allImports: Set<String>
    get() = collectAllImports()

/**
 * 收集 KSDeclaration 的所有导包语句
 * 包括：
 * 1. 类本身的包
 * 2. 父类/接口的包
 * 3. 属性类型的包
 * 4. 泛型参数的包
 * 5. 注解的包
 */
fun KSDeclaration.collectAllImports(): Set<String> {
    val imports = mutableSetOf<String>()

    // 1. 收集当前类型的包
    this.qualifiedName?.asString()?.let { qualifiedName ->
        val packageName = qualifiedName.substringBeforeLast(".", "")
        if (packageName.isNotEmpty() && !isKotlinBuiltinPackage(packageName)) {
            imports.add(qualifiedName)
        }
    }

    // 2. 如果是类声明，收集更多信息
    if (this is KSClassDeclaration) {
        // 收集父类型的导包
        this.superTypes.forEach { superType ->
            imports.addAll(superType.resolve().collectTypeImports())
        }

        // 收集属性的导包
        this.getAllProperties().forEach { property ->
            imports.addAll(property.collectPropertyImports())
        }

        // 收集函数的导包
        this.getAllFunctions().forEach { function ->
            imports.addAll(function.collectFunctionImports())
        }

        // 收集类型参数的导包
        this.typeParameters.forEach { typeParam ->
            typeParam.bounds.forEach { bound ->
                imports.addAll(bound.resolve().collectTypeImports())
            }
        }
    }

    // 3. 收集注解的导包
    this.annotations.forEach { annotation ->
        imports.addAll(annotation.collectAnnotationImports())
    }

    return imports.filter { it.isNotEmpty() }.toSet()
}

/**
 * 收集 KSType 的导包信息
 */
fun KSType.collectTypeImports(): Set<String> {
    val imports = mutableSetOf<String>()

    // 主类型
    this.declaration.qualifiedName?.asString()?.let { qualifiedName ->
        val packageName = qualifiedName.substringBeforeLast(".", "")
        if (packageName.isNotEmpty() && !isKotlinBuiltinPackage(packageName)) {
            imports.add(qualifiedName)
        }
    }

    // 泛型参数
    this.arguments.forEach { argument ->
        argument.type?.resolve()?.let { argType ->
            imports.addAll(argType.collectTypeImports())
        }
    }

    return imports
}

/**
 * 收集属性的导包信息
 */
fun KSPropertyDeclaration.collectPropertyImports(): Set<String> {
    val imports = mutableSetOf<String>()

    // 属性类型
    imports.addAll(this.type.resolve().collectTypeImports())

    // 属性注解
    this.annotations.forEach { annotation ->
        imports.addAll(annotation.collectAnnotationImports())
    }

    return imports
}

/**
 * 收集函数的导包信息
 */
fun KSFunctionDeclaration.collectFunctionImports(): Set<String> {
    val imports = mutableSetOf<String>()

    // 返回类型
    this.returnType?.resolve()?.let { returnType ->
        imports.addAll(returnType.collectTypeImports())
    }

    // 参数类型
    this.parameters.forEach { param ->
        imports.addAll(param.type.resolve().collectTypeImports())
        // 参数注解
        param.annotations.forEach { annotation ->
            imports.addAll(annotation.collectAnnotationImports())
        }
    }

    // 函数注解
    this.annotations.forEach { annotation ->
        imports.addAll(annotation.collectAnnotationImports())
    }

    // 类型参数
    this.typeParameters.forEach { typeParam ->
        typeParam.bounds.forEach { bound ->
            imports.addAll(bound.resolve().collectTypeImports())
        }
    }

    return imports
}

/**
 * 收集注解的导包信息
 */
fun KSAnnotation.collectAnnotationImports(): Set<String> {
    val imports = mutableSetOf<String>()

    // 注解类型本身
    this.annotationType.resolve().declaration.qualifiedName?.asString()?.let { qualifiedName ->
        val packageName = qualifiedName.substringBeforeLast(".", "")
        if (packageName.isNotEmpty() && !isKotlinBuiltinPackage(packageName)) {
            imports.add(qualifiedName)
        }
    }

    // 注解参数中的类型
    this.arguments.forEach { argument ->
        when (val value = argument.value) {
            is KSType -> imports.addAll(value.collectTypeImports())
            is KSAnnotation -> imports.addAll(value.collectAnnotationImports())
            is List<*> -> {
                value.forEach { item ->
                    when (item) {
                        is KSType -> imports.addAll(item.collectTypeImports())
                        is KSAnnotation -> imports.addAll(item.collectAnnotationImports())
                    }
                }
            }
        }
    }

    return imports
}

/**
 * 判断是否为 Kotlin 内置包，这些包通常不需要显式导入
 */
fun isKotlinBuiltinPackage(packageName: String): Boolean {
    return packageName in setOf(
        "kotlin",
        "kotlin.collections",
        "kotlin.ranges",
        "kotlin.sequences",
        "kotlin.text",
        "kotlin.io",
        "kotlin.math",
        "kotlin.random",
        "kotlin.comparisons",
        "kotlin.contracts",
        "kotlin.experimental",
        "kotlin.properties",
        "kotlin.reflect",
        "kotlin.system",
        "kotlin.time",
        "kotlin.uuid",
        "java.lang"
    ) || packageName.startsWith("kotlin.jvm") || packageName.startsWith("kotlin.js")
}

/**
 * 获取格式化的导包语句列表
 * @param excludePackages 要排除的包名集合
 * @param includeCurrentPackage 是否包含当前类所在的包
 */
fun KSDeclaration.getFormattedImports(
    excludePackages: Set<String> = emptySet(),
    includeCurrentPackage: Boolean = false
): List<String> {
    val currentPackage = this.packageName.asString()

    return this.allImports
        .filter { import ->
            // 排除指定的包
            val packageName = import.substringBeforeLast(".", "")
            packageName !in excludePackages
        }
        .filter { import ->
            // 根据参数决定是否包含当前包
            if (!includeCurrentPackage) {
                val packageName = import.substringBeforeLast(".", "")
                packageName != currentPackage
            } else {
                true
            }
        }
        .sorted()
        .map { "import $it" }
}

/**
 * 获取仅包含外部依赖的导包语句
 * 排除 kotlin.* 和 java.* 包
 */
val KSDeclaration.externalImports: Set<String>
    get() = this.allImports.filter { import ->
        val packageName = import.substringBeforeLast(".", "")
        !packageName.startsWith("kotlin") &&
                !packageName.startsWith("java") &&
                packageName.isNotEmpty()
    }.toSet()

/**
 * 获取仅包含 Kotlin 标准库的导包语句
 */
val KSDeclaration.kotlinImports: Set<String>
    get() = this.allImports.filter { import ->
        val packageName = import.substringBeforeLast(".", "")
        packageName.startsWith("kotlin") && !isKotlinBuiltinPackage(packageName)
    }.toSet()

/**
 * 获取按包名分组的导包语句
 */
fun KSDeclaration.getImportsByPackage(): Map<String, List<String>> {
    return this.allImports.groupBy { import ->
        import.substringBeforeLast(".", "")
    }.mapValues { (_, imports) ->
        imports.sorted()
    }
}

/**
 * 生成完整的导包代码块
 * @param excludePackages 要排除的包名
 * @param groupByPackage 是否按包名分组
 */
fun KSDeclaration.generateImportBlock(
    excludePackages: Set<String> = emptySet(),
    groupByPackage: Boolean = false
): String {
    val imports = this.getFormattedImports(excludePackages)

    if (imports.isEmpty()) {
        return ""
    }

    return if (groupByPackage) {
        val grouped = this.getImportsByPackage()
            .filterKeys { it !in excludePackages }
            .toSortedMap()

        grouped.entries.joinToString("\n\n") { (packageName, imports) ->
            "// $packageName\n" + imports.joinToString("\n") { "import $it" }
        }
    } else {
        imports.joinToString("\n")
    }
}
