package site.addzero.web.infra.exception_advice

import cn.dev33.satoken.exception.DisableServiceException
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.exception.NotRoleException
import site.addzero.enums.ErrorEnum
import site.addzero.web.infra.config.log
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
@Order(2)
class SatokenExceptionAdvice {
    @ExceptionHandler(NotLoginException::class)
    fun handleNotLogin(e: NotLoginException?): ProblemDetail {
        log.error("未登录", e)
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "用户未登录").apply {
            title = "Authentication Required"
            type = URI.create("urn:problem-type:authentication-required")
            setProperty("code", ErrorEnum.USER_NOT_LOGGED_IN.code)
        }
    }

    @ExceptionHandler(NotRoleException::class)
    fun handleNotRole(e: NotRoleException): ProblemDetail {
        log.error("角色校验异常", e)
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "用户权限不足").apply {
            title = "Insufficient Permissions"
            type = URI.create("urn:problem-type:insufficient-permissins")
            setProperty("code", ErrorEnum.UNAUTHORIZED.code)
        }
    }

    @ExceptionHandler(DisableServiceException::class)
    fun handleDisabledException(e: DisableServiceException?): ProblemDetail {
        log.error("账号封禁", e)
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "账号已被封禁").apply {
            title = "Account Disabled"
            type = URI.create("urn:problem-type:account-disabled")
            setProperty("code", ErrorEnum.ACCOUNT_BANNED.code)
        }
    }


}
