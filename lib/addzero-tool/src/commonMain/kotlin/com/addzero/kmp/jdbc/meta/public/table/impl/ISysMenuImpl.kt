package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.*
import com.addzero.kmp.jdbc.meta.public.table.ISysMenu
             import kotlinx.serialization.Serializable

/**
 * ISysMenuImpl 实现类
 * 实现 ISysMenu 接口
 * 菜单表
 */
@Serializable
data class ISysMenuImpl(
    override val id: Long,
        override val parentId: Long?,
        override val title: String?,
        override val routePath: String?,
        override val icon: String?,
        override val order: Float?
) : ISysMenu
