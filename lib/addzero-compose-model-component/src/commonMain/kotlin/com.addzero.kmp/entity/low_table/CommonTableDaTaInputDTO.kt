package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable

@Serializable
data class CommonTableDaTaInputDTO(
    val pageNo: Int = 1,
    val pageSize: Int = 10,
    val tableName: String,
    //关键词
    val keyword: String,
    //排序条件
//       @Contextual
    val stateSorts: MutableSet<StateSort> = mutableSetOf(),
    //查询条件
    val stateSearchForms: MutableSet<StateSearchForm> = mutableSetOf<StateSearchForm>(),
)
