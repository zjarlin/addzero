package com.addzero.kmp.jdbc.meta.public.table

/**
 * ISysDeptSysUserMapping
 */
interface ISysDeptSysUserMapping {

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
     * sys_dept_id
     *
     * 数据库列名: sys_dept_id
     * 数据类型: int8
     *
     * 可空: 是
     *
     */
    val sysDeptId: Long?

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
