package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.addzero.kmp.util.typeName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 日期策略
 */
object DateStrategy : FormStrategy {
    
    override val priority: Int = 10
    
    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        val typeName = prop.typeName
        
        return ktName.contains("date", ignoreCase = true) ||
               ktName.contains("time", ignoreCase = true) ||
               ktName.contains("日期", ignoreCase = true) ||
               ktName.contains("时间", ignoreCase = true) ||
               typeName.contains("Date") ||
               typeName.contains("LocalDate") ||
               typeName.contains("LocalDateTime")
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
                regexEnum = RegexEnum.DATE
            )
        """.trimIndent()
    }
}
