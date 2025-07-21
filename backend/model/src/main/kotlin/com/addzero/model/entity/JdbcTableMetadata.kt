package com.addzero.model.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 * JdbcTableMetadata
 *
 * 对应数据库表: jdbc_table_metadata
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "jdbc_table_metadata")
interface JdbcTableMetadata : BaseEntity {

    /**
     * tableName
     */
    @Column(name = "table_name")
    @Key
    @LabelProp
    val tableName: String?

    /**
     * schemaName
     */
    @Column(name = "schema_name")
    @Key
    val schemaName: String?

    /**
     * tableType
     */
    @Column(name = "table_type")
    val tableType: String?

    /**
     * remarks
     */
    @Column(name = "remarks")
    @LabelProp
    val remarks: String?

    @OneToMany(mappedBy = "table")
    val columns: List<JdbcColumnMetadata>
}
