package com.addzero.kmp.jdbc.meta.public.table

/**
 * ISysDept
 * 部门表
 */
interface ISysDept {

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
     * 部门名称
     *
     * 数据库列名: name
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val name: String?

    /**
     * parent_id
     *
     * 数据库列名: parent_id
     * 数据类型: int8
     *
     * 可空: 是
     *
     */
    val parentId: Long?

}
