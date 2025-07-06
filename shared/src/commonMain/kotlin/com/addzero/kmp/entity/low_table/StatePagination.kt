package com.addzero.kmp.entity.low_table






/**
 * 分页状态
 */
data class StatePagination(
    var currentPage: Int = 1,
    var pageSize: Int = 10,
    var totalItems: Int = 0
) {
    val totalPages: Int get() = if (totalItems == 0) 1 else (totalItems + pageSize - 1) / pageSize
    val hasPreviousPage: Boolean get() = currentPage > 1
    val hasNextPage: Boolean get() = currentPage < totalPages
    val startItem: Int get() = (currentPage - 1) * pageSize + 1
    val endItem: Int get() = minOf(currentPage * pageSize, totalItems)


}


