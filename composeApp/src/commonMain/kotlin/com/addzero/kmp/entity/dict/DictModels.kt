package com.addzero.kmp.entity.dict

import kotlinx.serialization.Serializable

/**
 * 字典类型查询参数
 */
data class DictTypeQuery(
    val name: String? = null,
    val code: String? = null,
    val status: Int? = null,
    val pageNum: Int = 1,
    val pageSize: Int = 10
)

/**
 * 字典项查询参数
 */
data class DictItemQuery(
    val dictId: Long? = null,
    val itemText: String? = null,
    val status: Long? = null,
    val pageNum: Int = 1,
    val pageSize: Int = 10
)

/**
 * 分页结果
 */
@Serializable
data class PageResult<T>(
    val total: Long = 0,
    val items: List<T> = emptyList(),
    val pageNum: Int = 1,
    val pageSize: Int = 10,
    val pages: Int = 0
) 