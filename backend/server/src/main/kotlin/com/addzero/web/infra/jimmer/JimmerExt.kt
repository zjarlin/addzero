package com.addzero.web.infra.jimmer

import cn.hutool.core.bean.BeanUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.common.kt_util.toBitmask
import com.addzero.kmp.core.ext.toJsonByKtx
import com.addzero.web.infra.jackson.toJson
import com.alibaba.fastjson2.parseObject
import org.babyfish.jimmer.DraftObjects
import org.babyfish.jimmer.ImmutableObjects
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.runtime.DraftSpi
import org.babyfish.jimmer.runtime.Internal
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.KExpression
import org.babyfish.jimmer.sql.kt.ast.expression.KNonNullExpression
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.sql
import org.babyfish.jimmer.sql.kt.ast.query.KConfigurableRootQuery
import org.babyfish.jimmer.sql.kt.ast.query.KMutableRootQuery
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.babyfish.jimmer.sql.kt.ast.table.KNonNullTable
import org.babyfish.jimmer.sql.kt.ast.table.isNull
import org.springframework.jdbc.core.JdbcTemplate

object JimmerExt


fun <E : Any> KSqlClient.updateById(e: E) = run {
    val save = this.save(e) {
        setMode(SaveMode.UPDATE_ONLY)
    }
    save
}

fun <E : Any> KNonNullTable<E>.eqId(e: E?): KNonNullExpression<Boolean> {
    if (e == null) {
        return isNull()
    }
    val id = ImmutableObjects.get(e, "id")
    val expression = this@eqId.getId<Any>() eq id
    return expression
}

inline fun <reified A : Any, reified E : Any> A.toJimmerEntity(): E {
    val toJsonByKtx = this.toJsonByKtx<A>()
    val fromString = ImmutableObjects.fromString(E::class.java, toJsonByKtx)
    val jimmerEntity = fromString.setJimmerEntity {
        DraftObjects.unload(it, "createTime") // 清除createTime字段
    }
    return jimmerEntity!!
}


fun KSqlClient.sql(sql: String): List<Map<String?, Any?>?> = SpringUtil.getBean(JdbcTemplate::class.java).queryForList(sql)


infix fun <E : Enum<E>> KExpression<List<E>>.`enumValueIn?`(enums: Collection<E>): KNonNullExpression<Boolean>? {
    if (enums.isEmpty()) {
        return null
    }
    val bit = enums.toBitmask()
    return sql(Boolean::class, "(%e & %v) <> 0") {
        expression(this@`enumValueIn?`)
        value(bit)
    }
}

inline fun <reified E : Any, R> KSqlClient.createQuery(noinline block: KMutableRootQuery<E>.() -> KConfigurableRootQuery<E, R>) =
    createQuery(E::class, block)


//fun <E : Any,T:Any>         KRepository<E, T>.findPage(
//
//    query:Page,
//
//): Unit {
//
//}
//


//fun <T : Any> KSqlClient.list(entityType: KClass<T>): List<T> {
//    val bean = SpringUtil.getBean(KSqlClient::class.java)
//    val createQuery = bean.createQuery(entityType) {
//        val select = select(
//            table
//        )
////        where()
//        select
//    }
//    val execute1 = createQuery.execute()
//    return execute1
//}


//inline fun <reified E : Any, P : Any> E.set(propName: String, columnValue: P): E {
//    val kClass: KClass<E> = E::class
////    val otherProperties: List<KProperty1<E, *>> = kClass.memberProperties.filter { it.name != propName }
//    val constructor: KFunction<E>? = kClass.primaryConstructor
//    val newParams: List<Unit> = constructor
//        ?.parameters
//        ?.map {
//            val paramKClass: KClass<*> = it.jdbcType.classifier as KClass<*>
//            if (paramKClass == columnValue::class) {
//                // 这里原代码块内没有内容，可按需补充
//            }
//        }?: emptyList()
//    return constructor?.call(*newParams.toTypedArray()) ?: this
//}

fun <E : Any> E?.setJimmerEntity(fieldName: String, value: Any?): E? = this?.let { entity ->
    val copy = this.setJimmerEntity {
        DraftObjects.set(it, fieldName, value)
    }
    return copy
}

fun <E : Any> E?.setJimmerEntity(block: (DraftSpi) -> Unit = {}): E? = this?.let { e ->
    Internal.produce(ImmutableType.get(e.javaClass), e) { d ->
        block(d as DraftSpi)
        d
    } as E
}

fun <E : Any> E.fromMap(updates: Map<String, Any?>, block: (String, Any?) -> Unit = { _, _ -> }): E? {
    val newItem = this.setJimmerEntity { draft ->
        updates.forEach { (fieldName, value) ->
            DraftObjects.set(draft, fieldName, value)
            block(fieldName, value)
        }
    }
    return newItem
}

fun <E : Any> E?.toMap(): MutableMap<String, Any>? {
    if (this == null) {
        return null
    }
    val beanToMap = BeanUtil.beanToMap(this, false, true)
    return beanToMap
}


inline fun <E : Any, reified Spec : KSpecification<E>> Spec.fromMap(updates: Map<String, Any?>): Spec? {
    val jsonObject = this.toJson().parseObject()
    jsonObject.putAll(updates)
    val fromString = ImmutableObjects.fromString(
        Spec::class.java, jsonObject
            .toJson()
    )
    return fromString
}
