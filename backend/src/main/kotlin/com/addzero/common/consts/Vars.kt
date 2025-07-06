package com.addzero.common.consts

import com.addzero.jlstarter.common.util.IPUtils.localIp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 一些常用变量
 *
 * @author zjarlin
 * @since 2023/03/02
 */
interface Vars {
    companion object {
        val ip: String = localIp
        val datePrefix: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val timePrefix: String = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS_")
        )
        val timeSuffix: String = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS")
        )
    }
}