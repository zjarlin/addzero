package com.addzero.ai.mcp

import com.addzero.common.consts.sql
import com.addzero.kmp.generated.isomorphic.SysDictIso
import com.addzero.web.infra.jimmer.toJimmerEntity
import com.addzero.web.modules.sys_dict.entity.SysDict
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
class AiDictService {
    @Tool(description = "保存字典")
    fun saveDict(@ToolParam(description = "字典数据库实体") sysDict: SysDictIso): String {
        val toJimmerEntity = sysDict.toJimmerEntity<SysDictIso, SysDict>()
        return try {
            val save = sql.save(toJimmerEntity)
            val modifiedEntity = save.modifiedEntity
            return "添加成功"
        } catch (e: Exception) {
            "添加失败: ${e.message}"
        }
    }
}