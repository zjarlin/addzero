package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.*
import com.addzero.kmp.jdbc.meta.public.table.ISysUser
             import kotlinx.serialization.Serializable

/**
 * ISysUserImpl 实现类
 * 实现 ISysUser 接口
 * 系统用户表
 */
@Serializable
data class ISysUserImpl(
    override val id: Long,
        override val phone: String?,
        override val password: String,
        override val avatar: String?,
        override val nickname: String?,
        override val gender: String?,
        override val username: String?,
        override val email: String?
) : ISysUser
