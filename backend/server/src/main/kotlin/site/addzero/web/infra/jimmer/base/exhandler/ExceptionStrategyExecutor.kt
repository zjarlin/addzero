package site.addzero.web.infra.jimmer.base.exhandler

import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import org.springframework.http.ProblemDetail
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

fun fixCol(extractTableName: String?, extractColumnNames: String?): String {
    if (StrUtil.isAllNotBlank(extractColumnNames, extractTableName)) {
        val s = "alter table $extractTableName add column $extractColumnNames varchar(255)"
        val jdbcTemplate = SpringUtil.getBean<JdbcTemplate>(JdbcTemplate::class.java)
        try {
            jdbcTemplate.execute(s)
        } catch (e: Exception) {
            e.printStackTrace()
            val s1 = "${extractTableName}添加列失败$extractColumnNames"
            return s1
        }
        val s1 = "${extractTableName}已自动添加列$extractColumnNames"
        return s1
    }
    val s1 = "${extractTableName}添加列失败$extractColumnNames"
    return s1
}

fun extractColumnNames(sql: String?): String {
    if (sql.isNullOrEmpty()) {
        return ""
    }
    val subBetween = StrUtil.subBetween(sql, "ERROR: column tb_1_.", " does not exist")
    return subBetween!!
}


@Component
class ExceptionStrategyExecutor(
    private val strategies: MutableList<out AutoAddColStrategy>
    , private val jdbcTemplate: JdbcTemplate
) {
    fun handleException(message: String?, causeMessage: String?): ProblemDetail? {
        // 遍历策略，找到第一个可处理的策略
        try {
            val strategy = strategies.first { it.canHandle(message, causeMessage) == true }
            return strategy.handle(message, causeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}
