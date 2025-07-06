package com.addzero.web.infra.exception_advice

import cn.dev33.satoken.exception.DisableServiceException
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.exception.NotRoleException
import com.addzero.kmp.entity.fail
import com.addzero.kmp.enums.ErrorEnum
import com.addzero.web.infra.config.log
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(2)
class SatokenExceptionAdvice() {


    @ExceptionHandler(NotLoginException::class)
    fun handleNotLogin(e: NotLoginException?): Any {
        log.error("未登录", e)
        val body = ErrorEnum.USER_NOT_LOGGED_IN.fail().buidResponseEntity()
        return body
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(NotRoleException::class)
    fun handleNotRole(e: NotRoleException): Any {
        log.error("角色校验异常", e)
        val body = ErrorEnum.UNAUTHORIZED.fail().buidResponseEntity()
        return body
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(DisableServiceException::class)
    fun handleDisabledException(e: DisableServiceException?): Any {
        log.error("账号封禁", e)
        val body = ErrorEnum.ACCOUNT_BANNED.fail().buidResponseEntity()
        return body
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)

    }


}

