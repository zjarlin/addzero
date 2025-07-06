package com.addzero.kmp.entity

data class PageResult<T>(
    val content: List<T> = emptyList(),

    val totalElements: Long = 0L,

    val totalPages: Int = 0,

    val pageIndex: Int = 1,

    val pageSize: Int = 10,

    val isFirst: Boolean = true,

    val isLast: Boolean = true
) {
    companion object {
      fun <T> empty(pageSize: Int = 20) =

      PageResult<T>(
            content = emptyList(),
            totalElements = 0,
            totalPages = 0,
            pageIndex = 0,
            pageSize = pageSize,
            isFirst = true,
            isLast = true
        )
    }
}
