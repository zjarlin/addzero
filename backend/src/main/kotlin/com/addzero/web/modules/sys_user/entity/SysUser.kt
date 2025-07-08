package com.addzero.web.modules.sys_user.entity

import com.addzero.kmp.generated.enums.EnumSysGender
import com.addzero.web.infra.jimmer.SnowflakeIdGenerator
import com.addzero.web.infra.jimmer.base.basedatetime.BaseDateTime
import com.addzero.web.modules.sys_dept.entity.SysDept
import com.addzero.web.modules.sys_role.entity.SysRole
import jakarta.validation.Validation
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.babyfish.jimmer.sql.*
fun main() {
    val sysUser = SysUser {
        phone = "13302334455"
    }

    val sysUsera1 = SysUser {
        phone = "133023344559999999"
    }

    val factory = Validation.buildDefaultValidatorFactory()
    val validator = factory.validator


    val toList = sequenceOf(sysUser, sysUsera1).flatMap {
        validator.validate(it)
    }.toList()
    println(toList)

}

/**
 *
 * @author zjarlin
 * @date 2024/11/03
 * @constructor 创建[SysUser]
 *
 */
@Entity
@Table(name = "sys_user")
//@ValidImmutable
interface SysUser : BaseDateTime {
    /**
     *价格
     */
    val price: String
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
    val email: String

    /**
     * 用户名
     */
    @Key(group = "username")
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
    val gender: EnumSysGender?

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
