package com.addzero.kmp.strategy

// import com.addzero.kmp.strategy.impl.DateStrategy
// import com.addzero.kmp.strategy.impl.DecimalStrategy
// import com.addzero.kmp.strategy.impl.EmailStrategy
// import com.addzero.kmp.strategy.impl.GenericListStrategy
// import com.addzero.kmp.strategy.impl.GenericSingleStrategy
// import com.addzero.kmp.strategy.impl.IdCardStrategy
// import com.addzero.kmp.strategy.impl.IntegerStrategy
// import com.addzero.kmp.strategy.impl.PasswordStrategy
// import com.addzero.kmp.strategy.impl.PercentageStrategy
// import com.addzero.kmp.strategy.impl.PhoneStrategy
// import com.addzero.kmp.strategy.impl.UrlStrategy
// import com.addzero.kmp.strategy.impl.UsernameStrategy
import com.addzero.kmp.strategy.impl.*
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 表单策略基类
 *
 * 🎯 基于权重的策略模式：
 * 1. 每个策略计算对属性的匹配权重
 * 2. 权重最高的策略被选中
 * 3. 支持用户扩展新策略
 */
interface FormStrategy {
    /**
     * 策略名称，用于调试和日志
     */
    val name: String

    /**
     * 计算对指定属性的匹配权重
     *
     * @param prop 属性声明
     * @return 匹配权重，0 表示不匹配，数字越大权重越高
     */
    fun calculateWeight(prop: KSPropertyDeclaration): Int

    /**
     * 生成代码
     *
     * @param prop 属性声明
     * @return 生成的表单字段代码
     */
    fun genCode(prop: KSPropertyDeclaration): String

}

/**
 * 策略管理器
 */
object FormStrategyManager {
    val strategies = mutableListOf<FormStrategy>(
        // 枚举策略（高优先级）
        EnumListStrategy,
        EnumStrategy,

        // 专用策略
        MoneyStrategy,
        BooleanStrategy,
        PasswordStrategy,
        EmailStrategy,
        IntegerStrategy,
        DecimalStrategy,
        PhoneStrategy,
        UrlStrategy,
        UsernameStrategy,
        IdCardStrategy,
        PercentageStrategy,
        BankCardStrategy,
        DateStrategy,
        DateTimeStrategy,

        // 通用策略（低优先级）
        GenericSingleStrategy,
        GenericListStrategy,
        StringStrategy
    )

    /**
     * 生成代码
     */
    fun generateCode(property: KSPropertyDeclaration): String {

        val bestStrategyWithWeight = strategies
            .map { strategy -> strategy to strategy.calculateWeight(property) }
            .filter { (_, weight) -> weight > 0 }
            .maxByOrNull { (_, weight) -> weight }

        if (bestStrategyWithWeight == null) {
            throw IllegalStateException("没有找到适合属性 ${property.simpleName.asString()} 的策略")
        }

        val (bestStrategy, maxWeight) = bestStrategyWithWeight
        println("属性 ${property.simpleName.asString()} 选择策略: ${bestStrategy.name}，权重: $maxWeight")

        return bestStrategy.genCode(property)
    }

    /**
     * 过滤 BaseEntity 字段
     */
    fun shouldFilterBaseEntity(property: KSPropertyDeclaration): Boolean {
        val propertyName = property.simpleName.asString()
        val baseEntityFields = setOf(
            "id", "createTime", "updateTime", "createBy", "updateBy",
            "deleted", "version", "tenantId"
        )
        return propertyName in baseEntityFields
    }
}
