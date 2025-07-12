package strategy.impl

import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 🎯 通用列表选择策略
 *
 * 自动识别 List<T> 和 Set<T> 类型字段，生成通用选择器组件
 *
 * 支持的字段模式：
 * - 类型为: List<T>, Set<T>, MutableList<T>, MutableSet<T>
 * - 自动推断数据类型和生成对应的选择器
 * - 支持树形数据和列表数据
 */
object GenericListStrategy : FormStrategy {

    override val priority: Int = 10 // 较低优先级，作为通用兜底策略

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val typeName = prop.typeName

        // 支持各种集合类型
        return typeName.contains("List<") ||
                typeName.contains("Set<") ||
                typeName.contains("MutableList<") ||
                typeName.contains("MutableSet<")
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName
        val typeOrGenericClassDeclaration = prop.firstTypeArgumentKSClassDeclaration?:throw IllegalStateException("未找到集合动态表单的泛型类型")

        // 提取泛型类型
//        val genericType = extractGenericType(typeName)

        // 判断集合类型
        val isSet = typeName.contains("Set")
        val collectionType = if (isSet) "toSet()" else ""

        val argFirstValue = prop.getAnno("LabelProp").getArgFirstValue()


        return genCodeWhenSingle(typeOrGenericClassDeclaration, typeName, name, argFirstValue, isRequired,false)

    }

    /**
     * 提取泛型类型
     */
    private fun extractGenericType(typeName: String): String {
        val regex = """(?:List|Set|MutableList|MutableSet)<(.+?)>""".toRegex()
        val matchResult = regex.find(typeName)
        return matchResult?.groupValues?.get(1)?.trim() ?: "Any"
    }

}

fun genCodeWhenSingle(bool: KSClassDeclaration, typeName: String, name: String, argFirstValue: String?, isRequired: Boolean, isSingle: Boolean = true): String {
    val hasProperty = bool.hasProperty("children")
    val string = if (hasProperty) {
        "it.children"
    } else {
        "emptyList()"
    }


    val isoTypeName = """${typeName}Iso"""

    val whenSingleFunName =
        if (isSingle) {
            """AddGenericSingleSelector"""
        } else {
            """AddGenericSelector"""
        }

    val whenSingleAsName =
        if (isSingle) {
            isoTypeName
        } else {
            "List<$isoTypeName>"
        }


    val trimIndent = """
                val ${name}DataProvider = remember {
                              val dataProviderFunction = isoToDataProvider[$isoTypeName::class] ?: throw IllegalStateException("未找到 $typeName 的数据提供者，请在Iso2DataProvider注册")
                }
    
                $whenSingleFunName(
                    value = state.value.$name,
                    onValueChange = { 
                        state.value = state.value.copy($name = it as $whenSingleAsName)
                    },
                    dataProvider = {
                        
                           if (  ${name}DataProvider == null) {
                    emptyList()
                } else {
                      ${name}DataProvider().invoke("") as List<$isoTypeName>
                } 
                    },
                    getId = {it.id!! },
                    getLabel = { it.   ${argFirstValue ?: "name"}   },
                    
                    getChildren = { 
                $string
                    },
                    allowClear = ${!isRequired},
                )
            """.trimIndent()
    return trimIndent
}
