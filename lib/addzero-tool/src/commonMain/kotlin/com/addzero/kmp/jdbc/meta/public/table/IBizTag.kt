package com.addzero.kmp.jdbc.meta.public.table

import com.addzero.kmp.jdbc.meta.*
/**
 * IBizTag
 * 标签实体类，用于管理笔记的标签系统 该实体类映射到数据库表 `biz_tag`
 */
interface IBizTag {

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
     * 标签名称
     * 
     * 数据库列名: name
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val name: String?

    /**
     * 标签描述
     * 
     * 数据库列名: description
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val description: String?

}
