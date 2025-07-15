package com.addzero.web.jdbc.metadata

data class PrimaryKeyMetadata(
    val tableName: String,      // 表名
    val columnName: String,     // 主键列名
    val keySeq: Short,         // 主键序号
    val pkName: String?        // 主键约束名称
)
