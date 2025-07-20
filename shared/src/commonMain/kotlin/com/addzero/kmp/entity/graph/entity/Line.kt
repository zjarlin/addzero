package com.addzero.kmp.entity.graph.entity

data class Line(
    /** from node id */
    var from: String,
    /** to node id */
    var to: String,
    /** 类型或关系描述 */
    var relation: String?
)
