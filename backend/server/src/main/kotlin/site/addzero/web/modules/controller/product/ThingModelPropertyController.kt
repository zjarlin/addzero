package site.addzero.web.modules.controller.product

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.addzero.model.entity.biz_device.ThingModelProperty
import site.addzero.web.infra.jimmer.base.BaseController

@RestController
@RequestMapping("/thingModelProperty")
class ThingModelPropertyController : BaseController<ThingModelProperty> {
}
