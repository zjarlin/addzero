package site.addzero.model.entity

import org.babyfish.jimmer.sql.*
import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity

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

    @Key
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    @JoinColumn(name = "parent_id")
    val parent: SysDept?

    @OneToMany(mappedBy = "parent")
    val children: List<SysDept>

    /** 部门用户 */
    @ManyToMany
    val sysUsers: List<SysUser>

}
