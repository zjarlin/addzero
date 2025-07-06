package com.addzero.web.infra.jimmer.base.exhandler

import com.addzero.kmp.entity.Res
import com.addzero.web.infra.exception_advice.buidResponseEntity
import org.babyfish.jimmer.sql.exception.SaveException
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(1) // 优先级最高
class JimmerExceptionAdvice(

    private val exceptionStrategyExecutor: ExceptionStrategyExecutor
) {
    @ExceptionHandler(SaveException.NeitherIdNorKey::class)
    fun handleException(e: SaveException.NeitherIdNorKey): Any? {
        e.printStackTrace()
        val s =
            """请在实体字段上加@Key,不能接受既没有id也没有键的实体。有三种方法可以解决这个问题：1。为保存对象指定id属性“id”；2.使用注解“org.babyfish.jimmer.sql.Key”修饰实体类型中的一些标量或外键属性，或调用save命令的“setKeyProps”，指定的键属性，最后指定保存对象的键属性值；3.将save命令的聚合根保存模式指定为“INSERT_ONLY”（函数已更改）、“INSERT_IF_ABSENT”（函数更改）或“NON_IDEMPOTENT_UPSERT”"""
        return Res.fail(s).buidResponseEntity()
    }


}
