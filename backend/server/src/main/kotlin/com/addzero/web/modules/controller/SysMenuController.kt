package com.addzero.web.modules.controller

import com.addzero.kmp.entity.sys.menu.SysMenuVO
import com.addzero.kmp.mock.mockkSysMunu
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysMenu")
class SysMenuController {

       @PostMapping("/getSysMenu")
    fun getAllMenu(@RequestBody params: Set<String>): Map<String, SysMenuVO> {
        val defaultSysMenuVO = mockkSysMunu()
        return defaultSysMenuVO
    }



}