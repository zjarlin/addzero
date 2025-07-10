package com.addzero.web.modules.sys_user.controller

import com.addzero.common.consts.sql
import com.addzero.kmp.entity.PageResult
import com.addzero.kmp.isomorphic.SysUserIso
import com.addzero.web.infra.jackson.convertTo
import com.addzero.web.modules.sys_user.entity.SysUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysUser")
class SysUserController {

    @GetMapping("/page")
    fun page(): PageResult<SysUserIso> {
        val fetchPage = sql.createQuery(SysUser::class) {
            select(table)
        }.fetchPage(0, 10)
        return fetchPage.convertTo<PageResult<SysUserIso>>()
    }

    @PostMapping("/save")
    fun save(): Unit {
        // TODO:
    }

}
