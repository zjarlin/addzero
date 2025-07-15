package com.addzero.web.infra.jimmer.dynamicdatasource

import com.addzero.web.infra.spring.SprCtxUtil.applicationContext
import com.addzero.web.jdbc.metadata.DbEnum
import com.zaxxer.hikari.HikariDataSource
import org.babyfish.jimmer.spring.SqlClients
import org.babyfish.jimmer.spring.transaction.JimmerTransactionManager
import org.babyfish.jimmer.sql.dialect.*
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import javax.sql.DataSource

@Configuration
open class DynamicDataSourceConfig(
     private val dynamicDatasourceProperties: DynamicDatasourceProperties,
)  {

//    @Bean
//    open fun dynamicTm(
//        ctx: ApplicationContext,
//        @Qualifier("dynamicDataSource") dataSource: DataSource,
//    ): PlatformTransactionManager {
//        val kSqlClient = SqlClients.kotlin(ctx, dataSource) {}
//        return JimmerTransactionManager(kSqlClient)
//    }


    @Bean
    open fun dynamicDataSource(): DataSource {
        val routingDataSource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any {
// 动态切换数据源
                val primary = dynamicDatasourceProperties.primary
                val dataSourceKey = DataSourceContextHolder.getKey()
                return dataSourceKey ?: primary
            }
        }
        val targetDataSources: MutableMap<Any, Any> = HashMap()
        dynamicDatasourceProperties.datasource.forEach { (key, config) ->
            val dataSource = HikariDataSource().apply {
                driverClassName = config.driverClassName
                jdbcUrl = config.url
                username = config.username
                password = config.password
            }
            targetDataSources[key] = dataSource
//            regisBean(dataSource, key)
        }
        routingDataSource.setTargetDataSources(targetDataSources)
        targetDataSources[dynamicDatasourceProperties.primary]?.let { routingDataSource.setDefaultTargetDataSource(it) }
        return routingDataSource
    }

    companion object {
        fun registerTm(properties: DynamicDatasourceProperties, registry: BeanDefinitionRegistry) {
            val primary = properties.primary

            properties.datasource.forEach { (key, value) ->
                val ds = HikariDataSource()
                ds.jdbcUrl = value.url
                ds.username = value.username
                ds.password = value.password

                val kotlin = SqlClients.kotlin(applicationContext, ds)
                val jimmerTransactionManager = JimmerTransactionManager(kotlin)
                val tmDef = RootBeanDefinition(JimmerTransactionManager::class.java) { jimmerTransactionManager }

                if (key == primary) {
                    tmDef.isPrimary = true
                }
                registry.registerBeanDefinition("${key}Tm", tmDef)
            }
        }


        fun getDialectByDatasourceKey(dataSourceKey: String): Dialect {
            val oracleDialect = OracleDialect()
            val mySqlDialect = MySqlDialect()
            val mySql5Dialect = MySql5Dialect()
            val h2Dialect = H2Dialect()
            val postgresDialect = PostgresDialect()
            val sqlServerDialect = SqlServerDialect()
            val tiDBDialect = TiDBDialect()
            val dialect: Dialect = when (dataSourceKey) {
                DbEnum.DM.key -> oracleDialect
                DbEnum.MYSQL.key -> mySqlDialect
                DbEnum.MYSQL5.key -> mySql5Dialect
                DbEnum.H2.key -> h2Dialect
                DbEnum.ORACLE.key -> oracleDialect
                DbEnum.POSTGRESQL.key -> postgresDialect
                DbEnum.TiDB.key -> tiDBDialect
                DbEnum.SQLSERVER.key -> sqlServerDialect
                else -> oracleDialect
            }
            return dialect
        }
    }

}
