package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * 搜索条件类
 */
@Serializable
data class StateSearchForm(
    val columnKey: String,
    val operator: EnumSearchOperator = EnumSearchOperator.EQ,
    @Contextual
    val columnValue: Any? = null,
    val logicType: EnumLogicOperator = EnumLogicOperator.AND
) {


}
