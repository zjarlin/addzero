package com.addzero.web.event

import com.addzero.kmp.generated.api.JdbcColumnMetadataApi
import com.addzero.kmp.util.JdbcMetadataExtractor
import com.addzero.web.infra.jimmer.dynamicdatasource.DynamicDatasourceProperties
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class JdbcMetaInitEvent {
    @EventListener(ContextRefreshedEvent::class)
    @Async
    fun initSql(
        dynamicDatasourceProperties: DynamicDatasourceProperties
    ): Unit {
        val primary = dynamicDatasourceProperties.primary

        val config = dynamicDatasourceProperties.datasource[primary]!!
        val jdbcUrl = config.url.split("?")[0]
        val jdbcSchema =jdbcUrl.split("=")[1]
        JdbcMetadataExtractor.extractDatabaseMetadata(JdbcMetadataExtractor
            .JdbcConfig(
                jdbcUrl = jdbcUrl,
                jdbcUsername = config.username,
                jdbcPassword = config.password,
                jdbcSchema = jdbcSchema,
                jdbcDriver = config.driverClassName,
                excludeTables = listOf(),
//                excludeColumns = TODO()
            ))

//        JdbcColumnMetadataApi

    }
}
