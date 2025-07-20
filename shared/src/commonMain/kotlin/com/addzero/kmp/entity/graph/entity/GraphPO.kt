package com.addzero.kmp.entity.graph.entity


data class GraphPO(
    /** 节点列表 */
    var nodes: List<Node> = ArrayList(),
    /** 关系列表 */
    var lines: List<Line> = ArrayList(),
    /** 三元组属性列表 */
    var spos: List<SPO> = ArrayList()
)

