package com.addzero.model.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 *部门
 */

@Entity
@Table(name = "sys_dept")
interface SysDept : BaseEntity {

    /**部门名称  */
    @Key
    @LabelProp
    val name: String

    @ManyToOne
    @Key
    @JoinColumn(name = "parent_id")
    val parent: SysDept?

    @OneToMany(mappedBy = "parent")
    val children: List<SysDept>

    /** 部门用户 */
    @ManyToMany
    val sysUsers: List<SysUser>

}