    package site.addzero.generated.mcp

    import site.addzero.common.consts.sql
    import site.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import site.addzero.model.entity.BizNote
    import site.addzero.generated.isomorphic.BizNoteIso

    /**
     * BizNote MCP服务
     *
     * 提供笔记相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class BizNoteMcpService  {

        @Tool(description = "保存笔记数据到数据库")
        fun saveBizNote(@ToolParam(description = "笔记数据对象") entity: BizNoteIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(BizNote::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }