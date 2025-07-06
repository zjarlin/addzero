package com.addzero.web.infra

import com.addzero.web.modules.sys_area.entity.SysArea
import com.addzero.web.modules.sys_user.entity.SysUser

val entityMap = mapOf(
    "sys_user" to SysUser::class,
    "sys_area" to SysArea::class
)
