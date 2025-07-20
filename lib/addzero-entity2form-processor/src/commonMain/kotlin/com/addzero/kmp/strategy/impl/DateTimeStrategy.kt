package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 日期时间策略
 */
object DateTimeStrategy : FormStrategy {

    override val name: String = "DateStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val ktName = prop.name
        val typeName = prop.typeName

        return ktName.contains("datetime", ignoreCase = true) +
                ktName.contains("日期时间", ignoreCase = true) +
                (typeName == "LocalDateTime") +
                ktName.equals("datetime", ignoreCase = true)
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
            |            AddDateTimeField(
            |                value = state.value.$name,
            |                onValueChange = {
            |                if(it==null){
            |                    state.value = state.value
            |                }else{
            |                    state.value = state.value.copy($name = it!!)
            |                }
            |                },
            |                label = $label,
            |                isRequired = $isRequired,
            |            )
            |        }
        """.trimMargin()
    }
}
