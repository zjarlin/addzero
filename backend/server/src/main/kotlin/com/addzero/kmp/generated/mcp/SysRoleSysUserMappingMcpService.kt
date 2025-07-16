    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysRoleSysUserMapping
    import com.addzero.kmp.generated.isomorphic.SysRoleSysUserMappingIso

    /**
     * SysRoleSysUserMapping MCP服务
     *
     * 提供rolesysusermapping相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysRoleSysUserMappingMcpService  {

        @Tool(description = "保存rolesysusermapping数据到数据库")
        fun saveSysRoleSysUserMapping(@ToolParam(description = "rolesysusermapping数据对象") entity: SysRoleSysUserMappingIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysRoleSysUserMapping::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }