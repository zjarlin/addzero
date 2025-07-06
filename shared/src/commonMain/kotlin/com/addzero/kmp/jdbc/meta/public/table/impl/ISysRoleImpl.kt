package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.ISysRole
             import kotlinx.serialization.Serializable

/**
 * ISysRoleImpl 实现类
 * 实现 ISysRole 接口
 * 系统角色实体对应数据库表
 */
@Serializable
data class ISysRoleImpl(
    override val id: Long,
        override val roleCode: String?,
        override val roleName: String?,
        override val systemFlag: Boolean?,
        override val status: String?
) : ISysRole
