package com.addzero.web.infra.curllog

import com.addzero.common.kt_util.getRestUrl
import com.addzero.web.infra.config.log
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired

@Aspect
@org.springframework.stereotype.Component
class CurlAop (
   private val httpServletRequest: HttpServletRequest,
){
    @Autowired

    @Pointcut("execution(* com.addzero..BaseController.*(..))")
    fun p1() {
    }

    @Pointcut("execution(* com.addzero..*Api*+.*(..))")
    fun p2() {
    }

    @Pointcut("execution(* com.addzero..*Controller*+.*(..))")
    fun p3() {
    }
    @Pointcut("@annotation(CurlLog)")
    fun logCurlAnnotation() {
    }

    @Pointcut("p1() || p2() || p3()||logCurlAnnotation()")
    fun logCurlMethods() {
    }


    @Around("logCurlMethods()")
    fun logBefore(pjp: ProceedingJoinPoint): Any {
        val proceed: Any
        try {
            proceed = pjp.proceed()
        } catch (e: Throwable) {
            if (httpServletRequest != null) {
                val curlCommand = CurlUtil.generateCurlCommand(httpServletRequest, pjp)
                val requestURL: StringBuffer = httpServletRequest.requestURL
                val restUrl = requestURL.toString().getRestUrl()
                log.error("See Error restUrl:{} ", restUrl)
                //                String formattedCurlCommand = formatCurlCommand(curlCommand);
                log.error("See Error Curl Command:{} ", curlCommand)
            }
            throw e
        }
        return proceed
    }

    @Around("logCurlAnnotation()")
    @Throws(Throwable::class)
    fun logWithAnnotation(pjp: ProceedingJoinPoint): Any {
        if (httpServletRequest != null) {
            val curlCommand = CurlUtil.generateCurlCommand(httpServletRequest, pjp)
            //            String formattedCurlCommand = formatCurlCommand(curlCommand);
            log.info("See Curl Command:{} ", curlCommand)
        }
        val proceed: Any = pjp.proceed()
        return proceed
    } // Rest of the code remains the same...
}
