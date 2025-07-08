package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 手机号策略
 */
object PhoneStrategy : FormStrategy {

    override val priority: Int = 3

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("phone", ignoreCase = true) ||
               ktName.contains("mobile", ignoreCase = true) ||
               ktName.contains("手机", ignoreCase = true) ||
               ktName.contains("电话", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        return """
            AddTextField(
                value = state.value.$name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
                },
                label = $label,
               leadingIcon = Icons.Default.Phone,
                isRequired = $isRequired,
                regexEnum = RegexEnum.PHONE
            )
        """.trimIndent()
    }
}
