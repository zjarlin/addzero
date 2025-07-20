package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.IBizNote
import kotlinx.serialization.Serializable

/**
 * IBizNoteImpl 实现类
 * 实现 IBizNote 接口
 * 笔记实体类
 */
@Serializable
data class IBizNoteImpl(
    override val id: Long,
    override val title: String?,
    override val content: String?,
    override val type: String?,
    override val parentId: Long?
) : IBizNote
