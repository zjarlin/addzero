package strategy

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.impl.*

/**
 * 表单策略基类
 *
 * 🎯 自动注册策略模式：
 * 1. 每个策略自动往 strategies 里 add
 * 2. support 和 genCode 方法只需要 KSPropertyDeclaration 参数
 * 3. 简单的自注册机制
 */
interface FormStrategy {
    /**
     * 优先级（数字越小优先级越高）
     */
    val priority: Int

    /**
     * 判断是否支持该属性
     */
    fun support(prop: KSPropertyDeclaration): Boolean

    /**
     * 生成代码
     */
    fun genCode(prop: KSPropertyDeclaration): String

}

/**
 * 策略管理器
 */
object FormStrategyManager {
    val strategies = mutableListOf<FormStrategy>(
        BankCardStrategy,
        BooleanStrategy,
        DateStrategy,
        DecimalStrategy,
        SysDeptSingleStrategy,     // 🏢 SysDept 单选策略 (优先级2)
        SysDeptsStrategy,  // 🏢 SysDeptIso 部门选择策略 (优先级3)
        EmailStrategy,
        IdCardStrategy,
        IntegerStrategy,
        MoneyStrategy,
        PercentageStrategy,
        PhoneStrategy,
        StringStrategy,
        UrlStrategy,
        UsernameStrategy,
        PasswordStrategy
    )

    /**
     * 生成代码
     */
    fun generateCode(property: KSPropertyDeclaration): String {
        // 确保所有策略都已加载

        println("策略有${strategies.size}个")

        // 按优先级排序
        val sortedStrategies = strategies.sortedBy { it.priority }

        // 找到第一个支持的策略
        val strategy = sortedStrategies.first { it.support(property) }
        return strategy.genCode(property)
    }
}
