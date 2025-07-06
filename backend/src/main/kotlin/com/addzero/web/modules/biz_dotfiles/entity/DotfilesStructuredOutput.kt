//package com.addzero.web.modules.second_brain.dotfiles
//
//
//
///**
// * @author zjarlin
// * @date 2024/10/20
// */
//
//data class DotfilesStructuredOutput(
//    @field:Schema(description = "操作系统 可选值 linux/mac/win") var osType: String="",
//    @field:Schema(description = "系统架构 arm64/x86_64") var osStructure: String="",
//    @field:Schema(description = "定义类型 export/alias/function") var defType: String="",
//    @field:Schema(
//        description = """
//            name的定义:
//            例如：export JAVA_HOME=/usr/local/jdk1.8.0_201
//            则 name=JAVA_HOME, columnValue=/usr/local/jdk1.8.0_201
//           例如： alias ll='ls -alF'
//            则 name=ll,   columnValue='ls -alF'注意我这里有引号你就加上引号和我保持一致双引号同理
//       例如：    function echo_name(){
//       echo 'name'
//
//}
//
//            则 name=echo_name, columnValue=echo 'name', 注意这里的函数体要保持原格式字符串, 换行符美元符号等等特殊字符也要保持原格式
//        """
//
//    )
//    var name: String="",
//    @field:Schema(description = "值的定义参见name的定义") var columnValue: String="",
//    @field:Schema(description = "注释,comment可以为空,你根据我给出的示例自行判断是bash或者powershell语法补充一下注释,不知道的话可以不填") var comment: String="",
//    @field:Schema(
//        description = """状态1=启用0=未启用,默认为1,表示启用
//                   注意:如果有以下注释的情况, 请在是否启用里选择0不启用
//          示例文本---
//               # 注释内容
//           #    alias ls='ls -alF'
//
//                ---
//                name=ls, columnValue='ls -aF', comment=" 注释内容", status=0
//    """
//    ) var status: String="",
//)
