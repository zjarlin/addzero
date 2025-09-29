package mock_data

import cn.hutool.extra.pinyin.PinyinUtil
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.mutation.KBatchSaveResult
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import site.addzero.SpringBootApp
import site.addzero.model.entity.*
import site.addzero.util.PinYin4JUtils

@SpringBootTest(classes = [SpringBootApp::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MockDataTests(val kSqlClient: KSqlClient) {

    @Test
    fun `test`() {
        val products = listOf(
            Product {
                name = "温度传感器"
                code = PinYin4JUtils.hanziToPinyin(name)
                productCategory = ProductCategory {
                    name = "传感器"
                    description = "传感器设备"
                    parent = null
                    parentId = null
                    products = emptyList()
                    sort = 1
                    enabled = true
                }
                productCategoryId = null
                thingModel = ThingModel {
                    product = null
                    productId = null
                    name = "温度传感器模型"
                    description = "温度传感器的物模型"
                    identifier = "temp_sensor_v1"
                    status = 1
                    properties = emptyList()
                }
                thingModelId = null
                devices = emptyList()
                description = "用于测量环境温度的传感器"
                accessMethod = "MQTT"
                authMethod = "设备密钥"
                enabled = true
            },
            Product {
                name = "智能开关"
                code = PinYin4JUtils.hanziToPinyin(name)
                productCategory = ProductCategory {
                    name = "智能家居"
                    description = "智能家居设备"
                    parent = null
                    parentId = null
                    products = emptyList()
                    sort = 2
                    enabled = true
                }
                productCategoryId = null
                thingModel = ThingModel {
                    product = null
                    productId = null
                    name = "智能开关模型"
                    description = "智能开关的物模型"
                    identifier = "smart_switch_v1"
                    status = 1
                    properties = emptyList()
                }
                thingModelId = null
                devices = emptyList()
                description = "用于家庭自动化的智能开关"
                accessMethod = "HTTP"
                authMethod = "证书认证"
                enabled = true
            }
        )

        val batchSaveResult: KBatchSaveResult<Product> = kSqlClient.saveEntities(products)
        println()

    }
}
