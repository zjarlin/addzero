package com.addzero.web.infra.exception_advice

import com.addzero.kmp.entity.Res
import com.addzero.kmp.enums.ErrorEnum
import com.addzero.kmp.exp.BizException
import com.addzero.web.infra.config.log
import jakarta.validation.ConstraintViolationException
import org.babyfish.jimmer.sql.exception.ExecutionException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BizException::class)
    fun handleBusinessException(
        exception: BizException,
//        request: WebRequest,
//        methodhandler: MethodHandle
    ): Any {
        val buildMessage = exception.buildMessage()
        val fail = Res.fail(buildMessage).buidResponseEntity()
        return fail
    }


    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidateException(e: ConstraintViolationException): Any {
        log.error("校验异常", e)
        val constraintViolations = ArrayList(e.constraintViolations)
        val joinToString = constraintViolations.joinToString(System.lineSeparator()) {
            it.message
        }
        val code = ErrorEnum.INVALID_PARAMETER.code
        val fail = Res.fail(code, joinToString).buidResponseEntity()
        return fail
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidateExceptionForSpring(e: MethodArgumentNotValidException): Any {
        log.error("校验异常", e)
        val code = ErrorEnum.INVALID_PARAMETER.code
        val allErrors = e.bindingResult.allErrors
        val joinToString = allErrors.joinToString(System.lineSeparator()) { it.defaultMessage.toString() }
        val fail = Res.fail(code, joinToString).buidResponseEntity()
        return fail
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Any {
//        e.printStackTrace()
        log.error("系统异常", e)
//        val message = e?.message
        val buildMessage = e.buildMessage()
        val fail = Res.fail(buildMessage).buidResponseEntity()
        return fail
    }


    @ExceptionHandler(ExecutionException::class)
    fun handleException(e: ExecutionException): Any {
        e.printStackTrace()
        log.error("系统异常", e)
//        val message = e?.message
        val buildMessage = e.buildMessage()
        val fail = Res.fail(buildMessage).buidResponseEntity()
        return fail
    }


}

