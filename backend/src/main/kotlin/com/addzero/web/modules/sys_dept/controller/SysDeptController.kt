package com.addzero.web.modules.sys_dept.controller

import com.addzero.common.consts.sql
import com.addzero.kmp.isomorphic.SysDeptIso
import com.addzero.kmp.jdbc.meta.public.table.impl.ISysUserImpl
import com.addzero.web.infra.jackson.convertTo
import com.addzero.web.infra.jimmer.toJimmerEntity
import com.addzero.web.modules.sys_dept.entity.*
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.*

@RestController
class SysDeptController {

    @GetMapping("/sysDept/tree")
    fun tree(keyword: String): List<SysDeptIso> {
        val map = sql.executeQuery(SysDept::class) {
//            where(
//                table.name `ilike?` keyword
//            )
            where(


                or(
                    table.name `ilike?` keyword,
                    table.children {
                        name `ilike?` keyword
                    }
                )

//                table.name `ilike?` keyword
                ,
                table.parentId.isNull()

            )
            select(table.fetchBy {
                allScalarFields()
                `children*`()
//                sysUsers {  }
            })
        }
        return map.convertTo()
    }

    @PostMapping("/sysDept/save")
    fun save(@RequestBody dept: SysDeptIso): SysDeptIso {
        val convertTo = dept.toJimmerEntity<SysDeptIso, SysDept>()

        val sysDept = SysDept(convertTo) {
            if (!isLoaded(this, SysDept::parent)) {
                parent = null
            }
        }
        val save = sql.save(sysDept)
        return save.convertTo()
    }


    @GetMapping("/sysDept/get/{id}")
    fun get(id: Long): SysDeptIso {

        return TODO("提供返回值")
    }


    @DeleteMapping("/sysDept/delete")
    fun delete(id: Long) {
        TODO("Not yet implemented")
    }


    @GetMapping("/sysDept/getAvailableUsers")
    fun getAvailableUsers(lng: Long): List<ISysUserImpl> {
        TODO("Not yet implemented")
    }

}
