package site.addzero.web.infra.jimmer.base

import cn.hutool.extra.spring.SpringUtil
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.jimmer.adv_search.queryPage
import site.addzero.web.infra.jimmer.setJimmerEntity

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
    fun save(@RequestBody input: E): E {
        val save = sql.save(input) {
            setMode(SaveMode.NON_IDEMPOTENT_UPSERT)
        }
        val modifiedEntity = save.modifiedEntity
        return modifiedEntity
    }

    @PostMapping("/upsert")
    fun upsert(@RequestBody input: E): E {
        val jimmerEntity = input.setJimmerEntity {
            DraftObjects.unload(it, "createTime") // 清除createTime字段
        }!!

        val save = sql.save(jimmerEntity)
        val modifiedEntity = save.modifiedEntity
        return modifiedEntity
    }

    @PutMapping("/update")
    fun edit(@RequestBody e: E): Int {
        val jimmerEntity = e.setJimmerEntity {
            DraftObjects.unload(it, "createTime") // 清除createTime字段
        }!!

        val save = sql.save(jimmerEntity) {
            setMode(SaveMode.UPDATE_ONLY)
        }
        val update = save.totalAffectedRowCount
        return update
    }

    @DeleteMapping("/delete")
    fun deleteByIds(@RequestParam vararg ids: String): Int {
        val map = ids.map { it.toLong() }
        val affectedRowCountMap = sql.deleteByIds(type, map).totalAffectedRowCount
        return affectedRowCountMap
    }

    @GetMapping("/delete")
    fun delete(id: String): Boolean {
        val toLong = id.toLong()
        val affectedRowCountMap = sql.deleteByIds(type, listOf(toLong)).totalAffectedRowCount
        return affectedRowCountMap > 0
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
        val byId = sql.findById(type, id.toLong())
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
