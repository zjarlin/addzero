package com.addzero.addzero_common

import com.addzero.kmp.entity.low_table.EnumSearchOperator.*
import com.addzero.kmp.entity.low_table.StateSearchForm
import com.addzero.model.entity.SysUser
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试高级搜索(
    val sql: KSqlClient,
) {

    @Test
    fun `spectest`(): Unit {
        // 定义查询条件
        val filters = listOf(
            StateSearchForm("name", LIKE, "%张%"),
            StateSearchForm("age", GT, "18"),
            StateSearchForm("email", IS_NOT_NULL, null)
        )
// 使用扩展函数
        val query = sql.executeQuery(SysUser::class) {

            select(table)
        }

    }


}
