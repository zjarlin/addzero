    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysFavoriteTab
    import com.addzero.kmp.generated.isomorphic.SysFavoriteTabIso

    /**
     * SysFavoriteTab MCP服务
     *
     * 提供favoritetab相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysFavoriteTabMcpService  {

        @Tool(description = "保存favoritetab数据到数据库")
        fun saveSysFavoriteTab(@ToolParam(description = "favoritetab数据对象") entity: SysFavoriteTabIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysFavoriteTab::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }