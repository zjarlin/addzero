package com.addzero.web.jdbc.metadata

data class ForeignKeyMetadata(
    val pkTableName: String,    // 主键表名
    val pkColumnName: String,   // 主键列名
    val fkTableName: String,    // 外键表名
    val fkColumnName: String,   // 外键列名
    val keySeq: Short,         // 外键序号
    val fkName: String?,       // 外键约束名称
    val pkName: String?        // 主键约束名称
)
