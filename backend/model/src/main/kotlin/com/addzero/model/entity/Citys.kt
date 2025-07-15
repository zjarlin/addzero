package com.addzero.model.entity

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table

/**
 * 🏙️ 城市信息实体
 *
 * 存储全国城市的基本信息，包括城市名称、拼音、行政区划等数据。
 * 用于地理位置选择、天气查询、地址管理等功能模块。
 *
 * @property id 城市唯一标识符 - 主键，系统内部使用的城市ID
 * @property areaId 地区ID - 外部系统（如天气API）使用的地区标识
 * @property pinyin 完整拼音 - 城市名称的完整拼音表示，用于搜索和排序
 * @property py 拼音简写 - 城市名称拼音的首字母缩写，用于快速检索
 * @property areaName 地区名称 - 具体的地区或区县名称
 * @property cityName 城市名称 - 所属城市的名称
 * @property provinceName 省份名称 - 所属省份或直辖市的名称
 *
 * @since 1.0.0
 * @author AddZero Team
 *
 */
@Entity
@Table(name = "citys")
interface Citys {

    /**
     * 🆔 城市唯一标识符
     *
     * 系统内部使用的主键ID，用于唯一标识每个城市记录。
     * 对应数据库表中的 _id 字段。
     */
    @Id
    val id: Int

    /**
     * 🗺️ 地区ID
     *
     * 外部系统（如天气API、地图API等）使用的地区标识符。
     * 用于与第三方服务进行数据交互和关联。
     *
     * 示例值：
     * - "57073" (洛阳)
     * - "54511" (北京)
     * - "58367" (上海)
     */
    val areaId: String

    /**
     * 🔤 完整拼音
     *
     * 城市名称的完整拼音表示，用于：
     * - 拼音搜索功能
     * - 城市列表排序
     * - 输入法联想
     *
     * 示例值：
     * - "luoyang" (洛阳)
     * - "beijing" (北京)
     * - "shanghai" (上海)
     */
    val pinyin: String?

    /**
     * 🅰️ 拼音简写
     *
     * 城市名称拼音的首字母缩写，用于：
     * - 快速检索和过滤
     * - 城市选择器的字母索引
     * - 移动端快速定位
     *
     * 示例值：
     * - "ly" (洛阳)
     * - "bj" (北京)
     * - "sh" (上海)
     */
    val py: String?

    /**
     * 🏘️ 地区名称
     *
     * 具体的地区、区县或城市名称，是最精确的地理位置描述。
     * 通常用于地址显示和精确定位。
     *
     * 示例值：
     * - "洛阳" (河南省洛阳市)
     * - "朝阳区" (北京市朝阳区)
     * - "浦东新区" (上海市浦东新区)
     */
    val areaName: String?

    /**
     * 🏙️ 城市名称
     *
     * 所属城市的完整名称，包含"市"字后缀。
     * 用于行政区划层级显示和城市级别的数据统计。
     *
     * 示例值：
     * - "洛阳市"
     * - "北京市"
     * - "上海市"
     */

    val cityName: String?

    /**
     * 🗾 省份名称
     *
     * 所属省份、直辖市或自治区的名称。
     * 用于省级行政区划显示和地理位置的层级结构。
     *
     * 示例值：
     * - "河南省"
     * - "北京市" (直辖市)
     * - "上海市" (直辖市)
     * - "广西壮族自治区"
     */

    val provinceName: String?
}
