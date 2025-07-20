package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

/**
 * SysFavoriteTab
 *
 * 对应数据库表: sys_favorite_tab
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "sys_favorite_tab")
interface SysFavoriteTab : BaseEntity {

    /**
     * routeKey
     */
    @Column(name = "route_key")
    val routeKey: String

}