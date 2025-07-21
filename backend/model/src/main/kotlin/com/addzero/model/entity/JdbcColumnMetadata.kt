package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

/**
 * JdbcColumnMetadata
 *
 * 对应数据库表: jdbc_column_metadata
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "jdbc_column_metadata")
interface JdbcColumnMetadata : BaseEntity {

    /**
     * tableName
     */
    @Column(name = "table_name")
    val tableName: String?

    /**
     * columnName
     */
    @Column(name = "column_name")
    val columnName: String?

    /**
     * jdbcType
     */
    @Column(name = "jdbc_type")
    val jdbcType: Long?

    /**
     * columnType
     */
    @Column(name = "column_type")
    val columnType: String?

    /**
     * columnLength
     */
    @Column(name = "column_length")
    val columnLength: Long?

    /**
     * nullable
     */
    @Column(name = "nullable_boolean")
    val nullableBoolean: Boolean?

    /**
     * nullableFlag
     */
    @Column(name = "nullable_flag")
    val nullableFlag: String?

    /**
     * remarks
     */
    @Column(name = "remarks")
    val remarks: String?

    /**
     * defaultValue
     */
    @Column(name = "default_value")
    val defaultValue: String?

    /**
     * primaryKey
     */
    @Column(name = "primary_key_flag")
    val primaryKeyFlag: String?

    /**
     * tableId
     */
    @Column(name = "table_id")
    val tableId: Long?
}
