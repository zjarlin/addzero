package strategy.impl

import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.addzero.kmp.util.typeName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 布尔策略
 */
object BooleanStrategy : FormStrategy {

    override val priority: Int = 11

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val typeName = prop.typeName
        return typeName == "Boolean"
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label

        return """
            
        Row(verticalAlignment = Alignment.CenterVertically) {
        Text($label)
            Switch(
                checked = state.value.$name ?: false,
                onCheckedChange = {
                    state.value = state.value.copy($name = it)
                },
            )
            
            Text(
                text = if (  state.value as? Boolean == true) "是" else "否",
                modifier = Modifier.width(40.dp)
            )
        
        }
            
        """.trimIndent()
    }
}
