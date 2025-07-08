package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * URL策略
 */
object UrlStrategy : FormStrategy {
    
    override val priority: Int = 5
    
    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("url", ignoreCase = true) ||
               ktName.contains("link", ignoreCase = true) ||
               ktName.contains("website", ignoreCase = true) ||
               ktName.contains("网址", ignoreCase = true) ||
               ktName.contains("链接", ignoreCase = true)
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
                isRequired = $isRequired,
                regexEnum = RegexEnum.URL
            )
        """.trimIndent()
    }
}
