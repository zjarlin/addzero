package com.addzero.web.infra.jimmer.base

import cn.hutool.core.util.TypeUtil
import com.addzero.common.consts.sql
import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.web.infra.curllog.CurlLog
import com.addzero.web.infra.jackson.convertToList
import com.addzero.web.infra.jimmer.adv_search.createLowQuery
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


private const val CHILDREN = "children"

private const val PARENT = "parent"

/**
 *任意位置的下拉框,需要配合Iso2DataProvider做下拉数据的提供者,然后jimmer实体中的关联关系就可以用这个下拉框展示
 */
interface BaseTreeApi<E : Any> {

    // 获取 ObjectMapper 实例的方法，由实现类提供
//    fun getObjectMapper(): ObjectMapper

    /**
     * 通过反射查找实体类中带有 @LabelProp 注解的属性
     * 如果有多个 @LabelProp 属性，返回第一个非空的那个
     * 如果没有找到，返回默认值 "name"
     */
    fun findLabelPropInEntity(): String {
        return try {
            val entityClass = CLASS()

            // 获取所有带有 @LabelProp 注解的属性
            val labelProperties = entityClass.memberProperties.filter { property ->
                property.annotations.any { annotation ->
                    annotation.annotationClass.simpleName == "LabelProp"
                }
            }

            if (labelProperties.isNotEmpty()) {
                // 如果有多个 @LabelProp 属性，选择第一个非空的
                val selectedProperty = labelProperties.firstOrNull { property ->
                    val propertyName = property.name
                    // 这里可以添加更复杂的非空检查逻辑
                    // 目前简单返回第一个找到的
                    propertyName.isNotBlank()
                } ?: labelProperties.first()

                val labelFieldName = selectedProperty.name
                if (labelProperties.size > 1) {
                    println("BaseTreeApi: 找到多个 @LabelProp 标记的属性: ${entityClass.simpleName}, 选择: ${labelFieldName}")
                } else {
                    println("BaseTreeApi: 找到 @LabelProp 标记的属性: ${entityClass.simpleName}.${labelFieldName}")
                }
                labelFieldName
            } else {
                println("BaseTreeApi: 在 ${entityClass.simpleName} 中未找到 @LabelProp 标记的属性，使用默认值 'name'")
                "name"  // 默认使用 name 字段
            }
        } catch (e: Exception) {
            println("BaseTreeApi: 查找 @LabelProp 属性时发生错误: ${e.message}")
            "name"  // 出错时使用默认值
        }
    }

    @GetMapping("/tree")
    @CurlLog
    fun tree(
        @RequestParam keyword: String
    ): List<E> {
        // 通过反射获取加了 @LabelProp 注解的属性名
        val keywordProp = findLabelPropInEntity()

        val immutableType = ImmutableType.get(
            CLASS().java
        )

        // 判断是否为树形结构（是否有 children 字段）
        val isTree = immutableType.declaredProps.map { it.key }.any { it == CHILDREN }

        val map = if (isTree) {
            // 树形结构查询
            val prop = immutableType.getProp(CHILDREN)
            val parentProp = immutableType.getProp(PARENT)

            sql.executeQuery(CLASS()) {
                val propExpression = table.get<String>(keywordProp)
                val parentexpression = table.getAssociatedId<Long>(parentProp)

                where(
                    or(
                        propExpression `ilike?` keyword,
                        table.exists<E>(prop) { propExpression `ilike?` keyword }
                    ),
                    parentexpression.isNull()
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
        } else {
            // 普通列表查询（非树形结构）
            sql.executeQuery(CLASS()) {
                val propExpression = table.get<String>(keywordProp)

                where(
                    propExpression `ilike?` keyword
                )

                val _fetcher = FetcherImpl(CLASS().java)
                select(
                    table.fetch(_fetcher.allScalarFields())
                )
            }
        }

        // 将 Jimmer 实体转换为同构体
        return map
    }

    fun CLASS(): KClass<E> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
        val type = typeArgument as Class<E>
        return type.kotlin
    }


}
