    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysRole
    import com.addzero.kmp.generated.isomorphic.SysRoleIso

    /**
     * SysRole MCP服务
     *
     * 提供角色相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysRoleMcpService  {

        @Tool(description = "保存角色数据到数据库")
        fun saveSysRole(@ToolParam(description = "角色数据对象") entity: SysRoleIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysRole::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }