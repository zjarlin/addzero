package site.addzero.model.entity

// 临时注释掉，避免 KSP 处理顺序问题
// import site.addzero.generated.enums.EnumSysToggle
import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.Table
import site.addzero.model.entity.SysUser

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
