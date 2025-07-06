package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.IBizMapping
             import kotlinx.serialization.Serializable

/**
 * IBizMappingImpl 实现类
 * 实现 IBizMapping 接口
 * 多对多关系表
 */
@Serializable
data class IBizMappingImpl(
    override val fromId: Long,
        override val toId: Long,
        override val mappingType: String
) : IBizMapping
