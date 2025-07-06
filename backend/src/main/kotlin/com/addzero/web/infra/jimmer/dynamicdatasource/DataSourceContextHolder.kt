package com.addzero.web.infra.jimmer.dynamicdatasource

import com.addzero.web.jdbc.metadata.DbEnum
import org.babyfish.jimmer.sql.kt.KSqlClient
import javax.sql.DataSource

/**
 * 数据源上下文持有者，用于线程安全地管理当前线程的数据源信息。
 */
object DataSourceContextHolder {

    // 定义一个数据类，存储当前线程的数据源上下文
    data class DataSourceContext(
        val key: String,               // 数据源标识
        val dataSource: DataSource,    // 数据源对象
        val kSqlClient: KSqlClient     // KSqlClient 实例
    )

    // 使用 ThreadLocal 存储每个线程的数据源上下文
    private val contextHolder = ThreadLocal<DataSourceContext>()

    /**
     * 设置数据源上下文
     */
    fun setContext(key: String, dataSource: DataSource, kSqlClient: KSqlClient) {
        contextHolder.set(DataSourceContext(key, dataSource, kSqlClient))
    }

    /**
     * 获取当前线程的数据源上下文
     */
    fun getContext(): DataSourceContext? {
        return contextHolder.get()
    }

    /**
     * 获取当前线程的 DataSource
     */
    fun getDataSource(): DataSource? {
        return contextHolder.get()?.dataSource
    }

    /**
     * 获取当前线程的 KSqlClient
     */
    fun getKSqlClient(): KSqlClient {
        return contextHolder.get()?.kSqlClient!!
    }

    /**
     * 获取当前线程的 key
     */
    fun getKey(): String {
        return contextHolder.get()?.key ?: DbEnum.POSTGRESQL.key
    }

    /**
     * 清除当前线程的数据源上下文
     */
    fun clearContext() {
        contextHolder.remove()
    }
}
