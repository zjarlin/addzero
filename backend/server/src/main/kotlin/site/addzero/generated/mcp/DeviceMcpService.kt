    package site.addzero.generated.mcp

    import site.addzero.common.consts.sql
    import site.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import site.addzero.model.entity.Device
    import site.addzero.generated.isomorphic.DeviceIso

    /**
     * Device MCP服务
     *
     * 提供device相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class DeviceMcpService  {

        @Tool(description = "保存device数据到数据库")
        fun saveDevice(@ToolParam(description = "device数据对象") entity: DeviceIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(Device::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }