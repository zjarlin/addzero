package com.addzero.ai.config

import org.slf4j.LoggerFactory
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration

/**
 * MCP工具配置类
 * 在应用启动时打印已注册的工具信息
 */
@Configuration
class McpToolsConfiguration(
   private val methodToolCallbackProvider: MethodToolCallbackProvider
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

        val tools = methodToolCallbackProvider.toolCallbacks

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
                val toolDefinition = tool.toolDefinition
                val name = toolDefinition.name()
                val description = toolDefinition.description()
                val inputSchema = toolDefinition.inputSchema()
                logger.info("${index + 1}. 工具名称: ${name} 描述: ${description}")
            }
        }
        logger.info("=".repeat(80))
        logger.info("MCP工具可通过以下方式使用：")
        logger.info("1. 在AI聊天中自然语言调用")
        logger.info("2. 通过 /api/mcp/test-tools 查看所有工具")
        logger.info("3. 通过 /ai/ask 进行AI对话")
        logger.info("=".repeat(80))
    }
}
