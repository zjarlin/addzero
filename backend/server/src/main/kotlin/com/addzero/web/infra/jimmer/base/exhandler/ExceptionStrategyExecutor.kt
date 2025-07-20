package com.addzero.web.infra.jimmer.base.exhandler

import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.kmp.kt_util.isBlank
import com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
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
    if (sql.isBlank()) {
        return ""
    }
    val subBetween = StrUtil.subBetween(sql, "ERROR: column tb_1_.", " does not exist")
    return subBetween!!
}


@Component
class ExceptionStrategyExecutor(
    private val strategies: MutableList<out AutoAddColStrategy>, private val jdbcTemplate: JdbcTemplate
) {
    fun handleException(message: String?, causeMessage: String?): Boolean? {
        // 遍历策略，找到第一个可处理的策略
        val handle = try {
            val strategy = strategies.first { it.canHandle(message, causeMessage) == true }
            strategy.handle(message, causeMessage)
            return true
        } catch (e: Exception) {
            return false
        }
        return false
    }

}
