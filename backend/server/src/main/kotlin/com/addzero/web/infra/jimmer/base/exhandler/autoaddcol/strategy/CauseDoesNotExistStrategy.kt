package com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import cn.hutool.core.util.StrUtil
import com.addzero.common.kt_util.removeBlankOrQuotation
import com.addzero.kmp.entity.Res
import com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import com.addzero.web.infra.jimmer.base.exhandler.fixCol
import org.springframework.stereotype.Component

@Component
class CauseDoesNotExistStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean {
        return causeMessage?.contains("does not exist") == true
    }

    override fun handle(message: String?, causeMessage: String?): Any? {
        val columnName =
            StrUtil.subBetween(causeMessage, "was aborted: ERROR: column \"", " of relation ")?.removeBlankOrQuotation()
        val tableName = StrUtil.subBetween(causeMessage, "of relation \"", " does not exist")?.removeBlankOrQuotation()
        val fixCol = fixCol(tableName, columnName)
       return  Res.success(fixCol)
    }
}
