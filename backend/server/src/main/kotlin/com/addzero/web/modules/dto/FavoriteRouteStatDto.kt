package com.addzero.web.modules.dto

/**
 * 常用路由统计DTO
 */
data class FavoriteRouteStatDto(
    /**
     * 路由键
     */
    val routeKey: String,

    /**
     * 使用次数
     */
    val usageCount: Long,

    /**
     * 路由标题（从菜单表获取）
     */
    val title: String? = null,

    /**
     * 图标
     */
    val icon: String? = null
)
