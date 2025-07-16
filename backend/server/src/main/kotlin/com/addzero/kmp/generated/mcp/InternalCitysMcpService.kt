    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.InternalCitys
    import com.addzero.kmp.generated.isomorphic.InternalCitysIso

    /**
     * InternalCitys MCP服务
     *
     * 提供internalcitys相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class InternalCitysMcpService  {

        @Tool(description = "保存internalcitys数据到数据库")
        fun saveInternalCitys(@ToolParam(description = "internalcitys数据对象") entity: InternalCitysIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(InternalCitys::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }