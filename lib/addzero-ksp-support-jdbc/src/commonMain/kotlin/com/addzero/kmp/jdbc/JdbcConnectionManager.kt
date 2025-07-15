package com.addzero.kmp.jdbc

import java.sql.Connection
import java.sql.DriverManager

/**
 * JDBC 连接管理器
 * 提供数据库连接的创建和管理功能
 */
class JdbcConnectionManager(private val config: JdbcConfig) {
    
    /**
     * JDBC 配置
     */
    data class JdbcConfig(
        val driver: String,
        val url: String,
        val username: String,
        val password: String
    )
    
    /**
     * 使用连接执行操作
     */
    fun <T> withConnection(block: (Connection) -> T): T {
        // 加载驱动
        Class.forName(config.driver)
        
        // 创建连接
        return DriverManager.getConnection(config.url, config.username, config.password).use { connection ->
            block(connection)
        }
    }
}
