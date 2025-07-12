package strategy.impl

import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * 🎯 单个对象选择策略
 *
 * 处理单个对象类型字段，生成单选选择器
 */
object GenericSingleStrategy : FormStrategy {

    override val priority: Int = 11 // 比 GenericListStrategy 稍低的优先级

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val typeName = prop.typeName
        val declaration = prop.type.resolve().declaration

        // 检查是否为 Jimmer 实体
        val isJimmerEntityType = isJimmerEntity(declaration)

        // 检查是否为枚举类型
        val isEnumType = isEnum(declaration)

        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        println("GenericSingleStrategy: ${declaration.simpleName.asString()}, isJimmerEntity: $isJimmerEntityType, isEnum: $isEnumType, isCollection: $isCollectionType")

        // 只支持 Jimmer 实体类型的单个对象（排除枚举类型和集合类型）
        return isJimmerEntityType &&
                !isEnumType &&  // 排除枚举类型
                !isCollectionType &&  // 排除集合类型
                !isBasicType(typeName)
    }

    private fun isBasicType(typeName: String): Boolean {
        val basicTypes = setOf(
            "String", "Long", "Int", "Boolean", "Double", "Float",
            "BigDecimal", "LocalDate", "LocalDateTime", "Instant"
        )
        return basicTypes.any { typeName.contains(it) }
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName.replace("?", "").trim()
        val argFirstValue = prop.getAnno("LabelProp").getArgFirstValue()
        val declaration = prop.type.resolve().declaration
        val typeOrGenericClassDeclaration = declaration as KSClassDeclaration
        return genCodeWhenSingle(typeOrGenericClassDeclaration, typeName, name, argFirstValue, isRequired,true)
    }

}
