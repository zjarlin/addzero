package site.addzero.web.modules.controller.product

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.addzero.model.entity.ProductCategory
import site.addzero.web.infra.jimmer.base.BaseController

@RestController
@RequestMapping("/productCategory")
class ProductCategoryController : BaseController<ProductCategory>
