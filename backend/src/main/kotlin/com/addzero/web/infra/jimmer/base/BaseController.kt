package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.kmp.exp.BizException
import com.addzero.kmp.entity.low_table.CommonTableDaTaInputDTO
import com.addzero.kmp.entity.low_table.EnumLogicOperator
import com.addzero.web.infra.constant.RestConsts.deleteUrl
import com.addzero.web.infra.constant.RestConsts.listAllUrl
import com.addzero.web.infra.constant.RestConsts.saveUrl
import com.addzero.web.infra.constant.RestConsts.updateUrl
import com.addzero.web.infra.jimmer.adv_search.queryPage
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass

interface BaseController<E : Any> {

    //interface Noting
    val idName: String
        get() {
            return "id"
        }

    // 懒加载 sqlClient，确保只初始化一次并缓存结果
    val sql: KSqlClient get() = lazySqlClient
//    val fetcher: Fetcher<T> get() = newFetcher(CLASS()).by{
//        allScalarFields()
//    }


    @PostMapping("/page")
    fun page(
        @RequestBody commonTableDaTaInputDTO: CommonTableDaTaInputDTO
    ): Page<E> {
        val tableName = commonTableDaTaInputDTO.tableName
        if (tableName.isBlank()) {
            throw BizException("Table name is blank")
        }
        commonTableDaTaInputDTO.keyword
        val stateSorts = commonTableDaTaInputDTO.stateSorts
        val stateSearchForms = commonTableDaTaInputDTO.stateSearchForms
        val pageNo = commonTableDaTaInputDTO.pageNo
        val pageSize = commonTableDaTaInputDTO.pageSize
        val associateBy = stateSearchForms.groupBy { it.logicType }
        val andSearchCondition = associateBy[EnumLogicOperator.AND]
        val orSearchCondition = associateBy[EnumLogicOperator.OR]
//        val klass = entityMap[tableName]
        val klass1 = CLASS()
        val queryPage = queryPage(
            sortStats = stateSorts,
            andStateSearchConditions = andSearchCondition?.toMutableSet(),
            orStateSearchConditions = orSearchCondition?.toMutableSet(),
            entityClass = klass1,
            stateVos = mutableSetOf(),
            pageNo = pageNo,
            pageSize = pageSize,
        )
//        val decodeFromString = json.decodeFromString<SpecPageResult<Map<String, JsonElement?>>>( toJsonByKtx )
        return queryPage
    }


    fun CLASS(): KClass<E> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
        val type = typeArgument as Class<E>
        return type.kotlin
    }

//    @GetMapping(listAllUrl)
//    fun list(
//    ): List<Any> {
//        val entityType = CLASS()
//        val execute1 = sql.list(entityType)
//        return execute1
//    }


    @PostMapping("/saveBatch")
    fun saveBatch(
        @RequestBody input: List<E>,
    ): Int {
        val saveEntities = sql.saveEntities(input)
        return saveEntities.totalAffectedRowCount
    }


    @GetMapping("/findById")
    fun findById(id: String): E? {
        val byId = sql.findById(CLASS(), id)
        return byId
    }

    @DeleteMapping(deleteUrl)
    fun deleteByIds(@RequestParam vararg ids: String): Int {
        val affectedRowCountMap = sql.deleteByIds(CLASS(), listOf(*ids)).totalAffectedRowCount
        return affectedRowCountMap
    }

    @PostMapping(saveUrl)
    fun save(@RequestBody input: E): Int {
        val modifiedEntity = sql.save(input).totalAffectedRowCount
        return modifiedEntity
    }

    @PutMapping(updateUrl)
    fun edit(@RequestBody e: E): Int {
        val update = sql.update(e).totalAffectedRowCount
        return update
    }


    companion object {
        private val lazySqlClient: KSqlClient by lazy {
            SpringUtil.getBean(KSqlClient::class.java)
        }
    }
}
