package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.ISysDept
             import kotlinx.serialization.Serializable

/**
 * ISysDeptImpl 实现类
 * 实现 ISysDept 接口
 * 部门表
 */
@Serializable
data class ISysDeptImpl(
    override val id: Long,
        override val name: String?,
        override val parentId: Long?
) : ISysDept
