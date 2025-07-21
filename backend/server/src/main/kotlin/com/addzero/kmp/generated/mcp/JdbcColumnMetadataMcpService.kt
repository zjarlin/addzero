    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.JdbcColumnMetadata
    import com.addzero.kmp.generated.isomorphic.JdbcColumnMetadataIso

    /**
     * JdbcColumnMetadata MCP服务
     *
     * 提供jdbccolumnmetadata相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class JdbcColumnMetadataMcpService  {

        @Tool(description = "保存jdbccolumnmetadata数据到数据库")
        fun saveJdbcColumnMetadata(@ToolParam(description = "jdbccolumnmetadata数据对象") entity: JdbcColumnMetadataIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(JdbcColumnMetadata::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }