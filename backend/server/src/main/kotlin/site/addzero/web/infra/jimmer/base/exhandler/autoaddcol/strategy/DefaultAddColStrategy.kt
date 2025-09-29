package site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.strategy

import site.addzero.web.infra.jimmer.base.exhandler.autoaddcol.AutoAddColStrategy
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.net.URI

@Component
class DefaultAddColStrategy : AutoAddColStrategy {
    override fun canHandle(message: String?, causeMessage: String?): Boolean = true

    override fun handle(message: String?, causeMessage: String?): ProblemDetail? {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, causeMessage ?: "Unknown error").apply {
            title = "Database Error"
            type = URI.create("urn:problem-type:database-error")
            setProperty("message", causeMessage)
        }
    }
}