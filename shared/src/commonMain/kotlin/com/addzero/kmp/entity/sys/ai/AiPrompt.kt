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

/**
 * 🎯 AI 提示词保存请求数据类
 * 
 * @property title 提示词标题
 * @property content 提示词内容
 * @property category 分类
 * @property tags 标签列表
 */
@Serializable
data class SavePromptRequest(
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String> = emptyList()
)

/**
 * 🔍 AI 提示词搜索请求数据类
 * 
 * @property keyword 搜索关键词
 * @property category 分类筛选（可选）
 * @property tags 标签筛选（可选）
 */
@Serializable
data class SearchPromptRequest(
    val keyword: String? = null,
    val category: String? = null,
    val tags: List<String> = emptyList()
)

/**
 * 💬 使用提示词聊天请求数据类
 * 
 * @property promptId 提示词ID
 * @property variables 变量替换映射
 */
@Serializable
data class ChatWithPromptRequest(
    val promptId: String,
    val variables: Map<String, String> = emptyMap()
)

/**
 * 📊 提示词统计数据类
 * 
 * @property totalCount 总数量
 * @property builtInCount 内置数量
 * @property customCount 自定义数量
 * @property categoryCount 分类统计
 */
@Serializable
data class PromptStatistics(
    val totalCount: Int,
    val builtInCount: Int,
    val customCount: Int,
    val categoryCount: Map<String, Int>
)
