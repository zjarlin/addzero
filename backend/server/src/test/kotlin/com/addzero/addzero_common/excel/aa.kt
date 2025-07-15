package com.addzero.addzero_common.excel

import com.addzero.kmp.anno.NoArg

//@Serializable
@NoArg
data class Dasda(
    val 报告编号: String,
    val 当前数据状态: String,
    val 样品_项目名称: String,
    val 项目名称: String,
    val 规格型号: String,
    val 委托日期: String,
    val 工程名称: String,
    val 工程部位: String,
    val 检测参数: String,
)
