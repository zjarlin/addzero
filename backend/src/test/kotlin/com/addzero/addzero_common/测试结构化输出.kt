package com.addzero.addzero_common

import com.addzero.web.modules.sys_dict.entity.SysDict
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试结构化输出(
    val sql: KSqlClient,
    private val chatClient: ChatClient

) {


    @Test
    fun test() {
        val entity = chatClient.prompt().user {
            it.text("帮我转为字典 {ctx}.")
                .param(
                    "ctx", """
                   /**
 * 🎯 树节点选择状态枚举
 */
enum class SelectionState {
    /** 未选中 */
    UNSELECTED,
    /** 半选中（部分子节点选中） */
    INDETERMINATE,
    /** 全选中 */
    SELECTED
} 保存字典 
                """.trimIndent()
                )
        }.call().entity<SysDict>(SysDict::class.java)
        println(entity)


    }


}
