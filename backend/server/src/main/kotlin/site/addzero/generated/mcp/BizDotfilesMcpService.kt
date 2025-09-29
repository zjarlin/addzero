package site.addzero.generated.mcp

import org.babyfish.jimmer.ImmutableObjects
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service
import site.addzero.common.consts.sql
import site.addzero.generated.isomorphic.BizDotfilesIso
import site.addzero.model.entity.BizDotfiles
import site.addzero.web.infra.jackson.toJson

/**
 * BizDotfiles MCP服务
 *
 * 提供配置文件相关的CRUD操作和AI工具
 * 自动生成的代码，请勿手动修改
 */
@Service
class BizDotfilesMcpService {

    @Tool(description = "保存配置文件数据到数据库")
    fun saveBizDotfiles(@ToolParam(description = "配置文件数据对象") entity: BizDotfilesIso): String {
        val toJson = entity.toJson()
        val fromString = ImmutableObjects.fromString(BizDotfiles::class.java, toJson)
        val save = sql.save(fromString)
        return "保存成功"

    }
}
