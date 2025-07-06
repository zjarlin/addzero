package com.addzero.web.jdbc.metadata


data class ColumnMetadata(
    val md5: String,
    val tableName: String,
    val columnName: String,
    val columnType: String,
    val columnLength: Int?,
    val nullableFlag: String
)
