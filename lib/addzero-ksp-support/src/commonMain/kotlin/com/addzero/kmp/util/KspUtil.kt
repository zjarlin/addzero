package com.addzero.kmp.util

import com.google.devtools.ksp.symbol.*
import java.io.File


fun KSType.getQualifiedTypeString(): String {
    val type = this

    // 基础类型名称
    val baseType = type.declaration.qualifiedName?.asString() ?: type.declaration.simpleName.asString()

    // 处理泛型参数
    val genericArgs = if (type.arguments.isNotEmpty()) {
        type.arguments.joinToString(", ") { arg ->
            arg.type?.resolve()?.let {
                it.declaration.qualifiedName?.asString() ?: it.declaration.simpleName.asString()
            } ?: "*" // 通配符表示未知类型
        }
    } else null

    // 处理可空性
    val nullableSuffix = if (type.nullability == Nullability.NULLABLE) "?" else ""

    return when {
        genericArgs != null -> "$baseType<$genericArgs>$nullableSuffix"
        else -> "$baseType$nullableSuffix"
    }
}




fun genCode(pathname: String, toJsonStr: String, skipExistFile: Boolean = false) {
//        val file = FileUtil.file(pathname)
//        FileUtil.writeUtf8String(toJsonStr, file)

    val targetFile = File(pathname)
    targetFile.parentFile?.mkdirs()
    if (skipExistFile) {
        if (targetFile.exists()) {
            return
        }

    }
    targetFile.writeText(toJsonStr)

}

