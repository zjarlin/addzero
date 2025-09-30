package site.addzero.screens.product.vm

import org.koin.android.annotation.KoinViewModel
import site.addzero.abs.BaseTableViewModel
import site.addzero.abs.TApi
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.api.ApiProvider.deviceApi
import site.addzero.generated.isomorphic.DeviceIso

object BizDeviceApi : TApi<DeviceIso> {
    override suspend fun page(commonTableDaTaInputDTO: CommonTableDaTaInputDTO): PageResult<DeviceIso> {
        return deviceApi.page(commonTableDaTaInputDTO)
    }

    override suspend fun save(input: DeviceIso): DeviceIso {
        return deviceApi.save(input)
    }

    override suspend fun edit(e: DeviceIso): Int {
        return deviceApi.edit(e)
    }

    override suspend fun deleteByIds(ids: String): Int {
        return deviceApi.deleteByIds(ids)
    }

    override suspend fun saveBatch(input: List<DeviceIso>): Int {
        return deviceApi.saveBatch(input)
    }

    override suspend fun findById(id: String): DeviceIso {
        return deviceApi.findById(id)
    }

    override suspend fun loadTableConfig() {
        return deviceApi.loadTableConfig()
    }
}
@KoinViewModel
class DeviceViewModel : BaseTableViewModel<DeviceIso, BizDeviceApi>(BizDeviceApi) {

}
