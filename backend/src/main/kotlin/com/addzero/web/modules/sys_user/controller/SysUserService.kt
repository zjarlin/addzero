package com.addzero.web.modules.sys_user.controller

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.StrUtil
import com.addzero.common.consts.sql
import com.addzero.kmp.api.CheckSignInput
import com.addzero.kmp.api.SecondLoginDTO
import com.addzero.kmp.exp.BizException
import com.addzero.web.modules.sys.login.identifyInputType
import com.addzero.web.modules.sys_user.entity.SysUser
import com.addzero.web.modules.sys_user.entity.email
import com.addzero.web.modules.sys_user.entity.phone
import com.addzero.web.modules.sys_user.entity.username
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

@Deprecated("有bug,三个值都有查到的人不对")
private fun user(email: String, phone: String?, username: String): SysUser? {
    val firstNonBlank = StrUtil.firstNonBlank(email, phone, username)
    val identifyInputType = identifyInputType(firstNonBlank!!)
    val user = when (identifyInputType) {
        CheckSignInput.PHONE -> {

            val firstOrNull = sql.executeQuery(SysUser::class) {
                where(
                    table.phone eq phone,
                )
                select(table)
            }.firstOrNull()
            firstOrNull
        }

        CheckSignInput.EMAIL -> {
            val firstOrNull = sql.executeQuery(SysUser::class) {
                where(
                    table.email eq email,
                )
                select(table)
            }.firstOrNull()
            firstOrNull

        }

        CheckSignInput.USERNAME -> {
            val firstOrNull = sql.executeQuery(SysUser::class) {
                where(
                    table.username eq username,
                )
                select(table)
            }.firstOrNull()
            firstOrNull

        }
    }

//        val firstOrNull = sql.executeQuery(SysUser::class) {
//            where(
//                or(
//                    table.phone eq phone,
//                    table.email eq email,
//                    table.username eq username,
//                )
//            )
//            select(table)
//        }.firstOrNull()
    return user
}

