package jimmer_bug

import kotlinx.datetime.LocalDateTime
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import site.addzero.SpringBootApp
import site.addzero.model.entity.Product
import site.addzero.model.entity.SysDictItem

@SpringBootTest(classes = [SpringBootApp::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JimmerBugTests(val kSqlClient: KSqlClient) {

    @Test
    fun `test`() {
        val trimIndent = """
           {
  "createTime" : "2025-09-25T14:47:20.332079",
  "itemText" : "mqtt",
  "itemValue" : "mqtt",
  "sysDict" : {
    "id" : 1971101845819424768,
    "createTime" : "2025-09-25T14:37:56.00415",
    "updateTime" : "2025-09-25T14:37:56.00415",
    "updateBy" : {
      "createTime" : "2025-09-25T14:37:56.032301",
      "id" : 1
    },
    "createBy" : {
      "createTime" : "2025-09-25T14:37:56.032321",
      "id" : 1
    },
    "dictName" : "设备接入方式",
    "dictCode" : "DEVICE_ACCESS_PARTY"
  }
} 
        """.trimIndent()
        val fromString = ImmutableObjects.fromString(SysDictItem::class.java, trimIndent)

        val save1 = kSqlClient.saveCommand(fromString) {
            setMode(SaveMode.INSERT_IF_ABSENT)
//            setAssociatedMode(SysDictProps.SYS_DICT_ITEMS, AssociatedSaveMode.MERGE)
        }
        println(save1)
    }

     @Test
    fun `test1`() {

        val trimIndent = """{
  "createTime" : "2025-09-25T15:48:30.401391",
//  "name" : "134",
//  "code" : "vasd",
//  "accessMethod" : "d",
//  "authMethod" : "d",
// "id" :123456, 
}"""
         val product = Product {
//             id = 123456
             name="123123"
             code="dajsoidj"
             accessMethod="das"
             authMethod="das"
//             enabled=true
             createTime= java.time.LocalDateTime.now()
         }
         val save1 = kSqlClient.saveCommand(product) {
            setMode(SaveMode.INSERT_ONLY)
        }.execute()
         val modifiedEntity = save1.modifiedEntity
         println(modifiedEntity)

    }


}
