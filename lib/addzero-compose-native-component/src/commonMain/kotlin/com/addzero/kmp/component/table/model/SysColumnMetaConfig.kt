package com.addzero.kmp.component.table.model

import com.addzero.kmp.entity.low_table.EnumColumnAlignment

data class SysColumnMetaConfig(
    //列对齐方式
    val alignment: EnumColumnAlignment = EnumColumnAlignment.CENTER,
    //单元格宽度比
    val widthRatio: Float = 1f,

    /**
     *"Long", "Integer", "Int", "Short"
     * "Float", "Double", "BigDecimal"
     * LocalDate,LocalTime ,LocalDateTime
     *
     * Boolean
     */
    val kmpType: String = "String",
    //显示在列表
    val showInList: Boolean = true,
    //可搜索
    val searchable: Boolean = true,
    //可排序
    val sortable: Boolean = true,

    )
