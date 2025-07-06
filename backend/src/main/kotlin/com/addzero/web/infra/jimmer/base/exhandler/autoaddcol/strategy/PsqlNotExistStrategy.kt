//package com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy
//
//import cn.hutool.core.util.StrUtil
//import com.addzero.common.kt_util.isBlank
//import com.addzero.common.kt_util.removeBlankOrQuotation
//import com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
//import com.addzero.web.infra.jimmer.base.exhandler.fixCol
//import org.springframework.stereotype.Component
//import success
//
//@Component
//class PsqlNotExistStrategy : AutoAddColStrategy {
//    override fun canHandle(message: String?, causeMessage: String?): Boolean {
//        fun isValidErrorMessage(errorMessage: String?): Boolean {
//            if (errorMessage.isBlank()) {
//                return false
//            }
//            // 定义正则表达式
//            val regex = Regex("column\\s+tb_\\d+_\\.\\w+\\s+does\\s+not\\s+exist")
//
//            // 检查是否匹配
//            return regex.matches(message!!)
//        }
//        val validErrorMessage = isValidErrorMessage(message)
//        return validErrorMessage
//    }
//
//    override fun handle(message: String?, causeMessage: String?): Any? {
//        val columnName = StrUtil.subBetween(causeMessage, "was aborted: ERROR: column \"", " of relation ")?.removeBlankOrQuotation()
//        val tableName = StrUtil.subBetween(causeMessage, "of relation \"", " does not exist")?.removeBlankOrQuotation()
//        val fixCol = fixCol(tableName, columnName)
//        return fixCol.success()
//    }
//}