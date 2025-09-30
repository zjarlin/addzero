package site.addzero.screens.product.sc

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.annotation.Route
import site.addzero.screens.product.*
import site.addzero.screens.product.vm.*


@Route("物联网模块", "产品管理")
@Composable
fun ProdMannager() {
    val koinViewModel = koinViewModel<ProductViewModel>()
    ProductScreen(koinViewModel)
}

@Route("物联网模块", "产品分类管理")
@Composable
fun EquipmentClassificationManagement() {
    val koinViewModel = koinViewModel<ProductCategoryViewModel>()
    ProductCategoryScreen(koinViewModel)
}

@Route("物联网模块", "物模型管理")
@Composable
fun ManagementOfPhysicalModels() {
    val koinViewModel = koinViewModel<ThingModelViewModel>()
    ThingModelScreen(koinViewModel)
}


@Route("物联网模块", "物模型属性管理")
@Composable
fun ManagementOfPhysicalModelAttributes() {
    val koinViewModel = koinViewModel<ThingModelPropertyViewModel>()
    ThingModelPropertyScreen(koinViewModel)
}


@Route("物联网模块", "设备管理")
@Composable
fun DeviceMannager() {
    val koinViewModel = koinViewModel<DeviceViewModel>()
    DeviceScreen(koinViewModel)

}

@Route("物联网模块", "设备接入")
@Composable
fun Shebiejieru() {
    MqttMessageScreen()

}

