package com.addzero.web.modules.controller

import com.addzero.model.entity.SysDictItem
import com.addzero.web.infra.jimmer.base.BaseTreeApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysDictItem")
class SysDictItemController : BaseTreeApi<SysDictItem> {

    @GetMapping("/page")
    fun page(): Unit {
        // TODO:
    }

    @PostMapping("/save")
    fun save(): Unit {
        // TODO:
    }

}