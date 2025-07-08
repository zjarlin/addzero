package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 金额策略
 */
object MoneyStrategy : FormStrategy {

    override val priority: Int = 1

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("money", ignoreCase = true) ||
               ktName.contains("amount", ignoreCase = true) ||
               ktName.contains("price", ignoreCase = true) ||
               ktName.contains("金额", ignoreCase = true) ||
               ktName.contains("价格", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

        // 根据字段名称智能判断货币类型
        val currency = when {
            name.contains("usd", ignoreCase = true) ||
            name.contains("dollar", ignoreCase = true) ||
            name.contains("美元", ignoreCase = true) -> "USD"

            name.contains("eur", ignoreCase = true) ||
            name.contains("euro", ignoreCase = true) ||
            name.contains("欧元", ignoreCase = true) -> "EUR"

            name.contains("gbp", ignoreCase = true) ||
            name.contains("pound", ignoreCase = true) ||
            name.contains("英镑", ignoreCase = true) -> "GBP"

            name.contains("jpy", ignoreCase = true) ||
            name.contains("yen", ignoreCase = true) ||
            name.contains("日元", ignoreCase = true) -> "JPY"

            else -> "CNY" // 默认人民币
        }

        return """
            AddMoneyField(
                value = state.value.$name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
                },
                label = $label,
                isRequired = $isRequired,
                currency = "$currency"
            )
        """.trimIndent()
    }
}
