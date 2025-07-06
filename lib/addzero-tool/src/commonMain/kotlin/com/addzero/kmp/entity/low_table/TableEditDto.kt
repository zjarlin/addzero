package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class TableSaveOrUpdateDTO(
    val tableName: String,
    val mutableMap: MutableMap<String, JsonElement>,
)
