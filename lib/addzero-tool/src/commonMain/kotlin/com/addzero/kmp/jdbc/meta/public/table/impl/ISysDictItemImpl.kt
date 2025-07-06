package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.*
import com.addzero.kmp.jdbc.meta.public.table.ISysDictItem
             import kotlinx.serialization.Serializable

/**
 * ISysDictItemImpl 实现类
 * 实现 ISysDictItem 接口
 * 字典项
 */
@Serializable
data class ISysDictItemImpl(
    override val id: Long,
        override val dictId: Long?,
        override val itemText: String,
        override val itemValue: String,
        override val description: String?,
        override val sortOrder: Long?,
        override val status: Long?
) : ISysDictItem
