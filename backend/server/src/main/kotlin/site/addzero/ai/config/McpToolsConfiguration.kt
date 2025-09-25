package site.addzero.ai.config

import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.definition.ToolDefinition
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

/**
 * MCP工具配置类
 * 在应用启动时打印已注册的工具信息
 */
@Configuration
@Order(0)
class McpToolsConfiguration(
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(McpToolsConfiguration::class.java)


    override fun run(vararg args: String?) {
        printRegisteredTools()
    }

    /**
     * 打印已注册的工具信息
     */
    private fun printRegisteredTools() {
        logger.info("=".repeat(80))
        logger.info("MCP工具自动注册完成")
        logger.info("=".repeat(80))

        val tools = gettooldef()

        if (tools.isEmpty()) {
            logger.warn("⚠️  未发现任何MCP工具！")
            logger.info("请确保：")
            logger.info("1. 类上有 @Service 或 @Component 注解")
            logger.info("2. 方法上有 @Tool 注解")
            logger.info("3. 类在Spring扫描路径内")
        } else {
            logger.info("✅ 成功注册 ${tools.size} 个MCP工具:")
            logger.info("-".repeat(80))

            tools.forEachIndexed { index, tool ->
                val name = tool.name()
                val description = tool.description()
                val inputSchema = tool.inputSchema()
                logger.info("${index + 1}. 工具名称: ${name} 描述: ${description}")
            }
        }
        logger.info("=".repeat(80))
        logger.info("MCP工具可通过以下方式使用：")
        logger.info("通过 /chat/gettools 查看所有工具")
        logger.info("通过 /chat/ask 进行AI对话")
        logger.info("=".repeat(80))
    }

}
fun gettooldef(): List<ToolDefinition> {
    val methodToolCallbackProvider = SpringUtil.getBean(MethodToolCallbackProvider::class.java)
    val tools = methodToolCallbackProvider.toolCallbacks
    val toolDefinitions = tools.map { it.toolDefinition }
    return toolDefinitions
}