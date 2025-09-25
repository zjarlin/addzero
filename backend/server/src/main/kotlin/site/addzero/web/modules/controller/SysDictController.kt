package site.addzero.web.modules.controller

import site.addzero.common.consts.sql
import site.addzero.exp.BizException
import site.addzero.model.entity.*
import site.addzero.web.infra.jimmer.base.BaseController
import site.addzero.web.infra.jimmer.base.BaseTreeApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/sysDict")
class SysDictController : BaseTreeApi<SysDict> , BaseController<SysDict> {
    @GetMapping("/querydict")
    fun querydict(@RequestParam keyword: String): List<SysDict> {
//        throw BizException("dddddddddd")
        val createQuery = sql.executeQuery(SysDict::class) {
            where(
                or(
                    table.dictName `ilike?` keyword, table.sysDictItems {
                        itemText `ilike?` keyword
                    }

                ))
            orderBy(table.dictName.asc())

            select(
                table.fetchBy {
                    allScalarFields()
                    sysDictItems({
                        filter {

                            orderBy(
                                table.sortOrder.asc(),
                                table.itemText.asc(),
                            )
                        }
                    }) {
                        allScalarFields()
                        sysDict() { }
                    }
                })
        }
        return createQuery
    }


//    @PostMapping("/saveDict")
//    fun saveDict(@RequestBody vO: SysDict): SysDict {
//        val save = sql.save(vO)
//        val modifiedEntity = save.modifiedEntity
//        return modifiedEntity
//    }
//
//
//
//    @GetMapping("/deleteDict")
//    fun deleteDict(lng: Long) {
//        val deleteById = sql.deleteById(SysDict::class, lng)
//
//
//    }
}
