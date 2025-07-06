package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable



/**
 * 列元数据
 *
 * @param T 数据类型
 * @param key 列唯一标识符
 * @param title 列标题
 * @param valueGetter 获取值的函数
 * @param formatter 格式化函数，可选
 * @param jdbcType 数据类型，如"string", "number", "boolean", "date"等
 * @param javaType Java类型名称
 * @param widthRatio 列宽度比例，默认为1
 * @param alignment 列对齐方式，默认为左对齐
 * @param sortable 是否可排序，默认为true
 * @param searchable 是否可搜索，默认为true
 * @param visible 是否可见，默认为true
 */
@Serializable
data class StateColumnMetadata(
    // 列键名
    val key: String,
    // 列标题
    val title: String,
    val jdbcType: String = "string",
    val javaType: String = "java.lang.String",
    val widthRatio: Float = 1f,
    val alignment: EnumColumnAlignment = EnumColumnAlignment.CENTER,
    //低代码元数据配置

    // 是否可排序
    val sortable: Boolean = true,
    // 是否可搜索
    val searchable: Boolean = true,

    // 是否在表单中显示
    val showInForm: Boolean = true,
    // 是否在列表中显示
    val showInList: Boolean = true,

    // 是否必填
    val required: Boolean = false,
    // 列宽度
    val width: Int = 100,
    // 默认值
    val defaultValue: String? = null,
    // 校验规则
    val validationRules: List<String> = emptyList(),
    // 字典代码(如果有)
    val dictCode: String? = null
)
