package com.addzero.kmp.util

import com.addzero.kmp.entity.JdbcColumnMetadata


/**
 * 类型映射工具类
 */
object TypeMapper {

    fun defaultValueForType(type: String): String {
        return when (type) {
            "String" -> "\"\""
            "Int" -> "0"
            "Long" -> "0L"
            "Boolean" -> "false"
            "Double" -> "0.0"
            else -> "null"
        }
    }

    /**
     * 将数据库类型映射到Kotlin类型
     */
    fun mapToKotlinType(column: JdbcColumnMetadata): String {
        return when {
            column.columnType.contains("char", ignoreCase = true) -> "String"
            column.columnType.contains("varchar", ignoreCase = true) -> "String"
            column.columnType.contains("text", ignoreCase = true) -> "String"
            column.columnType.contains("bigint", ignoreCase = true) -> "Long"
            column.columnType.contains("int", ignoreCase = true) -> "Long"
            column.columnType.contains("int8", ignoreCase = true) -> "Long"
            column.columnType.contains("smallint", ignoreCase = true) -> "Short"
            column.columnType.contains("float", ignoreCase = true) -> "Float"
            column.columnType.contains("double", ignoreCase = true) -> "Double"
            column.columnType.contains("real", ignoreCase = true) -> "Float"
            column.columnType.contains("bool", ignoreCase = true) -> "Boolean"
            else -> column.columnType.toBigCamelCase()
        }
    }


    fun mapJdbcTypeToKotlinType4Form(column: JdbcColumnMetadata): String {
        // 可根据实际数据库类型扩展
        return when (column.columnType.lowercase()) {
            "varchar", "text", "char", "uuid" -> if (column.nullable) "String?" else "String"
            "int", "integer", "serial" -> if (column.nullable) "Int?" else "Int"
            "bigint" -> if (column.nullable) "Long?" else "Long"
            "bool", "boolean" -> if (column.nullable) "Boolean?" else "Boolean"
            "float", "double", "real", "numeric", "decimal" -> if (column.nullable) "Double?" else "Double"
            "date", "timestamp", "timestamptz" -> if (column.nullable) "String?" else "String"
            else -> if (column.nullable) "String?" else "String"
        }
    }


    //    /**
//     * 将字符串转换为驼峰命名
//     */
    fun String.toBigCamelCase(): String {
        return this.split("_").joinToString("") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }

    /**
     * 将列名转换为驼峰命名
     */
    fun String.toLowCamelCase(): String {
        return this.split("_").joinToString("") {
            it.replaceFirstChar { char -> char.uppercase() }
        }.replaceFirstChar { it.lowercase() }
    }
}
