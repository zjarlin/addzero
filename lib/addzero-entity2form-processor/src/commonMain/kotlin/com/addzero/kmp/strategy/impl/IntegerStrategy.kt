package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 整数策略
 */
object IntegerStrategy : FormStrategy {

    override val name: String = "IntegerStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val typeName = prop.typeName

        // 使用布尔值 + 操作符累加计算权重，每个条件都是平等的 0 或 1
        return (typeName == "Int") +
                (typeName == "Long") +
                (typeName == "Short") +
                (typeName == "Byte")
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        // 从 parentDeclaration 获取实体类名
        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddIntegerField(
            |                value = state.value.$name?.toString() ?: "",
            |                onValueChange = {
            |                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
            |                },
            |                label = $label,
            |                isRequired = $isRequired
            |            )
            |        }
        """.trimMargin()
    }
}
