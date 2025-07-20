package com.addzero.model.entity

import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Table

/**
 * 系统天气数据表
 *
 * 对应数据库表: sys_weather
 * 自动生成的代码，请勿手动修改
 */
@Entity
@Table(name = "sys_weather")
interface SysWeather : BaseEntity {

    /**
     * 日期
     */
    @Column(name = "date")
    val date: java.time.LocalDate

    /**
     * 最高温度 (摄氏度)
     */
    @Column(name = "high_temp")
    val highTemp: Long?

    /**
     * 最低温度 (摄氏度)
     */
    @Column(name = "low_temp")
    val lowTemp: Long?

    /**
     * 上午天气状况
     */
    @Column(name = "am_condition")
    val amCondition: String?

    /**
     * 下午天气状况
     */
    @Column(name = "pm_condition")
    val pmCondition: String?

    /**
     * 风力风向信息
     */
    @Column(name = "wind")
    val wind: String?

    /**
     * 空气质量指数
     */
    @Column(name = "aqi")
    val aqi: Long?

    /**
     * 地区ID
     */
    @Column(name = "area_id")
    val areaId: String?

    /**
     * 地区类型
     */
    @Column(name = "area_type")
    val areaType: String?

    /**
     * 星期信息 (格式: YYYY-MM-DD 周X)
     */
    @Column(name = "week")
    val week: String?
}