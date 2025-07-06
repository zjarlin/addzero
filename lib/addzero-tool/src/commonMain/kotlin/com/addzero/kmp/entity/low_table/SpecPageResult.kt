package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable

@Serializable
data class SpecPageResult<T>(
    val rows: List<T> = emptyList(),
    val totalRowCount: Long = 0L,
    val totalPageCount: Long = 0L,
) {
    companion object {
        fun <T> empty(pageSize: Int = 20) =
            SpecPageResult<T>(
                rows = emptyList(),
                totalRowCount = 0,
                totalPageCount = 0
            )
    }
}
