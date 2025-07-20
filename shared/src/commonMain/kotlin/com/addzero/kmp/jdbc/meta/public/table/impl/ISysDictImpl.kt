package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.ISysDict
import kotlinx.serialization.Serializable

/**
 * ISysDictImpl 实现类
 * 实现 ISysDict 接口
 * 字典表
 */
@Serializable
data class ISysDictImpl(
    override val id: Long,
    override val dictName: String,
    override val dictCode: String,
    override val description: String?
) : ISysDict
