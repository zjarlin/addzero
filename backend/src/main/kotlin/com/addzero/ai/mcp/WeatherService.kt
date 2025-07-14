package com.addzero.ai.mcp

import cn.hutool.core.date.DateUtil
import com.addzero.common.consts.sql
import com.addzero.web.modules.entity.Citys
import com.addzero.web.modules.entity.areaName
import com.addzero.web.modules.entity.pinyin
import com.addzero.web.modules.entity.py
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service
import weatherutil.WeatherData
import weatherutil.WeatherUtil
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 天气服务 - MCP工具示例
 */
@Service
class WeatherService {

    @Tool(description = "获取城市历史天气信息")
    fun getCityWeather(
        city: String,
        year: String,
        month: String,
        @ToolParam(description = "默认展示一个月的数据,该参数表示裁剪几天的数据,默认三天") limit: Int = 3
    ): Map<String?, List<WeatherData?>> {
        val citys = sql.executeQuery(Citys::class) {
            where(
                or(
                    table.pinyin `ilike?` city,
                    table.py `ilike?` city,
                    table.areaName `ilike?` city,
//                                    table.cityName `ilike?` city
                )
            )
            select(table)
        }.take(limit)
        val associate = citys.associate {
            val queryWeather1 = WeatherUtil.queryWeather(year, month, it.areaId, "2")
            val sortedBy = queryWeather1
//                .sortedBy {
//                    DateUtil.parse(it?.date)
//                }
            val queryWeather = sortedBy.take(limit)
            it.areaName to queryWeather
        }
        return associate

    }

}
