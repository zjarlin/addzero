package com.addzero.web.infra.jimmer

import org.babyfish.jimmer.sql.meta.DatabaseNamingStrategy
import org.babyfish.jimmer.sql.runtime.DefaultDatabaseNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JimmerConfig {
    @Bean
    open fun databaseNamingStrategy(): DatabaseNamingStrategy {
        return DefaultDatabaseNamingStrategy.LOWER_CASE
    }
}