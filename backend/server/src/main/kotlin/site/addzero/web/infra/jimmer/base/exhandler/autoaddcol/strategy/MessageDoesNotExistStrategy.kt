package site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import site.addzero.common.util.metainfo.MetaInfoUtils.extractTableName
import site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import site.addzero.web.infra.jimmer.base.exhandler.extractColumnNames
import site.addzero.web.infra.jimmer.base.exhandler.fixCol
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.net.URI

@Component
class MessageDoesNotExistStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean? {
        return message?.contains("does not exist")
    }

    override fun handle(message: String?, causeMessage: String?): ProblemDetail? {
        val tableName = extractTableName(message)
        val columnName = extractColumnNames(causeMessage)
        val fixColResult = fixCol(tableName, columnName)
        
        // 根据结果设置合适的HTTP状态码
        val status = if (fixColResult.contains("已自动添加列")) {
            HttpStatus.OK
        } else if (fixColResult.contains("添加列失败")) {
            HttpStatus.CONFLICT
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        
        return ProblemDetail.forStatusAndDetail(status, fixColResult).apply {
            title = if (status == HttpStatus.OK) "Column Auto-added" else "Column Auto-add Failed"
            type = URI.create("urn:problem-type:column-auto-add")
            setProperty("tableName", tableName)
            setProperty("columnName", columnName)
            setProperty("result", fixColResult)
        }
    }
}