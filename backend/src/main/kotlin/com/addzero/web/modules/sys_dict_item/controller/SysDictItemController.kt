package com.addzero.web.modules.sys_dict_item.controller
import com.addzero.web.infra.jimmer.base.BaseTreeApi
import com.addzero.web.modules.sys_dict_item.entity.SysDictItem
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysDictItem")
class SysDictItemController: BaseTreeApi<SysDictItem> {

    @GetMapping("/page")
    fun page(): Unit {
        // TODO:
    }

    @PostMapping("/save")
    fun save(): Unit {
        // TODO:
    }

}
