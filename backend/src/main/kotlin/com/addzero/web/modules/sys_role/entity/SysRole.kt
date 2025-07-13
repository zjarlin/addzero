package com.addzero.web.modules.sys_role.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.kmp.generated.enums.EnumSysToggle
import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.modules.sys_user.entity.SysUser
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.Table

/**
 * 系统角色实体
 * 对应数据库表 'sys_role'
 */
@Entity
@Table(name = "sys_role")
interface SysRole : BaseEntity {

    /**
     * 角色编码
     */
    @Key
    val roleCode: String

    /**
     * 角色名称
     */
    @LabelProp
    val roleName: String

    /**
     * 是否为系统角色
     */
    val systemFlag: Boolean

    /**
     * 角色状态
     */
    val status: EnumSysToggle

    @ManyToMany
    val sysUsers: List<SysUser>

}
