package site.addzero.web.infra.config.startupinfo

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "app.startup.info",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableConfigurationProperties(StartupInfoProperties::class)
class StartupInfoAutoConfiguration
