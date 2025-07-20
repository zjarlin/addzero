package com.addzero.kmp.jdbc.meta.public.table

/**
 * IBizNote
 * 笔记实体类
 */
interface IBizNote {

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
     * 标题
     *
     * 数据库列名: title
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val title: String?

    /**
     * 内容
     *
     * 数据库列名: content
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val content: String?

    /**
     * 类型 @return 笔记类型
     *
     * 数据库列名: type
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val type: String?

    /**
     * 笔记的父节点，表示当前笔记的父笔记。 通过 {@link ManyToOne} 注解与子笔记关联。 @return 父笔记，如果没有父笔记则返回 null
     *
     * 数据库列名: parent_id
     * 数据类型: int8
     *
     * 可空: 是
     *
     */
    val parentId: Long?

}
