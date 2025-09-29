package site.addzero.model.entity

import site.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 * SysColumnConfig
 *
 * 对应数据库表: sys_column_config
 */
@Entity
@Table(name = "sys_column_config")
interface SysColumnConfig : BaseEntity {

    /**
     * 列唯一键
     */
    @Column(name = "column_key")
    val columnKey: String

    /**
     * 列描述
     */
    @Column(name = "column_comment")
    val columnComment: String

    /**
     * kmp类型
     */
    @Column(name = "kmp_type")
    val kmpType: String

    /**
     * 列排序
     */
    @Column(name = "sort_order")
    val sortOrder: Long

    /**
     * 对该列启用过滤
     */
    @Column(name = "show_filter")
    val showFilter: Boolean

    /**
     * 对该列启用排序
     */
    @Column(name = "show_sort")
    val showSort: Boolean

    /**
     * 路由键
     */
    @Column(name = "route_key")
    val routeKey: String


    @ManyToOne
       @OnDissociate(DissociateAction.DELETE)
    @JoinColumn(name = "table_id")
    val tableConfig: SysTableConfig?
}