// 扩展 KSPropertyDeclaration，判断属性是否为枚举类型
fun KSPropertyDeclaration.isEnumProperty(): Boolean {
    return this.type.resolve().declaration.let { decl ->
        (decl as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS
    }
}


/**
 * 获取属性的全限定类型字符串（包含泛型参数和可空性）
 */
fun KSPropertyDeclaration.getQualifiedTypeString(): String {
    val type = this.type.resolve()

    return buildString {
        // 基础类型名称
        append(type.declaration.qualifiedName?.asString() ?: type.declaration.simpleName.asString())

        // 处理泛型参数
        if (type.arguments.isNotEmpty()) {
            append("<")
            append(type.arguments.joinToString(", ") { arg ->
                arg.type?.resolve()?.let {
                    it.declaration.qualifiedName?.asString() ?: it.declaration.simpleName.asString()
                } ?: "*" // 通配符表示未知类型
            })
            append(">")
        }

        // 处理可空性
        if (type.nullability == Nullability.NULLABLE) {
            append("?")
        }
    }
}

/**
 * 获取属性的简化类型字符串（不包含包名，但保留泛型）
 */
fun KSPropertyDeclaration.getSimpleTypeString(): String {
    val type = this.type.resolve()
    return buildString {
        // 基础类型名称
        append(type.declaration.simpleName.asString())

        // 处理泛型参数
        if (type.arguments.isNotEmpty()) {
            append("<")
            append(type.arguments.joinToString(", ") { arg ->
                arg.type?.resolve()?.let {
                    it.declaration.simpleName.asString()
                } ?: "*"
            })
            append(">")
        }

        // 处理可空性
        if (type.nullability == Nullability.NULLABLE) {
            append("?")
        }
    }
}


fun KSPropertyDeclaration.defaultValue(): String {
    val type = this.type.resolve()
    val typeDecl = type.declaration
    val fullTypeName = type.declaration.qualifiedName?.asString() ?: ""
    val typeName = typeDecl.simpleName.asString()
    val isNullable = type.isMarkedNullable
    return when {
        this.isEnumProperty() -> {
            if (isNullable) "null" else "${fullTypeName}.entries.first()"
        }

        isNullable -> "null"
        typeName == "String" -> "\"\""
        typeName == "Int" -> "0"
        typeName == "Long" -> "0L"
        typeName == "Double" -> "0.0"
        typeName == "Float" -> "0f"
        typeName == "Boolean" -> "false"
        typeName == "List" -> "emptyList()"
        typeName == "Set" -> "emptySet()"
        typeName == "Map" -> "emptyMap()"
        typeName == "LocalDateTime" -> "kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())"
        typeName == "LocalDate" -> "kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date"
        typeName == "LocalTime" -> "kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).time"
        else -> ""
    }
}


fun KSPropertyDeclaration.getAnno(annoShortName: String): KSAnnotation? {
    return this.annotations.find { it.shortName.asString() == annoShortName }
}

fun KSAnnotation?.getArg(argName: String): Any? {
    val value = this?.arguments?.firstOrNull { it.name?.asString() == argName }?.value
    return value

}


fun KSPropertyDeclaration.isCustomClassType(): Boolean {
    val type = this.type.resolve()
    val declaration = type.declaration

    // 情况1：声明是类（且不是基本类型）
    if (declaration is KSClassDeclaration) {
        val qualifiedName = declaration.qualifiedName?.asString()

        // 排除Kotlin/Java的基本类型
        return qualifiedName !in setOf(
            "kotlin.String", "kotlin.Int", "kotlin.Long", "kotlin.Boolean", "kotlin.Float", "kotlin.Double", "kotlin.Byte", "kotlin.Short", "kotlin.Char", "java.lang.String", "java.lang.Integer" // 其他基本类型...
        )
    }

    // 情况2：其他情况（如泛型、类型参数等）默认视为非基本类型
    return true
}


fun KSPropertyDeclaration.ktType(): String {
    val ktType = this.type.resolve().declaration.simpleName.asString()
    return ktType
}


fun KSAnnotation?.getArgFirstValue(): String? {
    return this?.arguments?.firstOrNull()?.value?.toString()

}


fun KSPropertyDeclaration.isNullable(): Boolean {
    return this.type.resolve().isMarkedNullable
}


fun KSPropertyDeclaration.isNullableFlag(): String {
    val nullable = this.isNullable()
    return if (nullable) {
        "NULL"

    } else {
        "NOT NULL"

    }
}


fun KSPropertyDeclaration.ktName(): String {
    return this.simpleName.asString()
}

// 判断是否为Jimmer实体接口
fun isJimmerEntity(typeDecl: KSDeclaration): Boolean {
    return typeDecl.annotations.any { it.shortName.asString() == "Entity" } && (typeDecl as? KSClassDeclaration)?.classKind == ClassKind.INTERFACE
}

// 判断是否为枚举类
fun isEnum(typeDecl: KSDeclaration): Boolean {
    return (typeDecl as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS
}

fun KSPropertyDeclaration.isCollectionType(): Boolean {
    val type = this.type.resolve()
    val declaration = type.declaration

    // 获取类型的全限定名（如 "kotlin.collections.List"）
    val typeName = declaration.qualifiedName?.asString() ?: return false

    // 检查是否是常见集合类型
    return typeName in setOf(
        "kotlin.collections.List", "kotlin.collections.MutableList", "kotlin.collections.Set", "kotlin.collections.MutableSet", "kotlin.collections.Map", "kotlin.collections.MutableMap", "java.util.List", "java.util.ArrayList", "java.util.Set", "java.util.HashSet", "java.util.Map", "java.util.HashMap"
    )
}


/**
 * 猜测Jimmer实体的表名
 * 1. 优先读取@Table注解的name属性
 * 2. 没有则尝试从KDoc注释中提取@table标签
 * 3. 没有则用类名转下划线
 */
fun guessTableName(ktClass: KSClassDeclaration): String {
    // 1. 优先读取@Table注解
    val tableAnn = ktClass.annotations.firstOrNull {
        it.shortName.asString() == "Table" || it.annotationType.resolve().declaration.qualifiedName?.asString() == "org.babyfish.jimmer.sql.Table"
    }
    val tableNameFromAnn = tableAnn?.arguments?.firstOrNull { it.name?.asString() == "name" }?.value as? String
    if (!tableNameFromAnn.isNullOrBlank()) {
        return tableNameFromAnn
    }

    // 2. 尝试从KDoc注释中提取@table标签
    val doc = ktClass.docString
    if (!doc.isNullOrBlank()) {
        // 支持 @table 表名 或 @table:表名
        val regex = Regex("@table[:：]?\\s*([\\w_]+)")
        val match = regex.find(doc)
        if (match != null) {
            return match.groupValues[1]
        }
    }

    // 3. 默认用类名转下划线
    val asString = ktClass.simpleName.asString().toUnderLineCase()
    return asString
}


fun KSPropertyDeclaration.hasAnno(string: String): Boolean {
    return this.getAnno(string) != null
}
