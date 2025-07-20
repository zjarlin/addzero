package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 枚举策略
 * 处理单个枚举类型字段，使用 AddEnumSelector
 */
object EnumStrategy : FormStrategy {

    override val name: String = "EnumStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val declaration = prop.type.resolve().declaration

        // 检查是否为枚举类型
        val isEnumType = isEnum(declaration)

        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        // 只处理单个枚举类型，不处理集合
        return if (isEnumType && !isCollectionType) 10 else 0
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName.replace("?", "").trim()

        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddGenericSingleSelector(
            |                value = state.value.$name,
            |                onValueChange = { state.value = state.value.copy($name = it) },
            |                placeholder = $label,
            |                dataProvider = { $typeName.entries },
            |                getId = { it.name },
            |                getLabel = { it.name }
            |            )
            |        }
        """.trimMargin()
    }
}

/**
 * 枚举列表策略
 * 处理枚举列表类型字段，使用 AddGenericMultiSelector
 */
object EnumListStrategy : FormStrategy {

    override val name: String = "EnumListStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        if (!isCollectionType) return 0

        // 获取泛型参数类型
        val genericDeclaration = prop.firstTypeArgumentKSClassDeclaration

        // 检查泛型类型是否为枚举
        val isEnumType = genericDeclaration?.let { isEnum(it) } ?: false

        println("EnumListStrategy 调试: ${prop.name}")
        println("  - 是否为集合类型: $isCollectionType")
        println("  - 泛型类型: ${genericDeclaration?.simpleName?.asString()}")
        println("  - 是否为枚举: $isEnumType")

        // 只处理枚举列表
        val weight = if (isCollectionType && isEnumType) 15 else 0
        println("  - 最终权重: $weight")

        return weight
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired

        // 获取泛型参数类型
        val genericDeclaration = prop.firstTypeArgumentKSClassDeclaration
        val enumTypeName = genericDeclaration?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取枚举类型名")

        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddGenericMultiSelector(
            |                value = state.value.$name ?: emptyList(),
            |                onValueChange = { state.value = state.value.copy($name = it) },
            |                placeholder = $label,
            |                dataProvider = { $enumTypeName.entries },
            |                getId = { it.name },
            |                getLabel = { it.name }
            |            )
            |        }
        """.trimMargin()
    }
}
