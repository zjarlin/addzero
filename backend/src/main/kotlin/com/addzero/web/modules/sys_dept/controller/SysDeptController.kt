package com.addzero.web.modules.sys_dept.controller

import com.addzero.common.consts.sql
import com.addzero.kmp.generated.isomorphic.SysDeptIso
import com.addzero.kmp.jdbc.meta.public.table.impl.ISysUserImpl
import com.addzero.web.infra.jimmer.base.BaseTreeApi
import com.addzero.web.infra.jimmer.toJimmerEntity
import com.addzero.web.modules.sys_dept.entity.*
import org.babyfish.jimmer.kt.isLoaded
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sysDept")
class SysDeptController : BaseTreeApi<SysDept>{

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

        return TODO("提供返回值")
    }


    @DeleteMapping("/delete")
    fun delete(id: Long) {
        TODO("Not yet implemented")
    }


    @GetMapping("/getAvailableUsers")
    fun getAvailableUsers(lng: Long): List<ISysUserImpl> {
        TODO("Not yet implemented")
    }

}
