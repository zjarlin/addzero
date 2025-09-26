package site.addzero.screens.product.vm

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
        return iso(input)
    }
    private suspend fun iso(input: ProductIso): ProductIso {
        return productApi.save(input)
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
}

