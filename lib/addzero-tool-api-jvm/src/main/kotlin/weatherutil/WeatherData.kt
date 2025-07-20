package weatherutil


/**
 * @author zjarlin
 * @since 2023/4/9 22:36
 */
data class WeatherData(
    /** 日期  */
    var date: String,

    /** 最高温度  */
    val highTemp: Int,
    /** 最低温度  */
    val lowTemp: Int,
    /** 上午  */
    val amCondition: String,
    /** 下午  */
    val pmCondition: String,
    /** 风力风向  */
    val wind: String,
    /** 空气质量指数  */
    val aqi: Int
) {
    var areaId: String? = null

    var areaType: String? = null
    var week: String? = null

    /**
     * 🎨 格式化天气信息
     *
     * 将天气数据的所有字段及其注释以格式化的方式返回字符串。
     * 包含字段名称、注释说明和实际值。
     *
     * @return 格式化后的天气信息字符串
     */
    fun formatWeather(): String {
        val tempRange = if (highTemp > 0 && lowTemp > 0) {
            "${lowTemp}°C ~ ${highTemp}°C"
        } else {
            "温度数据不完整"
        }

        val weatherDesc = listOfNotNull(amCondition, pmCondition).joinToString(" / ")
        val weatherDescLine = if (weatherDesc.isNotEmpty()) {
            "\n🌤️ 全天天气: $weatherDesc"
        } else ""

        val airQuality = if (aqi > 0) {
            val level = when {
                aqi <= 50 -> "优秀 🟢"
                aqi <= 100 -> "良好 🟡"
                aqi <= 150 -> "轻度污染 🟠"
                aqi <= 200 -> "中度污染 🔴"
                aqi <= 300 -> "重度污染 🟣"
                else -> "严重污染 🟤"
            }
            "\n🏭 空气质量: AQI $aqi ($level)"
        } else ""

        return """
🌤️ 天气数据详情
${"=".repeat(40)}
📅 日期 (date): ${date ?: "无数据"}
🔥 最高温度 (highTemp): ${if (highTemp > 0) "${highTemp}°C" else "无数据"}
❄️ 最低温度 (lowTemp): ${if (lowTemp > 0) "${lowTemp}°C" else "无数据"}
🌅 上午天气 (amCondition): ${amCondition ?: "无数据"}
🌇 下午天气 (pmCondition): ${pmCondition ?: "无数据"}
💨 风力风向 (wind): ${wind ?: "无数据"}
🏭 空气质量指数 (aqi): ${if (aqi > 0) aqi.toString() else "无数据"}
🗺️ 地区ID (areaId): ${areaId ?: "无数据"}
🏷️ 地区类型 (areaType): ${areaType ?: "无数据"}

📋 数据摘要:
🌡️ 温度范围: $tempRange$weatherDescLine$airQuality
        """.trimIndent()
    }
}

