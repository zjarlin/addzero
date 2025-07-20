package com.addzero.ai.mcp

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import java.lang.management.ManagementFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 系统信息服务 - MCP工具示例
 * 使用@Component注解来测试自动注册功能对不同注解的支持
 */
@Service
class SystemInfoService {

    @Tool(description = "获取系统基本信息")
    fun getSystemInfo(): String {
        val runtime = Runtime.getRuntime()
        val osBean = ManagementFactory.getOperatingSystemMXBean()

        return """
            系统信息:
            - 操作系统: ${System.getProperty("os.name")} ${System.getProperty("os.version")}
            - Java版本: ${System.getProperty("java.version")}
            - 可用处理器: ${runtime.availableProcessors()}
            - 系统架构: ${System.getProperty("os.arch")}
            - 用户名: ${System.getProperty("user.name")}
            - 用户目录: ${System.getProperty("user.home")}
            - 当前时间: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
        """.trimIndent()
    }

    @Tool(description = "获取系统环境变量")
    fun getEnvironmentVariable(variableName: String): String {
        val value = System.getenv(variableName)
        return if (value != null) {
            "环境变量 $variableName = $value"
        } else {
            "环境变量 $variableName 不存在"
        }
    }

    @Tool(description = "获取系统属性")
    fun getSystemProperty(propertyName: String): String {
        val value = System.getProperty(propertyName)
        return if (value != null) {
            "系统属性 $propertyName = $value"
        } else {
            "系统属性 $propertyName 不存在"
        }
    }

    private fun formatBytes(bytes: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var unitIndex = 0

        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }

        return String.format("%.2f %s", size, units[unitIndex])
    }
}
