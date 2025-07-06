package com.addzero.kmp.entity.graph.entity

data class Node(
    /** 实体id */
    var nodeId: String,
    /** 实体名称 */
    var nodeName: String,
    /** 实体上下文类型 */
    var nodeType: String
)
