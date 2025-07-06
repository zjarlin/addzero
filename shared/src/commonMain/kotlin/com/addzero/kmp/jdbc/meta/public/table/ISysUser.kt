package com.addzero.kmp.jdbc.meta.public.table

import com.addzero.kmp.jdbc.meta.*
/**
 * ISysUser
 * 系统用户表
 */
interface ISysUser {

    /**
     * 用户ID
     * 
     * 数据库列名: id
     * 数据类型: int8
     * 主键: 是
     * 可空: 否
     * 
     */
     val id: Long

    /**
     * 手机号
     * 
     * 数据库列名: phone
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val phone: String?

    /**
     * 密码
     * 
     * 数据库列名: password
     * 数据类型: text
     * 
     * 可空: 否
     * 
     */
     val password: String

    /**
     * 头像
     * 
     * 数据库列名: avatar
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val avatar: String?

    /**
     * 昵称
     * 
     * 数据库列名: nickname
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val nickname: String?

    /**
     * 性别
     * 
     * 数据库列名: gender
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val gender: String?

    /**
     * 用户名
     * 
     * 数据库列名: username
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val username: String?

    /**
     * 邮箱
     * 
     * 数据库列名: email
     * 数据类型: text
     * 
     * 可空: 是
     * 
     */
     val email: String?

}
