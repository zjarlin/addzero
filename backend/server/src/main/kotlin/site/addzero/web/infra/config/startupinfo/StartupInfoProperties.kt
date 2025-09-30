package site.addzero.web.infra.config.startupinfo

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.startup.info")
data class StartupInfoProperties(
    /** 是否启用启动信息打印 */
    val enabled: Boolean = true,

    /** 文档类型 */
    val docType: DocType = DocType.JIMMER
)

enum class DocType {
    /** Jimmer文档 */
    JIMMER,

    /** Swagger文档 */
    SWAGGER,

    /** Knife4j文档 */
    KNIFE4J
}
