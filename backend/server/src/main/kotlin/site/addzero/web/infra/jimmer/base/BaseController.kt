package site.addzero.web.infra.jimmer.base

import cn.hutool.extra.spring.SpringUtil
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.jimmer.adv_search.queryPage

interface BaseController<E : Any> : BaseGenericContext<E> {
    val type get() = CLASS().kotlin
    val idName: String
        get() {
            return "id"
        }
    val sql: KSqlClient get() = lazySqlClient

    @PostMapping("/page")
    fun page(
        @RequestBody commonTableDaTaInputDTO: CommonTableDaTaInputDTO
    ): PageResult<E> {
        val stateSorts = commonTableDaTaInputDTO.stateSorts
        val stateSearchForms = commonTableDaTaInputDTO.stateSearches
        val pageNo = commonTableDaTaInputDTO.pageNo
        val pageSize = commonTableDaTaInputDTO.pageSize
        val queryPage = sql.queryPage(
            sortStats = stateSorts,
            searchConditions = stateSearchForms,
            entityClass = type,
            stateVos = mutableSetOf(),
            pageNo = pageNo,
            pageSize = pageSize,
        )
        val pageRes = queryPage.toPageResult()
        return pageRes
    }

    @PostMapping("/save")
    fun save(@RequestBody input: E): Int {
        val modifiedEntity = sql.save(input).totalAffectedRowCount
        return modifiedEntity
    }

    @PutMapping("/update")
    fun edit(@RequestBody e: E): Int {
        val update = sql.update(e).totalAffectedRowCount
        return update
    }

    @DeleteMapping("/delete")
    fun deleteByIds(@RequestParam vararg ids: String): Int {
        val affectedRowCountMap = sql.deleteByIds(type, listOf(*ids)).totalAffectedRowCount
        return affectedRowCountMap
    }

    @PostMapping("/saveBatch")
    fun saveBatch(
        @RequestBody input: List<E>,
    ): Int {
        val saveEntities = sql.saveEntities(input)
        return saveEntities.totalAffectedRowCount
    }

    @GetMapping("/findById")
    fun findById(id: String): E? {
        val byId = sql.findById(type, id)
        return byId
    }

    @GetMapping("/loadTableConfig")
    fun loadTableConfig() {
    }

    companion object {
        private val lazySqlClient: KSqlClient by lazy {
            SpringUtil.getBean(KSqlClient::class.java)
        }
    }
}

private fun <E> Page<E>.toPageResult(): PageResult<E> {
    return PageResult(this.rows, this.totalRowCount, this.totalPageCount.toInt())
}
