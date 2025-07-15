package com.addzero.web.modules.sys_dict.controller

import cn.hutool.extra.spring.SpringUtil
import com.addzero.common.consts.sql
import com.addzero.kmp.generated.isomorphic.SysDictIso
import com.addzero.kmp.generated.isomorphic.SysDictItemIso
import com.addzero.web.infra.jackson.convertTo
import com.addzero.web.infra.jimmer.base.BaseTreeApi
import com.addzero.web.infra.jimmer.toJimmerEntity
import com.addzero.web.modules.sys_dict.entity.SysDict
import com.addzero.web.modules.sys_dict.entity.dictName
import com.addzero.web.modules.sys_dict.entity.fetchBy
import com.addzero.web.modules.sys_dict.entity.sysDictItems
import com.addzero.web.modules.sys_dict_item.entity.SysDictItem
import com.addzero.web.modules.sys_dict_item.entity.itemText
import com.addzero.web.modules.sys_dict_item.entity.sortOrder
import com.fasterxml.jackson.databind.ObjectMapper
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.sql.kt.ast.expression.KNonNullExpression
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.babyfish.jimmer.sql.kt.ast.table.KNonNullTable
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/sysDict")
class SysDictController: BaseTreeApi<SysDict> {


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

