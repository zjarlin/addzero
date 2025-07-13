package strategy.impl

import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

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

    override val priority: Int = 10 // 较低优先级，作为通用兜底策略

    override fun support(prop: KSPropertyDeclaration): Boolean {
        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        if (!isCollectionType) {
            return false
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
            return false
        }

        // 检查泛型类型是否为 Jimmer 实体
        val isJimmerEntityType = isJimmerEntity(genericDeclaration)

        // 检查泛型类型是否为枚举
        val isEnumType = isEnum(genericDeclaration)

        println("  - 是否为 Jimmer 实体: $isJimmerEntityType")
        println("  - 是否为枚举: $isEnumType")
        println("  - 最终支持: ${isJimmerEntityType && !isEnumType}")

        // 只支持 Jimmer 实体类型的集合（排除枚举类型）
        return isJimmerEntityType && !isEnumType
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

        // 提取泛型类型
//        val genericType = extractGenericType(typeName)

        // 判断集合类型
        val isSet = typeName.contains("Set")
        val collectionType = if (isSet) "toSet()" else ""

        // 新逻辑：查找字段类型的属性中带有 @LabelProp 注解的属性
        val argFirstValue = findLabelPropInType(typeOrGenericClassDeclaration)


        return genCodeWhenSingle(typeOrGenericClassDeclaration, typeName, name, argFirstValue, isRequired, false, label)

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

fun genCodeWhenSingle(bool: KSClassDeclaration, typeName: String, name: String, argFirstValue: String?, isRequired: Boolean, isSingle: Boolean = true, label: String): String {
    // 检查是否为枚举类型
    val isEnum = bool.classKind.name == "ENUM_CLASS"

    // 如果是枚举类型，不生成通用选择器代码
    if (isEnum) {
        return """
            // 🚫 枚举类型 $typeName 不支持通用选择器，请使用专门的枚举选择器
            // TODO: 为枚举类型 $typeName 实现专门的选择器组件
            Text("枚举类型 $typeName 暂不支持", color = MaterialTheme.colorScheme.error)
        """.trimIndent()
    }

    val hasProperty = bool.hasProperty("children")
    val string = if (hasProperty) {
        "it.children"
    } else {
        "emptyList()"
    }

    val simpleName = bool.simpleName.asString()

    val isoTypeName = """${simpleName}Iso"""

    val whenSingleFunName =
        if (isSingle) {
            """AddGenericSingleSelector"""
        } else {
            """AddGenericSelector"""
        }

    val whenSingleAsName =
        if (isSingle) {
            isoTypeName
        } else {
            "List<$isoTypeName>"
        }


    val genericFullName =


        if (isSingle) {
            simpleName
        } else {
            "List<$simpleName>"
        }


    val trimIndent = """
                val ${name}DataProvider = remember {
                              val dataProviderFunction = isoToDataProvider[$isoTypeName::class] ?: throw IllegalStateException("未找到 val $name: $genericFullName 的数据提供者，请在Iso2DataProvider注册")
                             dataProviderFunction 
                }
    
                $whenSingleFunName(
                    value = state.value.$name,
                    onValueChange = { 
                        state.value = state.value.copy($name = it as $whenSingleAsName)
                    },
                    dataProvider = {
                        
                           if (  ${name}DataProvider == null) {
                    emptyList()
                } else {
                      ${name}DataProvider().invoke("") as List<$isoTypeName>
                } 
                    },
                    getId = {it.id!! },
                    getLabel = { it.   ${argFirstValue ?: "name"}   },
                    
                    getChildren = { 
                $string
                    },
                       placeholder = "请选择"+$label, 
                    allowClear = ${!isRequired},
                )
            """.trimIndent()
    return trimIndent
}
