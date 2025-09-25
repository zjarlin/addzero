package site.addzero.web.modules.controller.product

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.addzero.model.entity.Product
import site.addzero.web.infra.jimmer.base.BaseController

@RestController
@RequestMapping("/product")
class ProductController : BaseController<Product> {
}


