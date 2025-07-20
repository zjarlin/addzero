package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 字符串策略（默认策略）
 *
 * 🎯 真正的自动注册：
 * 1. 继承 FormStrategy sealed class
 * 2. 在类加载时自动注册（通过父类init块）
 * 3. 无需手动管理策略列表
 */
object StringStrategy : FormStrategy {

    override val name: String = "StringStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        // 默认策略支持所有类型，但权重最低
        return 1
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        // 从 parentDeclaration 获取实体类名
        val entityClassName =
            (prop.parentDeclaration as? com.google.devtools.ksp.symbol.KSClassDeclaration)?.simpleName?.asString()
                ?: throw IllegalStateException("无法获取实体类名")

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddTextField(
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
