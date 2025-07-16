    package com.addzero.kmp.generated.mcp

    import com.addzero.common.consts.sql
    import com.addzero.web.infra.jackson.toJson
    import org.babyfish.jimmer.ImmutableObjects
    import org.springframework.ai.tool.annotation.Tool
    import org.springframework.ai.tool.annotation.ToolParam
    import org.springframework.stereotype.Service
    import com.addzero.model.entity.SysDictItem
    import com.addzero.kmp.generated.isomorphic.SysDictItemIso

    /**
     * SysDictItem MCP服务
     *
     * 提供字典项相关的CRUD操作和AI工具
     * 自动生成的代码，请勿手动修改
     */
    @Service
    class SysDictItemMcpService  {

        @Tool(description = "保存字典项数据到数据库")
        fun saveSysDictItem(@ToolParam(description = "字典项数据对象") entity: SysDictItemIso): String {
               val toJson = entity.toJson()
val fromString = ImmutableObjects.fromString(SysDictItem::class.java, toJson)
val save = sql.save(fromString)
return "保存成功"

        }
    }