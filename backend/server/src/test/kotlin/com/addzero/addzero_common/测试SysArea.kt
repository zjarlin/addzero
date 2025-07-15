package com.addzero.addzero_common

import com.addzero.model.entity.SysArea
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试SysArea(
    val sql: KSqlClient,
) {
    @Test
    fun test() {
        val fetchPage = sql.createQuery(SysArea::class) {
            select(table)
        }.fetchPage(0, 100)
        println(fetchPage)
    }

}


