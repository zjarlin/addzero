package com.addzero.web.modules

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
@CrossOrigin
class SwaggerController {
    @GetMapping("/ui")
    fun ui(): String {
        return "openapi"
    }
}
