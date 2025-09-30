package site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import cn.hutool.core.util.StrUtil
import site.addzero.common.kt_util.removeBlankOrQuotation
import site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import site.addzero.web.infra.jimmer.base.exhandler.fixCol
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.net.URI

@Component
class CauseDoesNotExistStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean {
        return causeMessage?.contains("does not exist") == true
    }

    override fun handle(message: String?, causeMessage: String?): ProblemDetail? {
        val columnName =
            StrUtil.subBetween(causeMessage, "was aborted: ERROR: column \"", " of relation ")?.removeBlankOrQuotation()
        val tableName = StrUtil.subBetween(causeMessage, "of relation \"", " does not exist")?.removeBlankOrQuotation()
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