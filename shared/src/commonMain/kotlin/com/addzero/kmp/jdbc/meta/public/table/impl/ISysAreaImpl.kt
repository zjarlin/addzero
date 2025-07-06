package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.ISysArea
             import kotlinx.serialization.Serializable

/**
 * ISysAreaImpl 实现类
 * 实现 ISysArea 接口
 * 区域表
 */
@Serializable
data class ISysAreaImpl(
    override val id: Long,
        override val parentId: Long?,
        override val nodeType: String?,
        override val name: String?,
        override val areaCode: String?
) : ISysArea
