    package site.addzero.generated.mcp

    import site.addzero.common.consts.sql
    import site.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import site.addzero.model.entity.ThingModel
    import site.addzero.generated.isomorphic.ThingModelIso

    /**
     * ThingModel MCP服务
     *
     * 提供thingmodel相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class ThingModelMcpService  {

        @Tool(description = "保存thingmodel数据到数据库")
        fun saveThingModel(@ToolParam(description = "thingmodel数据对象") entity: ThingModelIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(ThingModel::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }