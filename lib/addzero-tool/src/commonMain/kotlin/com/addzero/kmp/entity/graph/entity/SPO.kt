package com.addzero.kmp.entity.graph.entity

/**
 * 表示一个三元组（Subject-Predicate-Object）的实体类。
 *
 * @property subject 主语，表示三元组中的主体部分。
 * @property predicate 谓语，表示三元组中的关系部分。
 * @property `object` 宾语，表示三元组中的客体部分。
 * @property context 上下文，表示三元组的相关上下文信息。
 */
data class SPO(
    /** 表示三元组中的主体部分 */
    var subject: String,
    /** 表示三元组中的谓词部分(可以设计为数据库字段的属性) */
    var predicate: String,
    /** 表示三元组中的客体部分(简单理解为数据库字段的属性值) */
    var `object`: String?,
    /** 表示三元组中的上下文信息 */
    var context: String?
)
