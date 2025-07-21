package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

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
    val tableName: String?

    /**
     * schemaName
     */
    @Column(name = "schema_name")
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
    val remarks: String?
}
