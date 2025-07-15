package com.addzero.web.infra.jimmer.adv_search

import cn.hutool.core.convert.Convert
import cn.hutool.core.util.NumberUtil
import com.addzero.common.kt_util.JlObjUtil
import com.addzero.kmp.entity.low_table.EnumSearchOperator
import com.addzero.kmp.entity.low_table.EnumSortDirection
import com.addzero.kmp.entity.low_table.StateSearchForm
import com.addzero.kmp.entity.low_table.StateSort
import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.ast.LikeMode
import org.babyfish.jimmer.sql.ast.query.Order
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImplementor
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.*
import org.babyfish.jimmer.sql.kt.ast.query.KConfigurableRootQuery
import org.babyfish.jimmer.sql.kt.ast.query.KMutableRootQuery
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import kotlin.reflect.KClass

fun <E : Any> KMutableRootQuery<E>.buildOrderBys(
    stateSort: StateSort
): Order {
    var columnKey = stateSort.columnKey.trim()
    var finalTable = table

    if (columnKey.contains(".")) {
        val split = columnKey.split(".")
        val joinColumnKey = split.last()

        split.dropLast(1).forEach {
            finalTable = finalTable.join<E>(it)
        }
        columnKey = joinColumnKey
    }

    if (stateSort.direction == EnumSortDirection.DESC) {
        return finalTable.get<String>(columnKey).desc()
    }
    return finalTable.get<String>(columnKey).asc()
}

fun <E : Any> KMutableRootQuery<E>.buildOrderByArray(
    stateSorts: Collection<StateSort>
): Array<Order> {
    return stateSorts.map {
        buildOrderBys(it)
    }.toTypedArray()
}
// Main query functions
fun <E : Any, V : View<E>> KSqlClient.createLowQuery(
    sortStats: Set<StateSort>,
    andStateSearchConditions: Set<StateSearchForm>?,
    orStateSearchConditions: Set<StateSearchForm>?,
    entityClass: KClass<E>,
    view: KClass<V>,
): KConfigurableRootQuery<E, V> = createQuery(entityClass) {
    where(andStateSearchConditions?.let { buildAndMultiCondition(it) })
    where(orStateSearchConditions?.let { buildOrMultiCondition(it) })
    orderBy(*buildOrderByArray(sortStats))
//    orderBy(table.makeOrders(baseSortBuilder(sortStats)))
    select(table.fetch(view))
}

fun baseSortBuilder(sortStats: Collection<StateSort>): String {
    return sortStats.joinToString(",") {
        """
           ${it.columnKey}  ${it.direction.name}
       """.trimIndent()
    }
}


fun <E : Any> KSqlClient.createLowQuery(
    sortStats: Set<StateSort>,
    andStateSearchConditions: Set<StateSearchForm>?,
    orStateSearchConditions: Set<StateSearchForm>?,
    entityClass: KClass<E>,
    stateVos: Set<StateVo> = emptySet(),
): KConfigurableRootQuery<E, E> {

    val mainFetcher = FetcherImpl(entityClass.java) as FetcherImplementor<E>
    val mainImmutableType = mainFetcher.immutableType

    val fetcherImplementor = stateVos.fold(mainFetcher.add(mainImmutableType.idProp.name)) { fetcher, vo ->
        if (vo.columnKey.contains(".")) {
            val buildNestedFetcher = buildNestedFetcher(fetcher, vo.columnKey)
            buildNestedFetcher
        } else {
            val add = fetcher.add(vo.columnKey)
            add
        }
    }

    val createQuery = this.createQuery(entityClass) {
        where(andStateSearchConditions?.let { buildAndMultiCondition(it) })
        where(orStateSearchConditions?.let { buildOrMultiCondition(it) })
//        buildOrder(sortStats)
        orderBy(table.makeOrders(baseSortBuilder(sortStats)))
        if (stateVos.isEmpty()) {
            select(table)
        } else {
            select(table.fetch(fetcherImplementor))
        }
    }
    return createQuery
}

