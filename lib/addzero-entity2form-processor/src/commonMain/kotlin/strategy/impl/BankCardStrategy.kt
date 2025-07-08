package strategy.impl

import com.addzero.kmp.util.JlStrUtil.makeSurroundWith
import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isNullable
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.addzero.kmp.util.removeAnyQuote
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 银行卡策略
 */
object BankCardStrategy : FormStrategy {

    override val priority: Int = 7

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("bankcard", ignoreCase = true) ||
                ktName.contains("cardno", ignoreCase = true) ||
                ktName.contains("银行卡", ignoreCase = true) ||
                ktName.contains("卡号", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {

        val name = prop.name
        val isRequired = !prop.isNullable()
        // 生成默认值
        val defaultValue = prop.defaultValue()
        val label = prop.label


        return """
            AddTextField(
                value = state.value.$name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
                },
                label = $label,
                isRequired = $isRequired,
                regexEnum = RegexEnum.BANK_CARD
            )
        """.trimIndent()
    }
}
