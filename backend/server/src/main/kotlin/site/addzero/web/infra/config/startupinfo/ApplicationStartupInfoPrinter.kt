package site.addzero.web.infra.config.startupinfo

import cn.hutool.core.net.NetUtil
import cn.hutool.core.text.CharSequenceUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component


@Component
@Order(value = Int.MAX_VALUE)
class ApplicationStartupInfoPrinter(
    private val environment: Environment,
    private val startupInfoProperties: StartupInfoProperties
) : CommandLineRunner {

    @Value("\${spring.application.name:Application}")
    private lateinit var applicationName: String

    override fun run(vararg args: String?) {
        if (!startupInfoProperties.enabled) return

        try {
            val ip = NetUtil.getLocalhostStr()
            val port = environment.getProperty("server.port") ?: "8080"
            val contextPath = environment.getProperty("server.servlet.context-path") ?: ""
            val path = CharSequenceUtil.removeSuffix(contextPath, "/")

            val uiPath = when (startupInfoProperties.docType) {
                DocType.JIMMER -> "/openapi.html"
                DocType.SWAGGER -> "/swagger-ui.html"
                DocType.KNIFE4J -> "/doc.html"
            }

            val tsZipPath = when (startupInfoProperties.docType) {
                DocType.JIMMER -> "/ts.zip"
                else -> ""
            }

            val docTypeName = when (startupInfoProperties.docType) {
                DocType.JIMMER -> "Jimmer"
                DocType.SWAGGER -> "Swagger"
                DocType.KNIFE4J -> "Knife4j"
            }

            // 只有Jimmer类型才显示API代理和Typescript路径
            val jimmerSpecificLines = if (startupInfoProperties.docType == DocType.JIMMER) {
                "│ API Proxy:   http://localhost:$port$path/ui${" ".repeat((37 - path.length).coerceAtLeast(0))}│\n" +
                        "│ Typescript:  http://localhost:$port$path$tsZipPath${
                            " ".repeat(
                                (37 - tsZipPath.length).coerceAtLeast(
                                    0
                                )
                            )
                        }│\n"
            } else {
                ""
            }

            val info = """
                
                ┌─────────────────────────────────────────────────────────────────┐
                │                   Application Startup Info                      │
                ├─────────────────────────────────────────────────────────────────┤
                │ Application: ${applicationName.padEnd(47)}│
                │ Doc Type:    ${docTypeName.padEnd(47)}│
                ├─────────────────────────────────────────────────────────────────┤
                │ API Docs:    http://$ip:$port$path$uiPath${" ".repeat((38 - uiPath.length).coerceAtLeast(0))}│
                $jimmerSpecificLines└─────────────────────────────────────────────────────────────────┘
                
            """.trimIndent()

            println(info)
        } catch (e: Exception) {
            println("Failed to print application startup info: ${e.message}")
        }
    }
}
