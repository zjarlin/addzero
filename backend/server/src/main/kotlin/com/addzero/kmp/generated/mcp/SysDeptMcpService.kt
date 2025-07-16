    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysDept
    import com.addzero.kmp.generated.isomorphic.SysDeptIso

    /**
     * SysDept MCP服务
     *
     * 提供部门相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysDeptMcpService  {

        @Tool(description = "保存部门数据到数据库")
        fun saveSysDept(@ToolParam(description = "部门数据对象") entity: SysDeptIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysDept::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }