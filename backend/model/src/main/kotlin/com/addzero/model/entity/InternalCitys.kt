package com.addzero.model.entity

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table

/**
 * 🌍 内部城市信息实体
 *
 * 存储全球城市的国际化信息，包括城市的中英文名称、所属国家、大洲等数据。
 * 主要用于国际化应用、全球天气服务、多语言城市选择等功能模块。
 *
 * 与 `Citys` 实体的区别：
 * - `Citys`: 主要存储国内城市的详细行政区划信息
 * - `InternalCitys`: 主要存储全球城市的国际化基础信息
 *
 * @property id 城市唯一标识符 - 主键，系统内部使用的城市ID
 * @property cityId 城市标识码 - 外部系统或国际标准使用的城市代码
 * @property cityName 城市中文名称 - 城市的中文显示名称
 * @property countryName 国家名称 - 城市所属国家的中文名称
 * @property continents 所属大洲 - 城市所在的大洲名称
 * @property english 英文名称 - 城市的英文名称，用于国际化显示
 * @property pinyin 拼音 - 中文城市名称的拼音表示，用于搜索和排序
 *
 * @since 1.0.0
 * @author AddZero Team
 *
 * @sample
 * ```kotlin
 * val city = InternalCitys {
 *     cityId = "CN_BJ_001"
 *     cityName = "北京"
 *     countryName = "中国"
 *     continents = "亚洲"
 *     english = "Beijing"
 *     pinyin = "beijing"
 * }
 * ```
 */
@Entity
@Table(name = "internal_citys")
interface InternalCitys {

    /**
     * 🆔 城市唯一标识符
     *
     * 系统内部使用的主键ID，用于唯一标识每个城市记录。
     * 对应数据库表中的 _id 字段。
     */
    @Id
    @Column(name = "_id")
    val id: Int

    /**
     * 🏷️ 城市标识码
     *
     * 外部系统或国际标准使用的城市代码，可能包含：
     * - ISO 城市代码
     * - 天气API城市代码
     * - 时区标识符
     * - 自定义城市编码
     *
     * 示例值：
     * - "CN_BJ_001" (北京)
     * - "US_NY_001" (纽约)
     * - "JP_TK_001" (东京)
     * - "GB_LN_001" (伦敦)
     */

    @Column(name = "cityId")
    val cityId: String?

    /**
     * 🏙️ 城市中文名称
     *
     * 城市的中文显示名称，用于：
     * - 中文界面显示
     * - 中文搜索功能
     * - 本地化用户体验
     *
     * 示例值：
     * - "北京" (中国首都)
     * - "纽约" (美国城市)
     * - "东京" (日本首都)
     * - "伦敦" (英国首都)
     */

    @Column(name = "cityName")
    val cityName: String?

    /**
     * 🌏 国家名称
     *
     * 城市所属国家的中文名称，用于：
     * - 地理位置层级显示
     * - 按国家分组查询
     * - 国际化地址格式
     *
     * 示例值：
     * - "中国"
     * - "美国"
     * - "日本"
     * - "英国"
     * - "法国"
     */

    @Column(name = "countryName")
    val countryName: String?

    /**
     * 🌍 所属大洲
     *
     * 城市所在的大洲名称，用于：
     * - 全球地理位置分类
     * - 时区计算辅助
     * - 地理统计分析
     *
     * 标准大洲名称：
     * - "亚洲" (Asia)
     * - "欧洲" (Europe)
     * - "北美洲" (North America)
     * - "南美洲" (South America)
     * - "非洲" (Africa)
     * - "大洋洲" (Oceania)
     * - "南极洲" (Antarctica)
     */

    val continents: String?

    /**
     * 🔤 英文名称
     *
     * 城市的英文名称，用于：
     * - 国际化界面显示
     * - 英文搜索功能
     * - API数据交换
     * - 多语言支持
     *
     * 示例值：
     * - "Beijing" (北京)
     * - "New York" (纽约)
     * - "Tokyo" (东京)
     * - "London" (伦敦)
     * - "Paris" (巴黎)
     */

    val english: String?

    /**
     * 🔤 拼音
     *
     * 中文城市名称的拼音表示，用于：
     * - 拼音搜索功能
     * - 城市列表排序
     * - 输入法联想
     * - 音译标准化
     *
     * 示例值：
     * - "beijing" (北京)
     * - "shanghai" (上海)
     * - "guangzhou" (广州)
     * - "shenzhen" (深圳)
     *
     * 注意：对于非中文城市，此字段可能为空或包含音译拼音
     */

    val pinyin: String?

    companion object {
        val areaType = "1"
    }


}