package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.addzero.kmp.util.typeName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.addzero.kmp.util.plus

/**
 * 日期策略
 */
object DateStrategy : FormStrategy {
    
    override val name: String = "DateStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val ktName = prop.name
        val typeName = prop.typeName

        return ktName.contains("date", ignoreCase = true) +
               ktName.contains("time", ignoreCase = true) +
               ktName.contains("日期", ignoreCase = true) +
               ktName.contains("时间", ignoreCase = true) +
               (typeName == "LocalDate") +
               (typeName == "LocalDateTime") +
               (typeName == "Instant") +
               ktName.equals("date", ignoreCase = true) +
               ktName.equals("createTime", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName

        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        val dateType = when (typeName) {
            "LocalDate" -> "DateType.DATE"
            "LocalDateTime" -> "DateType.DATETIME"
            "Instant" -> "DateType.DATETIME"
            else -> "DateType.DATE"
        }

        return """
            |        ${entityClassName}FormProps.$name to {
            |            AddDateField(
            |                value = state.value.$name,
            |                onValueChange = {
            |                    state.value = state.value.copy($name = it)
            |                },
            |                label = $label,
            |                isRequired = $isRequired,
            |                dateType = $dateType
            |            )
            |        }
        """.trimMargin()
    }
}
