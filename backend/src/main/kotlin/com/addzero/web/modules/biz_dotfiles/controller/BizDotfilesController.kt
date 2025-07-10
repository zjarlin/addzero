package com.addzero.web.modules.biz_dotfiles.controller
import com.addzero.web.modules.sys_user.entity.SysUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.full.memberProperties

@RestController
@RequestMapping("/bizDotfiles")
class BizDotfilesController {

    @GetMapping("/page")
    fun page(): Unit {
        // TODO:
    }

    @PostMapping("/save")
    fun save(): Unit {
        // TODO:
    }

}
