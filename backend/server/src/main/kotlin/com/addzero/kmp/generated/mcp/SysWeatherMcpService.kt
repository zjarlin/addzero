    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysWeather
    import com.addzero.kmp.generated.isomorphic.SysWeatherIso

    /**
     * SysWeather MCP服务
     *
     * 提供天气相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysWeatherMcpService  {

        @Tool(description = "保存天气数据到数据库")
        fun saveSysWeather(@ToolParam(description = "天气数据对象") entity: SysWeatherIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysWeather::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }