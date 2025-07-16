    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.BizTag
    import com.addzero.kmp.generated.isomorphic.BizTagIso

    /**
     * BizTag MCP服务
     *
     * 提供标签相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class BizTagMcpService  {

        @Tool(description = "保存标签数据到数据库")
        fun saveBizTag(@ToolParam(description = "标签数据对象") entity: BizTagIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(BizTag::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }