import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategyManager

/**
 * 根据字段类型渲染不同模板
 * generateDifferentTypes
 *
 * 🎯 自动注册策略版本：
 * 1. ✅ 性能优化：策略内部只解析一次 KSType
 * 2. ✅ 自动注册：每个策略自动往 strategies 里 add
 * 3. ✅ 简单扩展：添加新策略只需创建 object 并实现接口
 * 4. ✅ 零配置：无需手动管理策略列表
 */
fun KSPropertyDeclaration.generateDifferentTypes(): String {
    // 使用策略管理器生成代码
    return FormStrategyManager.generateCode(this)
}
