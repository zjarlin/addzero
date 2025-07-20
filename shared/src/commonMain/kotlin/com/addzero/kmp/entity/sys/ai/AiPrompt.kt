package com.addzero.kmp.entity.sys.ai

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

/**
 * 🤖 AI 提示词数据类
 *
 * @property id 提示词ID
 * @property title 提示词标题
 * @property content 提示词内容
 * @property category 分类
 * @property tags 标签列表
 * @property isBuiltIn 是否为内置提示词
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 */
@Serializable
@Deprecated("dajsoidjaso")
data class AiPrompt(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val isBuiltIn: Boolean = false,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

