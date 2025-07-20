package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.kmp.entity.PageResult
import com.addzero.web.infra.constant.RestConsts
import com.addzero.web.infra.jimmer.base.pagefactory.createPageFactory
import org.babyfish.jimmer.Input
import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KClass


interface BaseCrudController<T : Any, Spec : KSpecification<T>, SaveInputDTO : Input<T>, UpdateInputDTO : Input<T>, V : View<T>> {

    private val idName: String
        get() = "id"

    // 懒加载 sqlClient，确保只初始化一次并缓存结果
    val sql: KSqlClient get() = lazySqlClient

    @GetMapping("/page")
    fun page(
        spec: Spec? = null,
        @RequestParam(defaultValue = "0") pageNum: Int = 0,
        @RequestParam(defaultValue = "10") pageSize: Int = 10,
    ): PageResult<V> {
//        var pageNum = pageNum
        // 这里需要实现分页查询逻辑
        // 示例代码省略
//        pageNum -= 1
        val createQuery = sql.createQuery(CLASS()) {
            where(spec)
            orderBy(table.makeOrders("$idName desc"))
            select(
                table.fetch(VCLASS())
            )
        }
        val createPageFactory = createPageFactory<V>()

        val fetchPage = createQuery.fetchPage(pageNum, pageSize, null, createPageFactory)
//        val fetchPage = createQuery.fetchSpringPage(pageNum, pageSize)
        return fetchPage
    }

    @GetMapping(RestConsts.listAllUrl)
    fun list(
    ): List<Any> {
        val createQuery = sql.createQuery(CLASS()) {
            select(table.fetch(VCLASS()))
        }
        val execute = createQuery.execute()
        return execute
    }


    @PostMapping("/saveBatch")
    fun saveBatch(
        @RequestBody input: List<SaveInputDTO>,
    ): Int {
        val toList = input.map { it.toEntity() }.toList()
        val saveEntities = sql.saveEntities(toList)
        return saveEntities.totalAffectedRowCount
    }

    @GetMapping("/findById")
    fun findById(id: String): T? {
        val byId = sql.findById(CLASS(), id)
        return byId
    }

    @DeleteMapping(RestConsts.deleteUrl)
    fun deleteByIds(@RequestParam ids: List<Long>): Int {
        val affectedRowCountMap = sql.deleteByIds(CLASS(), ids).totalAffectedRowCount
        return affectedRowCountMap
    }

    @PostMapping(RestConsts.saveUrl)
    fun save(@RequestBody inputDTO: SaveInputDTO): Int {
        val modifiedEntity = sql.save(inputDTO).totalAffectedRowCount
        return modifiedEntity
    }

    @PostMapping(RestConsts.updateUrl)
    fun edit(@RequestBody inputDTO: UpdateInputDTO): Int {
        val update = sql.update(inputDTO).totalAffectedRowCount
        return update
    }

    companion object {
        private val lazySqlClient: KSqlClient by lazy {
            SpringUtil.getBean(KSqlClient::class.java)
        }
    }

    fun CLASS(): KClass<T> {
        return (TypeUtil.getTypeArgument(javaClass, 0) as Class<T>).kotlin
    }

    fun SpecCLASS(): KClass<Spec> {
        return (TypeUtil.getTypeArgument(javaClass, 1) as Class<Spec>).kotlin
    }

    fun SaveInputDTOCLASS(): KClass<SaveInputDTO> {
        return (TypeUtil.getTypeArgument(javaClass, 2) as Class<SaveInputDTO>).kotlin
    }

    fun UpdateInputDTOCLASS(): KClass<UpdateInputDTO> {
        return (TypeUtil.getTypeArgument(javaClass, 3) as Class<UpdateInputDTO>).kotlin
    }

    fun VCLASS(): KClass<V> {
        return (TypeUtil.getTypeArgument(javaClass, 4) as Class<V>).kotlin
    }
}
