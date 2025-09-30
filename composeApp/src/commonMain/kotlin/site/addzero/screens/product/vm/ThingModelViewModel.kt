package site.addzero.screens.product.vm

import org.koin.android.annotation.KoinViewModel
import site.addzero.abs.BaseTableViewModel
import site.addzero.abs.TApi
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.api.ApiProvider.thingModelApi
import site.addzero.generated.api.ApiProvider.thingModelPropertyApi
import site.addzero.generated.isomorphic.ThingModelIso
import site.addzero.generated.isomorphic.ThingModelPropertyIso

object BizThingModelApi : TApi<ThingModelIso> {
    override suspend fun page(commonTableDaTaInputDTO: CommonTableDaTaInputDTO): PageResult<ThingModelIso> {
        return thingModelApi.page(commonTableDaTaInputDTO)
    }

    override suspend fun save(input: ThingModelIso): ThingModelIso {
        return thingModelApi.save(input)
    }

    override suspend fun edit(e: ThingModelIso): Int {
        return thingModelApi.edit(e)
    }

    override suspend fun deleteByIds(ids: String): Int {
        return thingModelApi.deleteByIds(ids)
    }

    override suspend fun saveBatch(input: List<ThingModelIso>): Int {
        return thingModelApi.saveBatch(input)
    }

    override suspend fun findById(id: String): ThingModelIso {
        return thingModelApi.findById(id)
    }

    override suspend fun loadTableConfig() {
        return thingModelApi.loadTableConfig()
    }
}

object BizThingModelPropertyApi : TApi<ThingModelPropertyIso> {
    override suspend fun page(commonTableDaTaInputDTO: CommonTableDaTaInputDTO): PageResult<ThingModelPropertyIso> {
        return thingModelPropertyApi.page(commonTableDaTaInputDTO)
    }

    override suspend fun save(input: ThingModelPropertyIso): ThingModelPropertyIso {
        return thingModelPropertyApi.save(input)
    }

    override suspend fun edit(e: ThingModelPropertyIso): Int {
        return thingModelPropertyApi.edit(e)
    }

    override suspend fun deleteByIds(ids: String): Int {
        return thingModelPropertyApi.deleteByIds(ids)
    }

    override suspend fun saveBatch(input: List<ThingModelPropertyIso>): Int {
        return thingModelPropertyApi.saveBatch(input)
    }

    override suspend fun findById(id: String): ThingModelPropertyIso {
        return thingModelPropertyApi.findById(id)
    }

    override suspend fun loadTableConfig() {
        return thingModelPropertyApi.loadTableConfig()
    }
}

@KoinViewModel
class ThingModelViewModel : BaseTableViewModel<ThingModelIso, BizThingModelApi>(BizThingModelApi) {

}

@KoinViewModel
class ThingModelPropertyViewModel : BaseTableViewModel<ThingModelPropertyIso, BizThingModelPropertyApi>(BizThingModelPropertyApi) {

}