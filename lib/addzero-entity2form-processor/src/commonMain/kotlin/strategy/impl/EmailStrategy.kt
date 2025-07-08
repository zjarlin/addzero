package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 邮箱策略
 */
object EmailStrategy : FormStrategy {

    override val priority: Int = 4

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("email", ignoreCase = true) ||
               ktName.contains("mail", ignoreCase = true) ||
               ktName.contains("邮箱", ignoreCase = true) ||
               ktName.contains("邮件", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        return """
        AddEmailField(
                value = state.value.$name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
                },
                showCheckEmail=false,
                label = $label,
                isRequired = $isRequired,
            )
        """.trimIndent()
    }
}
