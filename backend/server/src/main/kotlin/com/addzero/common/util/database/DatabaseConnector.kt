package com.addzero.common.util.database

import com.addzero.kmp.anno.Shit
import com.addzero.common.kt_util.makeSurroundWith
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.nio.file.Path
import javax.sql.DataSource
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.pathString


private val DEFAULT_DATA_DIR = Path("./data")
private const val DATA_FILE_NAME = "bonus.d"
@Shit
fun main() {
    val string = "springcloud/openfegin/eureka/jakarta.validation/mapstruct/hutool/guava/Apache Commons Lang3 \n" +
            "\n"
    val joinToString = string.split("/").map {
        it.makeSurroundWith("`")
    }.joinToString("  ")
    println(joinToString)


//    val property = System.getProperty("user.home")
//    val property1 = System.getProperty("user.dir")
}
fun connectDatabaseOperator(dataDir: Path = DEFAULT_DATA_DIR, schemaName: String): DataSource {
    // val dialect = H2Dialect()

    val jdbcUrl =
        """jdbc:h2:${(dataDir / DATA_FILE_NAME).pathString};DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;TRACE_LEVEL_FILE=3;AUTO_RECONNECT=TRUE;"""

    // val connection

    val config = hikariConfig {
        this.jdbcUrl = jdbcUrl
        driverClassName = "org.h2.Driver"
        // this.threadFactory
        connectionInitSql = "CREATE SCHEMA IF NOT EXISTS $schemaName; SET SCHEMA $schemaName"
        minimumIdle = 1
        maximumPoolSize = 1
    }

    val source = HikariDataSource(config)
    val connection = source.connection
    val metaData = connection.metaData

    return source
}

private inline fun hikariConfig(block: HikariConfig.() -> Unit): HikariConfig = HikariConfig().apply {
    block()
}


