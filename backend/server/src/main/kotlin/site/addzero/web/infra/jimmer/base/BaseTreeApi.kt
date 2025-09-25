package site.addzero.web.infra.jimmer.base

import site.addzero.common.consts.sql
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.reflect.full.memberProperties

/**
 *任意位置的下拉框,需要配合Iso2DataProvider做下拉数据的提供者,然后jimmer实体中的关联关系就可以用这个下拉框展示
 */
interface BaseTreeApi<E : Any> : BaseGenericContext<E> {

    val entityType get() = CLASS().kotlin

    /**
     * 通过反射查找实体类中带有 @LabelProp 注解的属性
     * 如果有多个 @LabelProp 属性，返回第一个非空的那个
     * 如果没有找到，返回默认值 "name"
     */
    fun findLabelPropInEntity(): String {
        return try {

            // 获取所有带有 @LabelProp 注解的属性
            val labelProperties = entityType.memberProperties.filter { property ->
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
                    println("BaseTreeApi: 找到多个 @LabelProp 标记的属性: ${entityType.simpleName}, 选择: ${labelFieldName}")
                } else {
                    println("BaseTreeApi: 找到 @LabelProp 标记的属性: ${entityType.simpleName}.${labelFieldName}")
                }
                labelFieldName
            } else {
                println("BaseTreeApi: 在 ${entityType.simpleName} 中未找到 @LabelProp 标记的属性，使用默认值 'name'")
                "name"  // 默认使用 name 字段
            }
        } catch (e: Exception) {
            println("BaseTreeApi: 查找 @LabelProp 属性时发生错误: ${e.message}")
            "name"  // 出错时使用默认值
        }
    }

    @GetMapping("/tree")
    fun tree(
        @RequestParam keyword: String
    ): List<E> {
        // 通过反射获取加了 @LabelProp 注解的属性名
        val keywordProp = findLabelPropInEntity()

        val immutableType = ImmutableType.get(
            CLASS()
        )

        // 判断是否为树形结构（是否有 children 字段）
        val isTree = immutableType.declaredProps.map { it.key }.any { it == CHILDREN }


//        val mutableRootQueryImpl = MutableRootQueryImpl<Table<E>>(
//            sql as JSqlClientImplementor,
//            immutableType,
//            ExecutionPurpose.QUERY,
//            FilterLevel.DEFAULT
//        )
//        val table = mutableRootQueryImpl.tableImplementor as KNonNullTable<E>

        val map = if (isTree) {
            // 树形结构查询
            val prop = immutableType.getProp(CHILDREN)
            val parentProp = immutableType.getProp(PARENT)

            sql.executeQuery(entityType) {
                val propExpression = table.get<String>(keywordProp)
                val parentexpression = table.getAssociatedId<Long>(parentProp)
                where(
                    or(
                        propExpression `ilike?` keyword
                        , table.exists<E>(prop) { propExpression `ilike?` keyword }
                    ), parentexpression.isNull()

                )
                val _fetcher = FetcherImpl(CLASS())
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
            sql.executeQuery(entityType) {
                val propExpression = table.get<String>(keywordProp)

                where(
                    propExpression `ilike?` keyword
                )

                val _fetcher = FetcherImpl(CLASS())
                select(

                    table.fetch(_fetcher.allScalarFields())
                )
//                BeanUtil.copyProperties(source,target,CopyOptions.create() .setFieldMapping(mapOf<String, String>()))
            }
        }

        // 将 Jimmer 实体转换为同构体
        return map
    }


}

private const val CHILDREN = "children"

private const val PARENT = "parent"

