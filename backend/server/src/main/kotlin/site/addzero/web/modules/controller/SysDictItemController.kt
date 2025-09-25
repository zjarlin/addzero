package site.addzero.web.modules.controller

import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import site.addzero.model.entity.SysDictItem
import site.addzero.web.infra.jimmer.base.BaseTreeApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import site.addzero.common.consts.sql
import site.addzero.web.infra.jimmer.base.BaseController

@RestController
@RequestMapping("/sysDictItem")
class SysDictItemController : BaseTreeApi<SysDictItem> , BaseController<SysDictItem>{

}
