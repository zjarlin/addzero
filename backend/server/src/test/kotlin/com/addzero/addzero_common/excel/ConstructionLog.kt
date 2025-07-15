package com.addzero.addzero_common.excel


data class ConstructionLog(
    val 工程名称: String,
    val 日期: String,
    val 气象数据: WeatherData,
    val 出勤人数: Attendance,
    val 机械设备: Equipment,
    val 施工内容: List<String>,
    val 质量验收: String?,
    val 材料进场: String,
    val 会议纪要: String?,
    val 质量安全巡检: String,
    val 其他: String?,
    val 工程负责人: String,
    val 记录人: String?
)

data class WeatherData(
    val 白天: WeatherInfo,
    val 夜间: WeatherInfo
)

data class WeatherInfo(
    val 天气状况: String?,
    val 风力: String,
    val 风向: String,
    val 温度: Int
)

data class Attendance(
    val 管理人员: String,
    val 测量工: String?,
    val 普工: String
)

data class Equipment(
    val 铲车: String,
    val 挖掘机: String?
)