    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysDeptSysUserMapping
    import com.addzero.kmp.generated.isomorphic.SysDeptSysUserMappingIso

    /**
     * SysDeptSysUserMapping MCP服务
     *
     * 提供deptsysusermapping相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysDeptSysUserMappingMcpService  {

        @Tool(description = "保存deptsysusermapping数据到数据库")
        fun saveSysDeptSysUserMapping(@ToolParam(description = "deptsysusermapping数据对象") entity: SysDeptSysUserMappingIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysDeptSysUserMapping::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }