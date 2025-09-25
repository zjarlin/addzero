package site.addzero.web.infra.jimmer.base

import cn.hutool.extra.spring.SpringUtil
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*
import site.addzero.core.ext.toJsonByKtx
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.jimmer.adv_search.queryPage
import site.addzero.web.infra.jackson.toJson
import site.addzero.web.infra.jimmer.setJimmerEntity
import site.addzero.web.infra.jimmer.toJimmerEntity

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
        val toJsonByKtx = input.toJson()
        val fromString = ImmutableObjects.fromString(CLASS(), toJsonByKtx)

        //由于一些原因kmp实现unload比较麻烦,又因为无参构造字段必须要有默认值,这里得再卸载一下
        val jimmerEntity = fromString.setJimmerEntity {
            DraftObjects.unload(it, "createTime") // 清除createTime字段
        }
        val toJson = jimmerEntity?.toJson()
        val save = sql.save(jimmerEntity!!)
        val modifiedEntity = save.totalAffectedRowCount
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
