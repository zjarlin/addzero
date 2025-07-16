package com.addzero.model.entity

import com.addzero.model.common.SnowflakeIdGenerator
import org.babyfish.jimmer.sql.*

/**
 * 多对多关系表
 *
 * 对应数据库表: biz_mapping
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "biz_mapping")
interface BizMapping {
    @Id
    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
    val id: Long


    /**
     * fromId
     */
    @Column(name = "from_id")
    val fromId: Long

    /**
     * toId
     */
    @Column(name = "to_id")
    val toId: Long

    /**
     * mappingType
     */
    @Column(name = "mapping_type")
    val mappingType: String
}