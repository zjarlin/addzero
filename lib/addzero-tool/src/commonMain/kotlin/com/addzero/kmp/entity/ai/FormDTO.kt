package com.addzero.kmp.entity.ai

// FormDTO类，用于在数据传输过程中承载表单相关的信息
data class FormDTO(
    /** 表中文名 */
    val tableName: String? = null,
    /** 表英文名称tableEnglistName业务系统要求biz_开头,如果是系统架构表需要sys_开头 */
    val tableEnglishName: String = "",
    /** dbType的候选项为(全部小写字母) mysql oracle pg dm */
    val dbType: String = "",
    /** 数据库名称,达梦dm数据库类型的dbname不可以为空,其余都可以为空 */
    val dbName: String? = null,
    /** 字段列表 */
    var fields: List<FieldDTO>? = null,
)

// FieldDTO类，用于在数据传输过程中承载表单字段相关的信息
data class FieldDTO(
    /** javaType候选项为(区分大小写字母) Integer long String boolean Date LocalTime LocalDateTime BigDecimal double */
    var javaType: String = "",
    /** 字段名称 */
    var fieldName: String? = null,
    /** 字段中文名 */
    var fieldChineseName: String = "",
)
