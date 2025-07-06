package com.addzero.web.infra.jimmer.dynamicdatasource

import cn.hutool.extra.spring.SpringUtil
import com.addzero.web.infra.jimmer.dynamicdatasource.DynamicDataSourceConfig.Companion.getDialectByDatasourceKey
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.babyfish.jimmer.spring.SqlClients
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Aspect
@Component
class DataSourceAspect {
    @Around("@annotation(DS) || @within(DS)")
    fun switchDataSource(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        val annotation = joinPoint.target.javaClass.getAnnotation(DS::class.java)
        val annotation1 = method.getAnnotation(DS::class.java)
        val DS = annotation1 ?: annotation
        // 切换数据源
        DS?.let {
            val key = it.value
            val dataSource = SpringUtil.getBean(DataSource::class.java)
            val dialectByDatasourceKey = getDialectByDatasourceKey(key)
            val kSqlClient = SqlClients.kotlin(SpringUtil.getApplicationContext(), dataSource) {
                setDialect(dialectByDatasourceKey)
            }
            DataSourceContextHolder.setContext(key, dataSource, kSqlClient)
        }
        return try {
            // 执行方法
            joinPoint.proceed()
        } finally {
            // 方法执行后清除数据源
            DataSourceContextHolder.clearContext()
        }
    }

}