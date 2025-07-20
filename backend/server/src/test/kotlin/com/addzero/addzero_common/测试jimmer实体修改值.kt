package com.addzero.addzero_common

import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.KExpression
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class 测试jimmer实体修改值(
    val sql: KSqlClient,
) {


    private inline fun <reified E : Any> selectGeneric(
        keyword: String,
        getXxx: KExpression<String>?,
        pageNo: Int,
        pageSize: Int
    ): List<E> {

        val createQuery = sql.createQuery(E::class) {
//            where(getXxx `ilike?` keyword)
            orderBy(table.makeOrders("sid asc"))
            select(table)
        }.fetchPage(pageNo - 1, pageSize)
        val rows = createQuery.rows


        return rows
    }


}


//infix fun <T : Enum<T>> KExpression<T>.`enumValueIn?`(
//    values: Collection<T>?
//): KNonNullExpression<Boolean>? = values?.let {
//    InCollectionPredicate(nullable = false, negative = false, this, it)
//}


//infix fun <T : Enum<T>> KExpression<Collection<T>>.`enumValueIntersection?`(
//    values: Collection<T>?
//): KNonNullExpression<Boolean>? = values?.let {
//    InCollectionPredicate(nullable = false, negative = false, this, it)
//}


//private infix fun  KExpression<String>.contains(target: KExpression<String>): KNonNullExpression<Boolean> {
//    val sql = sql(Boolean::class, "%e like '%'|| %e ||'%'") {
//        expression(this@like)
//        expression(target)
//    }
//    return sql
//}

//class AJoibB<G : Any,B : Any>:KWeakJoin<G,B>(){
//    override fun on(source: KNonNullTable<G>, target: KNonNullTable<B>): KNonNullExpression<Boolean>? {
//        return super.on(source, target)
//    }


//}

