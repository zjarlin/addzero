package site.addzero.web.infra.jimmer.base.exhandler.autoaddcol

import org.springframework.http.ProblemDetail

interface AutoAddColStrategy {
    fun canHandle(message: String?, causeMessage: String?): Boolean?
    fun handle(message: String?, causeMessage: String?): ProblemDetail?
}