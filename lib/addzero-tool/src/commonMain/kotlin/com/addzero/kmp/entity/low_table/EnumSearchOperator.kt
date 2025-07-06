package com.addzero.kmp.entity.low_table

/**
 * 搜索操作类型
 */
enum class EnumSearchOperator(val displayName: String) {
    EQ("精确匹配"),         // =
    NE("不等于"),       // !=
    LIKE("包含"),          // LIKE %columnValue%
    STARTS_WITH("开头是"),      // LIKE columnValue%
    ENDS_WITH("结尾是"),        // LIKE %columnValue
    GT("大于"),      // >
    GE("大于等于"), // >=
    LT("小于"),         // <
    LE("小于等于"),    // <=
    BETWEEN("范围"),           // BETWEEN
    NOT_BETWEEN("不在范围"),
    IN("在列表中"),            // IN
    NOT_IN("不在列表中"),       // NOT IN
    IS_NULL("为空"),           // IS NULL
    IS_NOT_NULL("不为空")       // IS NOT NULL
    ,
}
