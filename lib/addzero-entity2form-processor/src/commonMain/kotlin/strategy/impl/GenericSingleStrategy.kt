package strategy.impl

import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 🎯 单个对象选择策略
 *
 * 处理单个对象类型字段，生成单选选择器
 */
object GenericSingleStrategy : FormStrategy {

    override val priority: Int = 11 // 比 GenericListStrategy 稍低的优先级

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val typeName = prop.typeName
        val declaration = prop.type.resolve().declaration

        // 检查是否为 Jimmer 实体
        val isJimmerEntityType = isJimmerEntity(declaration)

        // 检查是否为枚举类型
        val isEnumType = isEnum(declaration)

        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        println("GenericSingleStrategy: ${declaration.simpleName.asString()}, isJimmerEntity: $isJimmerEntityType, isEnum: $isEnumType, isCollection: $isCollectionType")

        // 只支持 Jimmer 实体类型的单个对象（排除枚举类型和集合类型）
        return isJimmerEntityType &&
                !isEnumType &&  // 排除枚举类型
                !isCollectionType &&  // 排除集合类型
                !isBasicType(typeName)
    }

    private fun isBasicType(typeName: String): Boolean {
        val basicTypes = setOf(
            "String", "Long", "Int", "Boolean", "Double", "Float",
            "BigDecimal", "LocalDate", "LocalDateTime", "Instant"
        )
        return basicTypes.any { typeName.contains(it) }
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

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName.replace("?", "").trim()
        val declaration = prop.type.resolve().declaration
        val typeOrGenericClassDeclaration = declaration as KSClassDeclaration

        // 新逻辑：查找字段类型的属性中带有 @LabelProp 注解的属性
        val argFirstValue = findLabelPropInType(typeOrGenericClassDeclaration)
        return genCodeWhenSingle(typeOrGenericClassDeclaration, typeName, name, argFirstValue, isRequired, label = label)
    }

}