private fun <E : Any> buildNestedFetcher(
    parentFetcher: FetcherImplementor<E>,
    columnKey: String
): FetcherImplementor<E> {
    val parts = columnKey.split(".")
    val (path, last) = parts.dropLast(1) to parts.last()

    val nestedFetcher = path.fold(parentFetcher) { fetcher, part ->
        val prop = fetcher.immutableType.getProp(part)
        val childFetcher = FetcherImpl(prop.elementClass) as FetcherImplementor<*>
        fetcher.add(childFetcher.immutableType.idProp!!.name)
        fetcher.add(part, childFetcher)
    }

    return nestedFetcher.add(last)
}

private fun <E : Any> KMutableRootQuery<E>.buildOrMultiCondition(
    stateSearchConditions: Collection<StateSearchForm>
): KNonNullExpression<Boolean>? =
    or(*stateSearchConditions.map { buildConditions(it.operator, it.columnKey, it.columnValue) }.toTypedArray())

private fun <E : Any> KMutableRootQuery<E>.buildAndMultiCondition(
    stateSearchConditions: Collection<StateSearchForm>
): KNonNullExpression<Boolean>? =
    and(*stateSearchConditions.map { buildConditions(it.operator, it.columnKey, it.columnValue) }.toTypedArray())

private fun <E : Any> KMutableRootQuery<E>.buildConditions(
    operator: EnumSearchOperator,
    columnName: String,
    value: Any?
): KNonNullExpression<Boolean> {
    var actValue = value
    val toStr = Convert.toStr(value)
    val number = NumberUtil.isNumber(toStr)
    if (number) {
        actValue = Convert.toNumber(actValue)
    }
    if (toStr.contains(",")) {
        val split = toStr.split(",")

        if (split.any { NumberUtil.isNumber(it) }) {
            val pair = Convert.toDouble(split[0])  to Convert.toDouble(split[1])
            actValue = pair
        }

        if (split.any { JlObjUtil.isDateValue(it) }) {
            val pair = Convert.toDate(split[0])  to Convert.toDate(split[1])
            actValue = pair
        }
    }

    val (finalTable, columnKey) = if (columnName.contains(".")) {
        val parts = columnName.split(".")
        // Initialize with the base table and empty string
        val (resultTable, _) = parts.dropLast(1).fold(table to "") { (currentTable, _), part ->
            currentTable.join<E>(part) to part
        }
        resultTable to parts.last()
    } else {
        table to columnName
    }
    return when (operator) {
        EnumSearchOperator.EQ -> finalTable.get<Any>(columnKey).eq(actValue)
        EnumSearchOperator.NE -> finalTable.get<Any>(columnKey).ne(actValue)
        EnumSearchOperator.LIKE -> finalTable.get<String>(columnKey).ilike(actValue as String, LikeMode.ANYWHERE)


        EnumSearchOperator.GT -> finalTable.get<Comparable<Any>>(columnKey).gt(actValue as Comparable<Any>)
        EnumSearchOperator.GE -> finalTable.get<Comparable<Any>>(columnKey).ge(actValue as Comparable<Any>)
        EnumSearchOperator.LT -> finalTable.get<Comparable<Any>>(columnKey).lt(actValue as Comparable<Any>)
        EnumSearchOperator.LE -> finalTable.get<Comparable<Any>>(columnKey).le(actValue as Comparable<Any>)
        EnumSearchOperator.IN -> finalTable.get<Any>(columnKey).valueIn(actValue as Collection<Any>)
        EnumSearchOperator.NOT_IN -> finalTable.get<Any>(columnKey).valueNotIn(actValue as Collection<Any>)
        EnumSearchOperator.BETWEEN -> {
            val (start, end) = actValue as Pair<*, *>
            finalTable.get<Comparable<Any>>(columnKey).between(start as Comparable<Any>, end as Comparable<Any>)
        }

        EnumSearchOperator.NOT_BETWEEN -> {
            val (start, end) = actValue as Pair<*, *>
            finalTable.get<Comparable<Any>>(columnKey).notBetween(start as Comparable<Any>, end as Comparable<Any>)
        }

        EnumSearchOperator.IS_NULL -> finalTable.get<Any>(columnKey).isNull()
        EnumSearchOperator.IS_NOT_NULL -> finalTable.get<Any>(columnKey).isNotNull()
        EnumSearchOperator.STARTS_WITH -> finalTable.get<String>(columnKey).like(actValue as String, LikeMode.START)
        EnumSearchOperator.ENDS_WITH -> finalTable.get<String>(columnKey).like(actValue as String, LikeMode.END)
    }
}
