package site.addzero.screens.product.vm

import io.ktor.util.StringValues
import org.koin.android.annotation.KoinViewModel
import site.addzero.abs.BaseTableViewModel
import site.addzero.abs.TApi
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.api.ApiProvider.productApi
import site.addzero.generated.isomorphic.ProductIso

object BizProductApi : TApi<ProductIso> {
    override suspend fun page(commonTableDaTaInputDTO: CommonTableDaTaInputDTO): PageResult<ProductIso> {
        return productApi.page(commonTableDaTaInputDTO)
    }

    override suspend fun save(input: ProductIso): ProductIso {
        val save = productApi.save(input)
        return save
    }

    override suspend fun edit(e: ProductIso): Int {

        return productApi.edit(e)
    }

    override suspend fun deleteByIds(ids: String): Int {
        return productApi.deleteByIds(ids)
    }

    override suspend fun saveBatch(input: List<ProductIso>): Int {
        return productApi.saveBatch(input)
    }

    override suspend fun findById(id: String): ProductIso {
        return productApi.findById(id)
    }

    override suspend fun loadTableConfig() {
        return productApi.loadTableConfig()
    }
}

@KoinViewModel
class ProductViewModel : BaseTableViewModel<ProductIso, BizProductApi>(BizProductApi) {


    //    fun loadTs(): List<T> {
//        return listOf(
//            T(
//                id = 1,
//                name = "数控机床",
//                code = "CNC-001",
//                productCategory = TCategoryIso(id = 1, name = "机床", enabled
//                = true),
//                description = "高精度数控机床",
//                accessMethod = "MQTT",
//                authMethod = "证书认证",
//                enabled = true
//            )
//        )
//    }


}

