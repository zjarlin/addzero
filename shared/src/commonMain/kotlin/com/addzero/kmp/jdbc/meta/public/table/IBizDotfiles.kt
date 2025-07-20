package com.addzero.kmp.jdbc.meta.public.table

/**
 * IBizDotfiles
 * 配置文件管理
 */
interface IBizDotfiles {

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
     * 操作系统
     *
     * 数据库列名: os_type
     * 数据类型: int4
     *
     * 可空: 否
     *
     */
    val osType: Long

    /**
     * 系统架构
     *
     * 数据库列名: os_structure
     * 数据类型: text
     *
     * 可空: 否
     *
     */
    val osStructure: String

    /**
     * 定义类型
     *
     * 数据库列名: def_type
     * 数据类型: text
     *
     * 可空: 否
     *
     */
    val defType: String

    /**
     * 名称
     *
     * 数据库列名: name
     * 数据类型: text
     *
     * 可空: 否
     *
     */
    val name: String

    /**
     * 值
     *
     * 数据库列名: value
     * 数据类型: text
     *
     * 可空: 否
     *
     */
    val value: String

    /**
     * 注释
     *
     * 数据库列名: describtion
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val describtion: String?

    /**
     * 状态1=启用0=未启用
     *
     * 数据库列名: status
     * 数据类型: int4
     *
     * 可空: 否
     *
     */
    val status: Long

    /**
     * 文件地址
     *
     * 数据库列名: file_url
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val fileUrl: String?

    /**
     * 文件位置
     *
     * 数据库列名: location
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val location: String?

}
