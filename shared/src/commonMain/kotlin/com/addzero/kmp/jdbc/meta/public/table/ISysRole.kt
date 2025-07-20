package com.addzero.kmp.jdbc.meta.public.table

/**
 * ISysRole
 * 系统角色实体对应数据库表
 */
interface ISysRole {

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
     * 角色编码
     *
     * 数据库列名: role_code
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val roleCode: String?

    /**
     * 角色名称
     *
     * 数据库列名: role_name
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val roleName: String?

    /**
     * 是否为系统角色
     *
     * 数据库列名: system_flag
     * 数据类型: bool
     *
     * 可空: 是
     *
     */
    val systemFlag: Boolean?

    /**
     * 角色状态
     *
     * 数据库列名: status
     * 数据类型: text
     *
     * 可空: 是
     *
     */
    val status: String?

}
