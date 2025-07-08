package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 百分比策略
 */
object PercentageStrategy : FormStrategy {
    
    override val priority: Int = 2
    
    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("percentage", ignoreCase = true) ||
               ktName.contains("percent", ignoreCase = true) ||
               ktName.contains("rate", ignoreCase = true) ||
               ktName.contains("百分比", ignoreCase = true) ||
               ktName.contains("比率", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        return """
            PercentageField(
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
