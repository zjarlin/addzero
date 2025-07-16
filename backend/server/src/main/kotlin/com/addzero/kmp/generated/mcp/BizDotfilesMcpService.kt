    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.BizDotfiles
    import com.addzero.kmp.generated.isomorphic.BizDotfilesIso

    /**
     * BizDotfiles MCP服务
     *
     * 提供配置文件相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class BizDotfilesMcpService  {

        @Tool(description = "保存配置文件数据到数据库")
        fun saveBizDotfiles(@ToolParam(description = "配置文件数据对象") entity: BizDotfilesIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(BizDotfiles::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }