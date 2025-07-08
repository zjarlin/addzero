package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.addzero.kmp.util.typeName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 整数策略
 */
object IntegerStrategy : FormStrategy {

    override val priority: Int = 8

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val typeName = prop.typeName
        return typeName in setOf("Int", "Long", "Short", "Byte")
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        return """
                     AddIntegerField(
                value = state.value.$name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
                },
                label = $label,
                isRequired = $isRequired
            )
        """.trimIndent()
    }
}
