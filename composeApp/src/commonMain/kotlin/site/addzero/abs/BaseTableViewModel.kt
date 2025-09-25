package site.addzero.abs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import site.addzero.assist.api
import site.addzero.component.table.original.entity.StatePagination
import site.addzero.component.toast.ToastManager
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.entity.low_table.StateSearch
import site.addzero.entity.low_table.StateSort

interface TApi<TIso> {
    suspend fun page(commonTableDaTaInputDTO: CommonTableDaTaInputDTO): PageResult<TIso>
    suspend fun save(input: TIso): Int
    suspend fun edit(e: TIso): Int
    suspend fun deleteByIds(ids: String): Int
    suspend fun saveBatch(input: List<TIso>): Int
    suspend fun findById(id: String): TIso
    suspend fun loadTableConfig()
}


abstract class BaseTableViewModel<T, Api : TApi<T>>(val bizApi: Api) :
    ViewModel() {
    var data by mutableStateOf(listOf<T>())
    var showForm by mutableStateOf(false)
    var selectedT by mutableStateOf<T?>(null)
    var keyword by mutableStateOf("")
    var stateSearch by mutableStateOf(setOf<StateSearch>())
    var stateSort by mutableStateOf(setOf<StateSort>())
    var statePagination by mutableStateOf(StatePagination())

    fun onSearch(
        keyword: String,
        serchState: Set<StateSearch>,
        stateSort: Set<StateSort>,
        statePagination: StatePagination
    ) {
        this.keyword = keyword
        this.stateSearch = serchState
        this.stateSort = stateSort
        this.statePagination = statePagination
        onLoadData()
    }

    fun onLoadData() {
        api {
            val page = bizApi.page(
                commonTableDaTaInputDTO = CommonTableDaTaInputDTO(
                    pageNo = statePagination.currentPage,
                    pageSize = statePagination.pageSize,
                    keyword = keyword,
                    stateSorts = stateSort.toMutableSet(),
                    stateSearches = stateSearch.toMutableSet()
                )
            )
            data = page.rows
            page.totalPageCount
            statePagination =
                statePagination.copy(currentPage = page.pageIndex, totalItems = page.totalRowCount.toInt())
        }
    }

    fun onSaveForm(ctx: T) {
        api {
            val save = bizApi.save(ctx)
            ToastManager.success("保存成功")
            onLoadData()
        }
    }
    fun onBatchDelete(it: Set<Any>) {
        api {
            bizApi.deleteByIds(ids = it.joinToString(","))
            ToastManager.success("批量删除成功")
            onLoadData()
        }
    }

    fun onDeleteClick(id: Any): Unit {
        api {
            val deleteByIds = bizApi.deleteByIds(id.toString())
            ToastManager.success("删除成功")
            onLoadData()
        }
    }

    fun onEditClick(id: Any) {
        api {
            showForm = true
//            val findById = bizApi.findById(id)
//            selectedT = findById
        }
    }


    init {
        onLoadData()
    }

}
