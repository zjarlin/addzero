package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.Table

/**
 * 标签实体类，用于管理笔记的标签系统
 * 该实体类映射到数据库表 `biz_tag`
 */
@Entity
@Table(name = "biz_tag")
interface BizTag : BaseEntity {

    /**
     * 标签名称
     */
    @Key
    val name: String

    /**
     * 标签描述
     */
    val description: String?

}
