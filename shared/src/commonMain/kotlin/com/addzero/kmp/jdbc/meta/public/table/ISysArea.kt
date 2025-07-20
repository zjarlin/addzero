package com.addzero.kmp.jdbc.meta.public.table

/**
 * ISysArea
 * 区域表
 */
interface ISysArea {

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
     * 上级
     *
     * 数据库列名: parent_id
     * 数据类型: int8
     *
     * 可空: 是
     *
     */
    val parentId: Long?

    /**
     * 1省,2市,3区
     *
     * 数据库列名: node_type
     * 数据类型: text
     *
     * 可空: 是
     * 默认值: NULL::character varying
     */
    val nodeType: String?

    /**
     * name
     *
     * 数据库列名: name
     * 数据类型: text
     *
     * 可空: 是
     * 默认值: NULL::character varying
     */
    val name: String?

    /**
     * 区域编码
     *
     * 数据库列名: area_code
     * 数据类型: text
     *
     * 可空: 是
     * 默认值: NULL::character varying
     */
    val areaCode: String?

}
