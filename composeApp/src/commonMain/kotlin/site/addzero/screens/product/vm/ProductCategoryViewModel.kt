package site.addzero.screens.product.vm

import org.koin.android.annotation.KoinViewModel
import site.addzero.abs.BaseTableViewModel
import site.addzero.abs.TApi
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.api.ApiProvider.productCategoryApi
import site.addzero.generated.isomorphic.ProductCategoryIso
object ProductCategoryApi : TApi<ProductCategoryIso> {
    override suspend fun page(commonTableDaTaInputDTO: CommonTableDaTaInputDTO): PageResult<ProductCategoryIso> {
        return productCategoryApi.page(commonTableDaTaInputDTO)
    }

    override suspend fun save(input: ProductCategoryIso): ProductCategoryIso {
        return productCategoryApi.save(input)
    }

    override suspend fun edit(e: ProductCategoryIso): Int {
        return productCategoryApi.edit(e)
    }

    override suspend fun deleteByIds(ids: String): Int {
        return productCategoryApi.deleteByIds(ids)
    }

    override suspend fun saveBatch(input: List<ProductCategoryIso>): Int {
        return productCategoryApi.saveBatch(input)
    }

    override suspend fun findById(id: String): ProductCategoryIso {
        return productCategoryApi.findById(id)
    }

    override suspend fun loadTableConfig() {
        return productCategoryApi.loadTableConfig()
    }
}

@KoinViewModel
class ProductCategoryViewModel : BaseTableViewModel<ProductCategoryIso, ProductCategoryApi>(ProductCategoryApi) {

}
