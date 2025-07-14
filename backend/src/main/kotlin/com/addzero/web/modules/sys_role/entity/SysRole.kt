package com.addzero.web.modules.sys_role.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
// 临时注释掉，避免 KSP 处理顺序问题
// import com.addzero.kmp.generated.enums.EnumSysToggle
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
    // 临时使用字符串类型，避免 KSP 处理顺序问题
    // val status: EnumSysToggle
    val status: String

    @ManyToMany
    val sysUsers: List<SysUser>

}
