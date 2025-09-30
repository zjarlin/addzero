package site.addzero.entity

import kotlinx.serialization.Serializable

@Serializable
data class PageResult<T>(
    val rows: List<T> = emptyList(),

    val totalRowCount: Long = 0L,

    val totalPageCount: Int = 0,

    val pageIndex: Int = 1,

    val pageSize: Int = 10,

    val isFirst: Boolean = true,

    val isLast: Boolean = true
) {
    companion object {
        fun <T> empty(pageSize: Int = 20) =

            PageResult<T>(
                rows = emptyList(),
                totalRowCount = 0,
                totalPageCount = 0,
                pageIndex = 0,
                pageSize = pageSize,
                isFirst = true,
                isLast = true
            )
    }
}
