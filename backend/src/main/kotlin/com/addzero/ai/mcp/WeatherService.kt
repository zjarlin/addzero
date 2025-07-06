package com.addzero.ai.mcp

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import kotlin.random.Random

/**
 * 天气服务 - MCP工具示例
 */
@Service
class WeatherService {

    @Tool(description = "获取指定城市的当前天气信息")
    fun getCurrentWeather(city: String): String {
        // 模拟天气数据
        val temperatures = listOf(15, 18, 22, 25, 28, 30, 32)
        val conditions = listOf("晴天", "多云", "阴天", "小雨", "大雨", "雪")
        
        val temperature = temperatures.random()
        val condition = conditions.random()
        val humidity = Random.nextInt(30, 90)
        
        return """
            城市: $city
            温度: ${temperature}°C
            天气: $condition
            湿度: ${humidity}%
            更新时间: ${java.time.LocalDateTime.now()}
        """.trimIndent()
    }

    @Tool(description = "获取指定城市未来几天的天气预报")
    fun getWeatherForecast(city: String, days: Int = 3): String {
        val forecasts = mutableListOf<String>()
        val conditions = listOf("晴天", "多云", "阴天", "小雨")
        
        repeat(days) { day ->
            val date = java.time.LocalDate.now().plusDays(day.toLong())
            val temperature = Random.nextInt(15, 35)
            val condition = conditions.random()
            
            forecasts.add("${date}: ${temperature}°C, $condition")
        }
        
        return """
            $city 未来${days}天天气预报:
            ${forecasts.joinToString("\n")}
        """.trimIndent()
    }
}
