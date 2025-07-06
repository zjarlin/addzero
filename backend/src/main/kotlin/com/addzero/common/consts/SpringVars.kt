package com.addzero.common.consts

import cn.hutool.extra.spring.SpringUtil
import com.addzero.jlstarter.common.util.IPUtils.localIp
import org.babyfish.jimmer.sql.kt.KSqlClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 一些常用变量
 *
 * @author zjarlin
 * @since 2023/03/02
 */
        val sql= SpringUtil.getBean(KSqlClient::class.java)

interface SpringVars {
    companion object {

    }
}
