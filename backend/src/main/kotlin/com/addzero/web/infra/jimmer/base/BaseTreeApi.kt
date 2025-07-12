package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import com.addzero.common.consts.sql
import com.addzero.web.infra.curllog.CurlLog
import com.addzero.web.infra.jackson.convertToList
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.reflect.KClass


private const val CHILDREN = "children"

private const val PARENT = "parent"

private const val KEYWORD_PROP = "name"

interface BaseTreeApi<E : Any,  Iso : Any> {

    // 获取 ObjectMapper 实例的方法，由实现类提供
//    fun getObjectMapper(): ObjectMapper

    @GetMapping("/tree")
    @CurlLog
    fun tree(
        @RequestParam keyword: String
    ): List<E> {
        val immutableType = ImmutableType.get(
            CLASS().java
        )
        val prop = immutableType.getProp(CHILDREN)
        val parentProp = immutableType.getProp(PARENT)

//        val childProp = TypedProp.referenceList<E, List<E>>(prop).unwrap()

//        val parentProp = TypedProp.reference<E, List<E>>(CLASS().memberProperties.find { it.name == PARENT }?.toImmutableProp()).unwrap()

        val map = sql.executeQuery(CLASS()) {
            val propExpression = table.get<String>(KEYWORD_PROP)
            val parentexpression = table.getAssociatedId<Long>(parentProp)

            where(
                or(
                    propExpression `ilike?` keyword, table.exists<E>(prop) { propExpression `ilike?` keyword }
                ), parentexpression.isNull()

            )

            val _fetcher = FetcherImpl(CLASS().java)
            select(
                table.fetch(
                    _fetcher.allScalarFields().addRecursion(
                        CHILDREN, null
                    )
                )
            )
        }
        // 使用字节码 CLASSIso 和 Kotlinx 序列化避免泛型擦除问题
//        val isoClass = CLASSIso()

//        val convertTo = map.convertToList(isoClass)

        // 通过字节码获取序列化器
//        isoClass.serializerOrNull()
//        val isoSerializer = serializer(isoClass.java)
//        val listSerializer = ListSerializer(isoSerializer)

        // 使用 Kotlinx 序列化解析
//        val readValue = Json.decodeFromString(listSerializer, content)
//        return convertTo as List<Iso>
        return map
       // 直接返回，不进行额外的序列化转换
//    @Suppress("UNCHECKED_CAST")
//    return map as List<Iso>
    }

    fun CLASS(): KClass<E> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
        val type = typeArgument as Class<E>
        return type.kotlin
    }

    fun CLASSIso(): KClass<Iso> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 1) // 第二个泛型参数
        val type = typeArgument as Class<Iso>
        return type.kotlin
    }
}
