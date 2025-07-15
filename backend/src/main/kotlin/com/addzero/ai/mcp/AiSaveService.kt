//package com.addzero.ai.mcp
//
//import com.addzero.common.consts.sql
//import com.addzero.kmp.generated.isomorphic.SysDictIso
//import com.addzero.web.infra.jackson.toJson
//import com.addzero.web.modules.sys_dict.entity.SysDict
//import org.babyfish.jimmer.ImmutableObjects
//import org.springframework.ai.tool.annotation.Tool
//import org.springframework.stereotype.Service
//
//
//@Service
//class DictService {
//    @Tool(description = "保存字典")
//    fun save(entity: SysDictIso): String {
//        val toJson = entity.toJson()
//        val fromString = ImmutableObjects.fromString(SysDict::class.java, toJson)
//        val save = sql.save(fromString)
//        val modifiedEntity = save.modifiedEntity
//        return "保存成功"
//    }
//}
