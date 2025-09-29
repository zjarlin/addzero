package mock_data

import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.mutation.KBatchSaveResult
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import site.addzero.SpringBootApp
import site.addzero.model.entity.*
import site.addzero.model.entity.biz_device.ThingModelProperty

@SpringBootTest(classes = [SpringBootApp::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MockDataTests3(val kSqlClient: KSqlClient) {

    @Test
    fun `test`() {
        // 创建产品分类数据
        val thingModel = ThingModel {
            name = TODO()
            description = TODO()
            identifier = TODO()
            status = TODO()
            properties = listOf<ThingModelProperty>(
                ThingModelProperty {
                    identifier = TODO()
                    name = TODO()
                    description = TODO()
                    dataType = TODO()
                    required = TODO()
                    minNormalValue = TODO()
                    maxNormalValue = TODO()
                    minWarningValue = TODO()
                    maxWarningValue = TODO()
                    dataPrecision = TODO()
                    accessMode = TODO()
                    sort = TODO()
                }


            )
        }
        val listOf = listOf(thingModel)


        // 批量保存产品分类
        val batchSaveResult = kSqlClient.saveEntities(listOf)
        println()

    }
}
