package com.addzero.kmp.entity.low_table


/**
 * 字段类型映射工具
 * 根据基础数据类型推导可用的渲染类型
 */
object ConfigFieldTypeMapper {

    /**
     * 获取字段类型对应的所有可用渲染类型
     */
    fun getAvailableRenderTypes(baseType: EnumBaseFieldType): List<EnumFieldRenderType> {
        return when (baseType) {
            EnumBaseFieldType.STRING -> listOf(
                EnumFieldRenderType.TEXT,
                EnumFieldRenderType.PASSWORD,
                EnumFieldRenderType.EMAIL,
                EnumFieldRenderType.LONG_TEXT,
                EnumFieldRenderType.AUTO_COMPLETE,
                EnumFieldRenderType.RICH_TEXT,
                EnumFieldRenderType.PHONE,
                EnumFieldRenderType.URL,
                EnumFieldRenderType.TAG
            )

            EnumBaseFieldType.INTEGER, EnumBaseFieldType.FLOAT, EnumBaseFieldType.DOUBLE -> listOf(
                EnumFieldRenderType.NUMBER,
                EnumFieldRenderType.CURRENCY,
                EnumFieldRenderType.PERCENTAGE,
                EnumFieldRenderType.PROGRESS,
                EnumFieldRenderType.RATING,
                EnumFieldRenderType.SLIDER
            )

            EnumBaseFieldType.BOOLEAN -> listOf(
                EnumFieldRenderType.CHECKBOX,
                EnumFieldRenderType.SWITCH,
                EnumFieldRenderType.RADIO
            )

            EnumBaseFieldType.DATE -> listOf(
                EnumFieldRenderType.DATE
            )

            EnumBaseFieldType.DATETIME -> listOf(
                EnumFieldRenderType.DATETIME
            )

            EnumBaseFieldType.TIME -> listOf(
                EnumFieldRenderType.TIME
            )

            EnumBaseFieldType.ENUM -> listOf(
                EnumFieldRenderType.SELECT,
                EnumFieldRenderType.RADIO
            )

            EnumBaseFieldType.ARRAY -> listOf(
                EnumFieldRenderType.MULTI_SELECT,
                EnumFieldRenderType.CHECKBOX,
                EnumFieldRenderType.TAG
            )

            EnumBaseFieldType.OBJECT -> listOf(
                EnumFieldRenderType.CUSTOM
            )

            else -> listOf(EnumFieldRenderType.TEXT)
        }
    }

    /**
     * 根据字段名称推测可能的字段类型
     */
    fun guessTypeByFieldName(fieldName: String): List<EnumFieldRenderType> {
        val lowerName = fieldName.lowercase()
        return when {
            // 邮箱字段
            lowerName.contains("email") || lowerName.contains("mail") -> listOf(EnumFieldRenderType.EMAIL)

            // 密码字段
            lowerName.contains("password") || lowerName.contains("pwd") -> listOf(EnumFieldRenderType.PASSWORD)

            // 电话号码
            lowerName.contains("phone") || lowerName.contains("mobile") || lowerName.contains("tel") -> listOf(
                EnumFieldRenderType.PHONE
            )

            // 网址
            lowerName.contains("url") || lowerName.contains("website") || lowerName.contains("link") -> listOf(
                EnumFieldRenderType.URL
            )

            // 颜色
            lowerName.contains("color") -> listOf(EnumFieldRenderType.COLOR)

            // 图片
            lowerName.contains("image") || lowerName.contains("photo") || lowerName.contains("picture") || lowerName.contains(
                "avatar"
            ) -> listOf(EnumFieldRenderType.IMAGE)

            // 文件
            lowerName.contains("file") || lowerName.contains("attachment") -> listOf(EnumFieldRenderType.FILE)

            // 日期
            lowerName.contains("date") -> listOf(EnumFieldRenderType.DATE)

            // 时间
            lowerName.contains("time") && !lowerName.contains("date") -> listOf(EnumFieldRenderType.TIME)

            // 日期时间
            lowerName.contains("datetime") || (lowerName.contains("date") && lowerName.contains("time")) -> listOf(
                EnumFieldRenderType.DATETIME
            )

            // 状态、开关
            lowerName.contains("status") || lowerName.contains("state") || lowerName.contains("enabled") || lowerName.contains(
                "active"
            ) -> listOf(EnumFieldRenderType.SWITCH)

            // 价格、金额
            lowerName.contains("price") || lowerName.contains("cost") || lowerName.contains("amount") || lowerName.contains(
                "money"
            ) -> listOf(EnumFieldRenderType.CURRENCY)

            // 百分比
            lowerName.contains("percent") || lowerName.contains("rate") || lowerName.contains("ratio") -> listOf(
                EnumFieldRenderType.PERCENTAGE
            )

            // 默认
            else -> emptyList()
        }
    }

