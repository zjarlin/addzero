package com.addzero.web.infra

import com.addzero.model.entity.SysArea
import com.addzero.model.entity.SysUser

val entityMap = mapOf(
    "sys_user" to SysUser::class,
    "sys_area" to SysArea::class
)
