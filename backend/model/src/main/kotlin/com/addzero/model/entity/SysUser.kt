package com.addzero.model.entity

// 临时注释掉，避免 KSP 处理顺序问题
// import com.addzero.kmp.generated.enums.EnumSysGender
import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.model.common.BaseDateTime
import com.addzero.model.common.SnowflakeIdGenerator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.babyfish.jimmer.sql.*

/**
 * 用户
 * @author zjarlin
 * @date 2024/11/03
 * @constructor 创建[SysUser]
 *
 */
@Entity
@Table(name = "sys_user")
interface SysUser : BaseDateTime {
    /**
     *价格
     */
    val price: String

    /**
     *整数
     */
    val testInt: Int

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
    val id: Long

    /**
     * 手机号
     */
    @Key(group = "phone")
    @get:Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @get:NotBlank
    val phone: String?


    /**
     * 电子邮箱
     */
    @Key(group = "email")
    @LabelProp
    val email: String

    /**
     * 用户名
     */
    @Key(group = "username")
    @LabelProp
    val username: String

    /**
     * 密码
     */
    val password: String

    /**
     * 头像
     */
    val avatar: String?


    /**
     * 昵称
     */
    val nickname: String?

    /**
     * 性别
     */
    // 临时使用字符串类型，避免 KSP 处理顺序问题
    // val gender: EnumSysGender?
    val gender: String?

    /**
     * 所属部门
     */
    @ManyToMany(mappedBy = "sysUsers")
    val depts: List<SysDept>

    /**
     * 角色列表
     */
    @ManyToMany(mappedBy = "sysUsers")
    val roles: List<SysRole>
}
