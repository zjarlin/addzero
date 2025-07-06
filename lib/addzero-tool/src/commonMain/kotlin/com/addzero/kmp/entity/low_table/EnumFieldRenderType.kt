package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable

/**
 * 渲染字段类型
 * @author zjarlin
 * @date 2025/04/18
 * @constructor 创建[EnumFieldRenderType]
 * @param [typeName]
 * @param [description]
 */
@Serializable
enum class EnumFieldRenderType(val typeName: String, val description: String) {
    // 文本类型
    TEXT("文本框", "基础文本输入"),
    PASSWORD("密码", "密码输入框"),
    EMAIL("邮箱", "邮箱输入框"),
    LONG_TEXT("长文本", "多行文本输入"),
    AUTO_COMPLETE("自动建议", "带有建议的输入框"),
    RICH_TEXT("富文本", "富文本编辑器"),

    // 数字类型
    NUMBER("数字", "数字输入框"),
    CURRENCY("货币", "货币输入框"),
    PERCENTAGE("百分比", "百分比输入框"),
    PROGRESS("进度条", "进度条"),
    RATING("评分", "星级评分"),

    // 日期类型
    DATE("日期", "日期选择器"),
    TIME("时间", "时间选择器"),
    DATETIME("日期时间", "日期和时间选择器"),

    // 选择类型
    SELECT("下拉选择", "单选下拉框"),
    MULTI_SELECT("多选", "多选下拉框"),
    RADIO("单选按钮", "单选按钮组"),
    CHECKBOX("复选框", "复选框组"),
    SWITCH("开关", "切换开关"),

    // 特殊类型
    FILE("文件上传", "文件上传控件"),
    IMAGE("图片上传", "图片上传控件"),
    COLOR("颜色选择", "颜色选择器"),
    PHONE("电话号码", "电话号码输入框"),
    URL("网址", "URL输入框"),
    SLIDER("滑块", "范围滑块"),
    TAG("标签", "标签输入框"),

    // 自定义类型
    CUSTOM("自定义", "自定义类型");

    companion object {
        fun getByName(name: String): EnumFieldRenderType {
            return entries.find { it.typeName == name } ?: TEXT
        }
    }
}
