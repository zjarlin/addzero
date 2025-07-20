package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.addzero.kmp.util.plus
import com.addzero.kmp.util.typeName
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 布尔策略
 */
object BooleanStrategy : FormStrategy {

    override val name: String = "BooleanStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val typeName = prop.typeName
        val ktName = prop.name

        // 使用布尔值 + 操作符累加计算权重
        return (typeName == "Boolean") +
                ktName.contains("enable", ignoreCase = true) +
                ktName.contains("active", ignoreCase = true) +
                ktName.contains("valid", ignoreCase = true) +
                ktName.startsWith("is", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label

        // 从 parentDeclaration 获取实体类名
        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddSwitchField(
            |                value = state.value.$name ?: false,
            |                onValueChange = { state.value = state.value.copy($name = it) },
            |                label = $label
            |            )
            |        }
        """.trimMargin()
    }
}
