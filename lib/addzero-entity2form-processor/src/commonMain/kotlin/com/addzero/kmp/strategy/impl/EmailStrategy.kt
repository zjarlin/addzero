package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 邮箱策略
 */
object EmailStrategy : FormStrategy {

    override val name: String = "EmailStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val ktName = prop.name

        // 使用布尔值 + 操作符累加计算权重，每个条件都是平等的 0 或 1
        return ktName.contains("email", ignoreCase = true) +
                ktName.contains("mail", ignoreCase = true) +
                ktName.contains("邮箱", ignoreCase = true) +
                ktName.contains("邮件", ignoreCase = true) +
                ktName.equals("email", ignoreCase = true) +
                ktName.equals("userEmail", ignoreCase = true) +
                ktName.equals("contactEmail", ignoreCase = true)
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
            |            AddEmailField(
            |                value = state.value.$name?.toString() ?: "",
            |                onValueChange = {
            |                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
            |                },
            |                showCheckEmail = false,
            |                label = $label,
            |                isRequired = $isRequired
            |            )
            |        }
        """.trimMargin()
    }
}
