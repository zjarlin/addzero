package com.addzero.addzero_common

import com.addzero.model.entity.SysUser
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试排序(
    val sql: KSqlClient,
) {

    @Test
    fun `spectest`(): Unit {
// 使用扩展函数
        val query = sql.createQuery(SysUser::class) {
            orderBy(table.makeOrders("id  DESC"))
            select(table)
        }.fetchPage(0, 10)
        println(query)


    }


}
