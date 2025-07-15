package com.addzero.web.modules.service

import cn.dev33.satoken.stp.StpUtil
import com.addzero.common.consts.sql
import com.addzero.kmp.entity.CheckSignInput
import com.addzero.kmp.entity.SecondLoginDTO
import com.addzero.kmp.exp.BizException
import com.addzero.model.entity.SysUser
import com.addzero.model.entity.email
import com.addzero.model.entity.phone
import com.addzero.model.entity.username
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Service

@Service
class SysUserService {
    fun getCurrentUser(): SysUser {
        // 获取当前登录用户ID
        val userId = StpUtil.getLoginIdAsLong()
        val findById = sql.findById(SysUser::class, userId) ?: throw BizException("用户未找到")
        return findById

    }

    fun findByUserRegFormState(secondLoginDTO: SecondLoginDTO): SysUser? {
        val userRegFormState = secondLoginDTO.userRegFormState

        val email = userRegFormState.email
        val phone = userRegFormState.phone
        val username = userRegFormState.username

        val user = sql.executeQuery(SysUser::class) {
            where(
                table.phone eq phone,
                table.email eq email,
                table.username eq username,
            )
            select(table)
        }.firstOrNull()

//        val user = user(email, phone, username)
        return user
    }

}

fun identifyInputType(input: String): CheckSignInput {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val phoneRegex = Regex("^1[3-9]\\d{9}$")
    return when {
        emailRegex.matches(input) -> CheckSignInput.EMAIL
        phoneRegex.matches(input) -> CheckSignInput.PHONE
        else -> CheckSignInput.USERNAME
    }
}