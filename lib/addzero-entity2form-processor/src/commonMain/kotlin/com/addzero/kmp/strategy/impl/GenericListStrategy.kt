package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 🎯 通用列表选择策略
 *
 * 自动识别 List<T> 和 Set<T> 类型字段，生成通用选择器组件
 *
 * 支持的字段模式：
 * - 类型为: List<T>, Set<T>, MutableList<T>, MutableSet<T>
 * - 自动推断数据类型和生成对应的选择器
 * - 支持树形数据和列表数据
 */
object GenericListStrategy : FormStrategy {

    override val name: String = "GenericListStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        if (!isCollectionType) {
            return 0
        }

        // 获取集合的泛型类型
        val genericType = prop.type.resolve().arguments.firstOrNull()?.type?.resolve()
        val genericDeclaration = genericType?.declaration

        println("GenericListStrategy 调试: ${prop.simpleName.asString()}")
        println("  - 是否为集合类型: $isCollectionType")
        println("  - 泛型类型: ${genericType?.toString()}")
        println("  - 泛型声明: ${genericDeclaration?.simpleName?.asString()}")

        if (genericDeclaration == null) {
            println("  - 泛型声明为空，不支持")
            return 0
        }

        // 检查泛型类型是否为 Jimmer 实体
        val isJimmerEntityType = isJimmerEntity(genericDeclaration)

        // 检查泛型类型是否为枚举
        val isEnumType = isEnum(genericDeclaration)

        println("  - 是否为 Jimmer 实体: $isJimmerEntityType")
        println("  - 是否为枚举: $isEnumType")

        // 使用布尔值 + 操作符累加计算权重
        val weight = isCollectionType +
                isJimmerEntityType +
                (!isEnumType)

        println("  - 最终权重: $weight")
        return weight
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName
        val typeOrGenericClassDeclaration = prop.firstTypeArgumentKSClassDeclaration
        if (typeOrGenericClassDeclaration == null) {
            println("GenericListStrategy.genCode 错误: 无法获取 ${name} 的泛型类型")
            println("  - 属性类型: ${prop.type.resolve()}")
            println("  - 类型参数: ${prop.type.resolve().arguments}")
            throw IllegalStateException("未找到${name}集合动态表单的泛型类型，属性类型: ${prop.type.resolve()}")
        }

        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        // 新逻辑：查找字段类型的属性中带有 @LabelProp 注解的属性
        val labelField = findLabelPropInType(typeOrGenericClassDeclaration)
        val istree = typeOrGenericClassDeclaration.hasProperty("children")
        val simpleName = typeOrGenericClassDeclaration.simpleName.asString()

        // 只有 Jimmer 实体才添加 Iso 后缀
        val isJimmerEntityType = isJimmerEntity(typeOrGenericClassDeclaration)
        val isoTypeName = if (isJimmerEntityType) "${simpleName}Iso" else simpleName

        val treedsl = if (istree) """getChildren = { it.children?:emptyList() }""" else ""
        return if (isJimmerEntityType) {
            // Jimmer 实体类型：使用 Iso2DataProvider 加载数据
            """
            |        ${entityClassName}FormProps.$name to {
            |            var dataList by remember { mutableStateOf<List<${isoTypeName}>>(emptyList()) }
            |
            |            LaunchedEffect(Unit) {
            |                try {
            |                    val provider = Iso2DataProvider.isoToDataProvider[${isoTypeName}::class]
            |                    dataList = provider?.invoke("") as? List<${isoTypeName}> ?: emptyList()
            |                } catch (e: Exception) {
            |                    println("加载 $name 数据失败: ${'$'}{e.message}")
            |                    dataList = emptyList()
            |                }
            |            }
            |
            |            AddGenericMultiSelector(
            |                value = state.value.$name ?: emptyList(),
            |                onValueChange = { state.value = state.value.copy($name = it) },
            |                placeholder = $label,
            |                dataProvider = { dataList },
            |                getId = { it.id ?: 0L },
            |                getLabel = { it.$labelField ?: "" },
            |                $treedsl
            |            )
            |        }
            """.trimMargin()
        } else {
            // 非 Jimmer 实体类型：直接使用静态数据或其他方式
            """
            |        ${entityClassName}FormProps.$name to {
            |            AddGenericMultiSelector(
            |                value = state.value.$name ?: emptyList(),
            |                onValueChange = { state.value = state.value.copy($name = it) },
            |                placeholder = $label,
            |                dataProvider = { emptyList<$isoTypeName>() }, // 需要根据具体类型提供数据
            |                getId = { it.toString() },
            |                getLabel = { it.toString() },
            |               $treedsl 
            |                
            |            )
            |        }
            """.trimMargin()
        }
    }

    /**
     * 在类型的属性中查找带有 @LabelProp 注解的属性
     * 如果有多个 @LabelProp 属性，返回第一个非空的那个
     */
    private fun findLabelPropInType(classDeclaration: KSClassDeclaration): String {
        try {
            // 获取类型的所有属性
            val properties = classDeclaration.getAllProperties()

            // 查找所有带有 @LabelProp 注解的属性
            val labelProperties = properties.filter { property ->
                property.annotations.any { annotation ->
                    annotation.shortName.asString() == "LabelProp"
                }
            }.toList()

            if (labelProperties.isNotEmpty()) {
                // 如果有多个 @LabelProp 属性，选择第一个非空的
                val selectedProperty = labelProperties.firstOrNull { property ->
                    val propertyName = property.simpleName.asString()
                    // 这里可以添加更复杂的非空检查逻辑
                    // 目前简单返回第一个找到的
                    propertyName.isNotBlank()
                } ?: labelProperties.first()

                val labelFieldName = selectedProperty.simpleName.asString()
                if (labelProperties.size > 1) {
                    println("找到多个 @LabelProp 标记的属性: ${classDeclaration.simpleName.asString()}, 选择: ${labelFieldName}")
                } else {
                    println("找到 @LabelProp 标记的属性: ${classDeclaration.simpleName.asString()}.${labelFieldName}")
                }
                return labelFieldName
            } else {
                println("在 ${classDeclaration.simpleName.asString()} 中未找到 @LabelProp 标记的属性，使用默认值 'name'")
                return "name"  // 默认使用 name 字段
            }
        } catch (e: Exception) {
            println("查找 @LabelProp 属性时发生错误: ${e.message}")
            return "name"  // 出错时使用默认值
        }
    }

    /**
     * 提取泛型类型
     */
    private fun extractGenericType(typeName: String): String {
        val regex = """(?:List|Set|MutableList|MutableSet)<(.+?)>""".toRegex()
        val matchResult = regex.find(typeName)
        return matchResult?.groupValues?.get(1)?.trim() ?: "Any"
    }

}
