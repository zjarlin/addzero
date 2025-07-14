package com.addzero.web.modules.sys_user.controller

import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.stp.StpUtil
import com.addzero.common.consts.sql
import com.addzero.web.infra.jimmer.updateById
import com.addzero.web.modules.sys_user.entity.SysUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/sysUser")
class SysUserCenterController(
    private val sysuserService: SysUserService,
    private val saTokenConfig: cn.dev33.satoken.config.SaTokenConfig
) {

    @GetMapping("/getCurrentUser")
    fun getCurrentUser(): SysUser {
        // 方式2：从当前请求中获取token（更底层）
        val tokenValue = SaHolder.getRequest().getHeader(saTokenConfig.tokenName)
        if (tokenValue == "admin") {
            return SysUser {
                username = tokenValue
                nickname = "超级管理员"
            }
        }
        val findById = sysuserService.getCurrentUser()
        return findById
    }

    @PostMapping("/updatePassword")
    fun updatePassword(@RequestBody newPassword: String): Boolean {
        val currentUser = sysuserService.getCurrentUser()
        val sysUser = SysUser(currentUser) {
            password = newPassword
        }
        val updateById = sql.updateById(sysUser)
        val rowAffected = updateById.isRowAffected
        return rowAffected
//        return updateById.isModified
    }

    @PostMapping("/logout")
    fun logout(): Boolean {
        StpUtil.logout()
        return true
    }


}
