package com.addzero.web.event

import com.addzero.common.consts.sql
import com.addzero.kmp.util.JdbcMetadataExtractor
import com.addzero.model.entity.JdbcColumnMetadata
import com.addzero.model.entity.JdbcTableMetadata
import com.addzero.web.infra.jimmer.dynamicdatasource.DynamicDatasourceProperties
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class JdbcMetaInitEvent(
    private val dynamicDatasourceProperties: DynamicDatasourceProperties


) {
    @EventListener(ContextRefreshedEvent::class)
    @Async
    fun initSql( ) {
        val primary = dynamicDatasourceProperties.primary

        val config = dynamicDatasourceProperties.datasource[primary]!!
        val jdbcUrl = config.url.split("?").first()
        val jdbcSchema = config.url.split("?").last().split("=").last()
        val extractDatabaseMetadata = JdbcMetadataExtractor.extractDatabaseMetadata(
            JdbcMetadataExtractor
                .JdbcConfig(
                    jdbcUrl = jdbcUrl,
                    jdbcUsername = config.username,
                    jdbcPassword = config.password,
                    jdbcSchema = jdbcSchema,
                    jdbcDriver = config.driverClassName,
                    excludeTables = config.excludeTables.split(",")
                )
        )

//        JdbcColumnMetadataApi
        val map = extractDatabaseMetadata.map {
            JdbcTableMetadata {
                tableName = it.tableName
                schemaName = it.schema
                tableType = it.tableType
                remarks = it.remarks
                columns = it.columns.map { columnMeta ->
                    JdbcColumnMetadata {
                        tableName = columnMeta.tableName
                        columnName = columnMeta.columnName
                        jdbcType = columnMeta.jdbcType.toLong()
                        columnType = columnMeta.columnType
                        columnLength = columnMeta.columnLength.toLong()
                        nullableBoolean = columnMeta.nullable
                        nullableFlag = columnMeta.nullableFlag
                        remarks = columnMeta.remarks
                        defaultValue = columnMeta.defaultValue
                        primaryKeyFlag = columnMeta.isPrimaryKey.toString()
                    }
                }
            }
        }
        sql.saveEntities(map)
    }
}
