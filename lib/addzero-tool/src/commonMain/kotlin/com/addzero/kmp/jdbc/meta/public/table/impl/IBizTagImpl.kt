package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.*
import com.addzero.kmp.jdbc.meta.public.table.IBizTag
             import kotlinx.serialization.Serializable

/**
 * IBizTagImpl 实现类
 * 实现 IBizTag 接口
 * 标签实体类，用于管理笔记的标签系统 该实体类映射到数据库表 `biz_tag`
 */
@Serializable
data class IBizTagImpl(
    override val id: Long,
        override val name: String?,
        override val description: String?
) : IBizTag
