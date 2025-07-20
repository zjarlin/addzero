package com.addzero.kmp.jdbc.meta.public.table

/**
 * ISysDictItem
 * 字典项
 */
interface ISysDictItem {

    /**
     * id
     *
     * 数据库列名: id
     * 数据类型: int8
     * 主键: 是
     * 可空: 否
     *
     */
    val id: Long

    /**
     * 字典id
     *
     * 数据库列名: dict_id
     * 数据类型: int8
     *
     * 可空: 是
     *
     */
    val dictId: Long?

    /**
     * 字典项文本
     *
     * 数据库列名: item_text
     * 数据类型: text
     *
     * 可空: 否
     *
     */
    val itemText: String

    /**
     * 字典项值
     *
     * 数据库列名: item_value
     * 数据类型: text
     *
     * 可空: 否
     *
     */
    val itemValue: String

    /**
     * 描述
     *
     * 数据库列名: description
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val description: String?

    /**
     * 排序
     *
     * 数据库列名: sort_order
     * 数据类型: int4
     *
     * 可空: 是
     *
     */
    val sortOrder: Long?

    /**
     * 状态（1启用 0不启用）
     *
     * 数据库列名: status
     * 数据类型: int4
     *
     * 可空: 是
     *
     */
    val status: Long?

}
