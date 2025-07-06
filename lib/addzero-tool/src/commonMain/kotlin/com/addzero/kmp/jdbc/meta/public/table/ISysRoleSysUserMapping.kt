package com.addzero.kmp.jdbc.meta.public.table

import com.addzero.kmp.jdbc.meta.*
/**
 * ISysRoleSysUserMapping
 */
interface ISysRoleSysUserMapping {

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
     * sys_role_id
     * 
     * 数据库列名: sys_role_id
     * 数据类型: int8
     * 
     * 可空: 是
     * 
     */
     val sysRoleId: Long?

    /**
     * sys_user_id
     * 
     * 数据库列名: sys_user_id
     * 数据类型: int8
     * 
     * 可空: 是
     * 
     */
     val sysUserId: Long?

}
