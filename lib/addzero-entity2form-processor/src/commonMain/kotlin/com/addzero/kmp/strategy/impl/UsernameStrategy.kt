package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.addzero.kmp.util.plus

/**
 * URL策略
 */
object UsernameStrategy : FormStrategy {

    override val name: String = "UsernameStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val ktName = prop.name

        return ktName.contains("username", ignoreCase = true) +
               ktName.contains("user", ignoreCase = true) +
               ktName.contains("用户名", ignoreCase = true) +
               ktName.equals("username", ignoreCase = true) +
               ktName.equals("userName", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddUsernameField(
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