    /**
     * 将Java/Kotlin类型转换为基础字段类型
     */
    fun convertJvmTypeToBaseType(typeName: String): EnumBaseFieldType {
        return when {
            typeName.contains("String") -> EnumBaseFieldType.STRING
            typeName.contains("Int") || typeName.contains("Long") -> EnumBaseFieldType.INTEGER
            typeName.contains("Float") -> EnumBaseFieldType.FLOAT
            typeName.contains("Double") -> EnumBaseFieldType.DOUBLE
            typeName.contains("Boolean") -> EnumBaseFieldType.BOOLEAN
            typeName.contains("Date") -> EnumBaseFieldType.DATE
            typeName.contains("LocalDate") -> EnumBaseFieldType.DATE
            typeName.contains("LocalDateTime") || typeName.contains("ZonedDateTime") -> EnumBaseFieldType.DATETIME
            typeName.contains("LocalTime") -> EnumBaseFieldType.TIME
            typeName.contains("Enum") -> EnumBaseFieldType.ENUM
            typeName.contains("List") || typeName.contains("Set") || typeName.contains("Array") -> EnumBaseFieldType.ARRAY
            typeName.contains("Map") || typeName.contains("Object") -> EnumBaseFieldType.OBJECT
            else -> EnumBaseFieldType.UNKNOWN
        }
    }

    /**
     * 根据数据库类型获取基础字段类型
     */
    fun convertDbTypeToBaseType(dbType: String): EnumBaseFieldType {
        return when (dbType.uppercase()) {
            "VARCHAR", "CHAR", "TEXT", "CLOB", "LONGTEXT" -> EnumBaseFieldType.STRING
            "INT", "INTEGER", "SMALLINT", "BIGINT", "TINYINT" -> EnumBaseFieldType.INTEGER
            "FLOAT", "REAL" -> EnumBaseFieldType.FLOAT
            "DOUBLE", "DECIMAL", "NUMERIC" -> EnumBaseFieldType.DOUBLE
            "BOOLEAN", "BIT", "BOOL" -> EnumBaseFieldType.BOOLEAN
            "DATE" -> EnumBaseFieldType.DATE
            "DATETIME", "TIMESTAMP" -> EnumBaseFieldType.DATETIME
            "TIME" -> EnumBaseFieldType.TIME
            "ENUM" -> EnumBaseFieldType.ENUM
            "ARRAY", "JSON_ARRAY" -> EnumBaseFieldType.ARRAY
            "JSON", "JSONB", "OBJECT" -> EnumBaseFieldType.OBJECT
            else -> EnumBaseFieldType.UNKNOWN
        }
    }

    /**
     * 获取最合适的默认渲染类型
     */
    fun getDefaultRenderType(baseType: EnumBaseFieldType, fieldName: String): EnumFieldRenderType {
        // 先尝试从字段名猜测
        val guessedTypes = guessTypeByFieldName(fieldName)
        if (guessedTypes.isNotEmpty()) {
            return guessedTypes.first()
        }

        // 如果无法从字段名猜测,则使用默认值
        return when (baseType) {
            EnumBaseFieldType.STRING -> EnumFieldRenderType.TEXT
            EnumBaseFieldType.INTEGER, EnumBaseFieldType.FLOAT, EnumBaseFieldType.DOUBLE -> EnumFieldRenderType.NUMBER
            EnumBaseFieldType.BOOLEAN -> EnumFieldRenderType.SWITCH
            EnumBaseFieldType.DATE -> EnumFieldRenderType.DATE
            EnumBaseFieldType.DATETIME -> EnumFieldRenderType.DATETIME
            EnumBaseFieldType.TIME -> EnumFieldRenderType.TIME
            EnumBaseFieldType.ENUM -> EnumFieldRenderType.SELECT
            EnumBaseFieldType.ARRAY -> EnumFieldRenderType.MULTI_SELECT
            EnumBaseFieldType.OBJECT -> EnumFieldRenderType.CUSTOM
            else -> EnumFieldRenderType.TEXT
        }
    }
}
