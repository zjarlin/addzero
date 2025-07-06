package com.addzero.kmp.jdbc.meta.public.table

import com.addzero.kmp.jdbc.meta.*
/**
 * IBizMapping
 * 多对多关系表
 */
interface IBizMapping {

    /**
     * from_id
     * 
     * 数据库列名: from_id
     * 数据类型: int8
     * 主键: 是
     * 可空: 否
     * 
     */
     val fromId: Long

    /**
     * to_id
     * 
     * 数据库列名: to_id
     * 数据类型: int8
     * 主键: 是
     * 可空: 否
     * 
     */
     val toId: Long

    /**
     * mapping_type
     * 
     * 数据库列名: mapping_type
     * 数据类型: text
     * 主键: 是
     * 可空: 否
     * 
     */
     val mappingType: String

}
