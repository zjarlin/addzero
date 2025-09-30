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
class MockDataTests2(val kSqlClient: KSqlClient) {

    @Test
    fun `test`() {
        // 创建产品分类数据
        val categories = listOf(
            ProductCategory {
                name = "传感器"
                description = "各类传感器设备"
                parent = {
                    ProductCategory {
                        name = ""
                    }
                } as ProductCategory?
                parentId = null
                products = emptyList()
                sort = 1
                enabled = true
            },
            ProductCategory {
                name = "智能家居"
                description = "智能家居设备系列"
                parent = null
                parentId = null
                products = emptyList()
                sort = 2
                enabled = true
            },
            ProductCategory {
                name = "工业控制"
                description = "工业自动化控制设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 3
                enabled = true
            },
            ProductCategory {
                name = "安防监控"
                description = "安全防护监控设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 4
                enabled = true
            },
            ProductCategory {
                name = "环境监测"
                description = "环境参数监测设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 5
                enabled = true
            },
            ProductCategory {
                name = "温湿度传感器"
                description = "专门用于检测温湿度的传感器"
                parent = null
                parentId = null
                products = emptyList()
                sort = 6
                enabled = true
            },
            ProductCategory {
                name = "光照传感器"
                description = "用于检测光照强度的传感器"
                parent = null
                parentId = null
                products = emptyList()
                sort = 7
                enabled = true
            },
            ProductCategory {
                name = "空气质量传感器"
                description = "检测空气中有害物质的传感器"
                parent = null
                parentId = null
                products = emptyList()
                sort = 8
                enabled = true
            },
            ProductCategory {
                name = "智能灯具"
                description = "可通过网络控制的照明设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 9
                enabled = true
            },
            ProductCategory {
                name = "智能插座"
                description = "可远程控制的电源插座"
                parent = null
                parentId = null
                products = emptyList()
                sort = 10
                enabled = true
            },
            ProductCategory {
                name = "门锁门禁"
                description = "智能门锁及门禁系统"
                parent = null
                parentId = null
                products = emptyList()
                sort = 11
                enabled = true
            },
            ProductCategory {
                name = "PLC控制器"
                description = "可编程逻辑控制器"
                parent = null
                parentId = null
                products = emptyList()
                sort = 12
                enabled = true
            },
            ProductCategory {
                name = "变频器"
                description = "电机调速控制设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 13
                enabled = true
            },
            ProductCategory {
                name = "HMI人机界面"
                description = "人机交互显示设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 14
                enabled = true
            },
            ProductCategory {
                name = "视频监控"
                description = "网络摄像头及视频监控设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 15
                enabled = true
            },
            ProductCategory {
                name = "入侵报警"
                description = "防盗报警设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 16
                enabled = true
            },
            ProductCategory {
                name = "消防报警"
                description = "火灾报警设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 17
                enabled = true
            },
            ProductCategory {
                name = "有毒气体检测"
                description = "检测环境中危险气体浓度的设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 18
                enabled = true
            },
            ProductCategory {
                name = "噪声检测"
                description = "环境噪声水平检测设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 19
                enabled = true
            },
            ProductCategory {
                name = "水质检测"
                description = "水质参数检测设备"
                parent = null
                parentId = null
                products = emptyList()
                sort = 20
                enabled = true
            }
        )

        // 批量保存产品分类
        val batchSaveResult: KBatchSaveResult<ProductCategory> = kSqlClient.saveEntities(categories)
        println()

    }
}
