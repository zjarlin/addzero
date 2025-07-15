package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

/**
 * 菜单表
 *
 * 对应数据库表: sys_menu
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "sys_menu")
interface SysMenu : BaseEntity {

    /**
     * 父节点ID
     */
    @Column(name = "parent_id")
    val parentId: Long?

    /**
     * 路由标题
     */
    @Column(name = "title")
    val title: String?

    /**
     * 路由地址
     */
    @Column(name = "route_path")
    val routePath: String?

    /**
     * 图标
     */
    @Column(name = "icon")
    val icon: String?

    /**
     * 排序
     */
    @Column(name = "order")
    val order: Float?
}