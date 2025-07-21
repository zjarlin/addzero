package com.addzero.util

data class DataSourceConfig(
    val driverClassName: String,
    val url: String,
    val username: String,
    val password: String
)

data class DynamicDataSourceConfig(
    val primary: String,
    val datasource: Map<String, DataSourceConfig>
)

data class SpringConfig(
    val profiles:SpringProfileProperties,
    val datasource: DynamicDataSource
) {
    data class DynamicDataSource(
        val dynamic: DynamicDataSourceConfig
    )
}

data class AppConfig(
    val spring: SpringConfig
)

data class SpringProfileProperties(
    val active: String ,
)
