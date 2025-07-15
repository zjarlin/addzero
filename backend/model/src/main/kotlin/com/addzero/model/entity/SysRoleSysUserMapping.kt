package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

/**
 * SysRoleSysUserMapping
 *
 * 对应数据库表: sys_role_sys_user_mapping
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "sys_role_sys_user_mapping")
interface SysRoleSysUserMapping : BaseEntity {

    /**
     * sysRoleId
     */
    @Column(name = "sys_role_id")
    val sysRoleId: Long?

    /**
     * sysUserId
     */
    @Column(name = "sys_user_id")
    val sysUserId: Long?
}