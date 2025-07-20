package com.addzero.web.modules.controller

import com.addzero.common.consts.sql
import com.addzero.model.entity.*
import com.addzero.web.infra.jimmer.base.BaseTreeApi
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sysDict")
class SysDictController : BaseTreeApi<SysDict> {


    @GetMapping("/querydict")

    fun querydict(@RequestParam keyword: String): List<SysDict> {
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
                        sysDict { }
                    }
                })
        }
        return createQuery
    }


    @PostMapping("/saveDict")
    fun saveDict(@RequestBody vO: SysDict): SysDict {
        val save = sql.save(vO)
        val modifiedEntity = save.modifiedEntity
        return modifiedEntity
    }


    @PostMapping("/saveDictItem")
    fun saveDictItem(@RequestBody impl: SysDictItem) {
        sql.save(impl)
    }


    @GetMapping("/deleteDictItem")
    fun deleteDictItem(@RequestParam lng: Long) {
        sql.deleteById(SysDictItem::class, lng)
    }

    @GetMapping("/deleteDict")
    fun deleteDict(lng: Long) {
        val deleteById = sql.deleteById(SysDict::class, lng)


    }
}