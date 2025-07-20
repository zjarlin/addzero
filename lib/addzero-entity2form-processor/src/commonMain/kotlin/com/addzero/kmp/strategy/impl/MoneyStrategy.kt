package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 金额策略
 */
object MoneyStrategy : FormStrategy {

    override val name: String = "MoneyStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val ktName = prop.name
        val typeName = prop.type.resolve().declaration.simpleName.asString()

        // 使用布尔值 + 操作符累加计算权重，每个条件都是平等的 0 或 1
        return ktName.contains("money", ignoreCase = true) +
                ktName.contains("amount", ignoreCase = true) +
                ktName.contains("price", ignoreCase = true) +
                ktName.contains("金额", ignoreCase = true) +
                ktName.contains("价格", ignoreCase = true) +
                ktName.contains("cost", ignoreCase = true) +
                ktName.contains("fee", ignoreCase = true) +
                ktName.contains("salary", ignoreCase = true) +
                ktName.contains("wage", ignoreCase = true) +
                (ktName.contains("total", ignoreCase = true) &&
                        typeName in setOf("BigDecimal", "Double", "Float")) +
                (typeName == "BigDecimal") +
                (typeName in setOf("Double", "Float"))
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
            |        ${entityClassName}FormProps.$name to {
            |            AddMoneyField(
            |                value = state.value.$name?.toString() ?: "",
            |                onValueChange = {
            |                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
            |                },
            |                label = $label,
            |                isRequired = $isRequired,
            |                currency = "$currency"
            |            )
            |        }
        """.trimMargin()
    }
}
