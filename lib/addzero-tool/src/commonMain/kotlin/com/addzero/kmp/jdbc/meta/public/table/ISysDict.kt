package com.addzero.kmp.jdbc.meta.public.table

import com.addzero.kmp.jdbc.meta.*
/**
 * ISysDict
 * 字典表
 */
interface ISysDict {

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
     * 字典名称
     * 
     * 数据库列名: dict_name
     * 数据类型: text
     * 
     * 可空: 否
     * 
     */
     val dictName: String

    /**
     * 字典编码
     * 
     * 数据库列名: dict_code
     * 数据类型: text
     * 
     * 可空: 否
     * 
     */
     val dictCode: String

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

}
