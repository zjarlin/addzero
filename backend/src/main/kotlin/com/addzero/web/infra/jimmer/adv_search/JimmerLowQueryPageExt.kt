package com.addzero.web.infra.jimmer.adv_search

import com.addzero.common.consts.sql
import com.addzero.kmp.entity.low_table.StateSearchForm
import com.addzero.kmp.entity.low_table.StateSort
import org.babyfish.jimmer.Page
import org.babyfish.jimmer.View
import kotlin.reflect.KClass

fun <E : Any, V : View<E>> queryPage(
    sortStats: MutableSet<StateSort>,
    andStateSearchConditions: MutableSet<StateSearchForm>,
    orStateSearchConditions: MutableSet<StateSearchForm>,
    entityClass: KClass<E>,
    view: KClass<V>,
    pageNo: Int = 1,
    pageSize: Int = 10,
): Page<V> {
    val fetchPage = sql.createLowQuery(
        sortStats, andStateSearchConditions, orStateSearchConditions, entityClass, view
    ).fetchPage(pageNo-1, pageSize)
    return fetchPage
}

fun <E : Any, V : View<E>> queryPage(
    sortStats: MutableSet<StateSort>,
    andStateSearchConditions: MutableSet<StateSearchForm>?,
    orStateSearchConditions: MutableSet<StateSearchForm>?,
    entityClass: KClass<E>,
    stateVos: MutableSet<StateVo>,
    pageNo: Int = 1,
    pageSize: Int = 10,
): Page<E> {

    val fetchPage = sql.createLowQuery(
        sortStats, andStateSearchConditions, orStateSearchConditions, entityClass, stateVos
    ).fetchPage(pageNo-1, pageSize)
    return fetchPage


}
