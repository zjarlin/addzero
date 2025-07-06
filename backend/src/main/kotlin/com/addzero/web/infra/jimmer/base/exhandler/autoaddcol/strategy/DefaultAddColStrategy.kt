package com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import com.addzero.kmp.entity.Res
import com.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import org.springframework.stereotype.Component

@Component
class DefaultAddColStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean = true

    override fun handle(message: String?, causeMessage: String?): Any? {
        return Res.fail(causeMessage)
    }
}
