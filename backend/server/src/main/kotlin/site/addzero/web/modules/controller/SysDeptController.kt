package site.addzero.web.modules.controller

import site.addzero.common.consts.sql
import site.addzero.generated.isomorphic.SysDeptIso
import site.addzero.model.entity.SysDept
import site.addzero.web.infra.jimmer.base.BaseTreeApi
import site.addzero.web.infra.jimmer.toJimmerEntity
import org.babyfish.jimmer.kt.isLoaded
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sysDept")
class SysDeptController : BaseTreeApi<SysDept> {

//    @GetMapping("/tree")
//    fun tree(keyword: String): List<SysDeptIso> {
//        val map = sql.executeQuery(SysDept::class) {
//            where(
//                or(
//                    table.name `ilike?` keyword,
//                    table.children {
//                        name `ilike?` keyword
//                    }
//                ),
//                table.parentId.isNull()
//            )
//            select(table.fetchBy {
//                allScalarFields()
//                `children*`()
//            })
//        }
//        return map.convertTo()
//    }

    @PostMapping("/save")
    fun save(@RequestBody dept: SysDeptIso): SysDept {
        val convertTo = dept.toJimmerEntity<SysDeptIso, SysDept>()

        val sysDept = SysDept(convertTo) {
            if (!isLoaded(this, SysDept::parent)) {
                parent = null
            }
        }
        val save = sql.save(sysDept)
        return save.modifiedEntity
    }


    @GetMapping("/get/{id}")
    fun get(id: Long): SysDeptIso {

        return SysDeptIso()
    }


    @DeleteMapping("/delete")
    fun delete(id: Long) {
        TODO("Not yet implemented")
    }



}
