package site.addzero.web.infra.exception_advice

import site.addzero.enums.ErrorEnum
import site.addzero.exp.BizException
import site.addzero.web.infra.config.log
import jakarta.validation.ConstraintViolationException
import org.babyfish.jimmer.sql.exception.ExecutionException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.URI

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BizException::class)
    fun handleBusinessException(
        exception: BizException,
//        request: WebRequest,
//        methodhandler: MethodHandle
    ): ProblemDetail {
        val buildMessage = exception.buildMessage()
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, buildMessage).apply {
            title = "Business Exception"
            type = URI.create("urn:problem-type:business-exception")
            setProperty("code", exception.value)
        }
    }


    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidateException(e: ConstraintViolationException): ProblemDetail {
        log.error("校验异常", e)
        val constraintViolations = ArrayList(e.constraintViolations)
        val joinToString = constraintViolations.joinToString(System.lineSeparator()) {
            it.message
        }
        val code = ErrorEnum.INVALID_PARAMETER.code
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, joinToString).apply {
            title = "Validation Exception"
            type = URI.create("urn:problem-type:validation-exception")
            setProperty("code", code)
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidateExceptionForSpring(e: MethodArgumentNotValidException): ProblemDetail {
        log.error("校验异常", e)
        val code = ErrorEnum.INVALID_PARAMETER.code
        val allErrors = e.bindingResult.allErrors
        val joinToString = allErrors.joinToString(System.lineSeparator()) { it.defaultMessage.toString() }
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, joinToString).apply {
            title = "Validation Exception"
            type = URI.create("urn:problem-type:validation-exception")
            setProperty("code", code)
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ProblemDetail {
//        e.printStackTrace()
        log.error("系统异常", e)
//        val message = e?.message
        val buildMessage = e.buildMessage()
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, buildMessage).apply {
            title = "Internal Server Error"
            type = URI.create("urn:problem-type:internal-server-error")
        }
    }


    @ExceptionHandler(ExecutionException::class)
    fun handleException(e: ExecutionException): ProblemDetail {
        e.printStackTrace()
        log.error("系统异常", e)
//        val message = e?.message
        val buildMessage = e.buildMessage()
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, buildMessage).apply {
            title = "Database Execution Error"
            type = URI.create("urn:problem-type:database-execution-error")
        }
    }


}