package com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import com.addzero.common.util.metainfo.MetaInfoUtils.extractTableName
import com.addzero.kmp.entity.Res
import com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import com.addzero.web.infra.jimmer.base.exhandler.extractColumnNames
import com.addzero.web.infra.jimmer.base.exhandler.fixCol
import org.springframework.stereotype.Component

@Component
class MessageDoesNotExistStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean? {
        return message?.contains("does not exist")
    }

    override fun handle(message: String?, causeMessage: String?): Any? {
        val tableName = extractTableName(message)
        val columnName = extractColumnNames(causeMessage)
        val fixCol = fixCol(tableName, columnName)
        return Res.success(fixCol)
    }
}
