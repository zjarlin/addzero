package com.addzero.kmp.annotation

/**
 * 路由注解
 * 用于生成路由表
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.SOURCE)
annotation class Route(
    val value: String = "",     // 注意是分组名称
    val title: String = "",      // 路由标题
    val routePath: String = "",  // 为空时使用全限定名
    val icon: String = "Apps",       // 图标
    val order: Double = 0.0,    // 排序(支持小数,方便插入新项)
    val qualifiedName: String = "",
    val simpleName: String = ""
)
