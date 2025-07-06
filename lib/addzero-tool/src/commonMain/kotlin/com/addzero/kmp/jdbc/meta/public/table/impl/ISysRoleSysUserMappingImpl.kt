package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.*
import com.addzero.kmp.jdbc.meta.public.table.ISysRoleSysUserMapping
             import kotlinx.serialization.Serializable

/**
 * ISysRoleSysUserMappingImpl 实现类
 * 实现 ISysRoleSysUserMapping 接口
 */
@Serializable
data class ISysRoleSysUserMappingImpl(
    override val id: Long,
        override val sysRoleId: Long?,
        override val sysUserId: Long?
) : ISysRoleSysUserMapping
