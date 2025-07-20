package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.ISysDeptSysUserMapping
import kotlinx.serialization.Serializable

/**
 * ISysDeptSysUserMappingImpl 实现类
 * 实现 ISysDeptSysUserMapping 接口
 */
@Serializable
data class ISysDeptSysUserMappingImpl(
    override val id: Long,
    override val sysDeptId: Long?,
    override val sysUserId: Long?
) : ISysDeptSysUserMapping
