package com.addzero.kmp.jdbc.meta.public.table

import com.addzero.kmp.jdbc.meta.*
/**
 * ISysMenu
 * 菜单表
 */
interface ISysMenu {

    /**
     * 主键
     * 
     * 数据库列名: id
     * 数据类型: int8
     * 主键: 是
     * 可空: 否
     * 
     */
     val id: Long

    /**
     * 父节点ID
     * 
     * 数据库列名: parent_id
     * 数据类型: int8
     * 
     * 可空: 是
     * 
     */
     val parentId: Long?

    /**
     * 路由标题
     * 
     * 数据库列名: title
     * 数据类型: text
     * 
     * 可空: 是
     * 默认值: NULL::character varying
     */
     val title: String?

    /**
     * 路由地址
     * 
     * 数据库列名: route_path
     * 数据类型: text
     * 
     * 可空: 是
     * 默认值: NULL::character varying
     */
     val routePath: String?

    /**
     * 图标
     * 
     * 数据库列名: icon
     * 数据类型: text
     * 
     * 可空: 是
     * 默认值: NULL::character varying
     */
     val icon: String?

    /**
     * 排序
     * 
     * 数据库列名: order
     * 数据类型: float8
     * 
     * 可空: 是
     * 
     */
     val order: Float?

}
