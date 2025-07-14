# 表单字段生成器

## 🎯 最终方案：原始when语句

经过多种设计模式的尝试，最终选择了最原始、最简单的when语句实现。

### ✅ 优势

1. **性能最佳**: 只解析一次 `KSType`，避免重复调用 `property.type.resolve()`
2. **简单直接**: 没有复杂的设计模式，代码一目了然
3. **易于维护**: 所有逻辑在一个文件中，修改方便
4. **零抽象开销**: 没有额外的抽象层，性能最优

### 📁 文件结构

```
lib/addzero-entity2form-processor/src/commonMain/kotlin/
└── GenFormUtil.kt  # 所有逻辑都在这一个文件中
```

### 🔧 核心实现

```kotlin
fun KSPropertyDeclaration.generateDifferentTypes(): String {
    val type = this.type.resolve() // 只解析一次！
    val ktName = getKtName(this)
    val typeName = type.declaration.simpleName.asString()
    
    return when {
        // 金额字段
        ktName.contains("money", ignoreCase = true) -> generateMoneyField(this, type)
        
        // 百分比字段  
        ktName.contains("percentage", ignoreCase = true) -> generatePercentageField(this, type)
        
        // 手机号字段
        ktName.contains("phone", ignoreCase = true) -> generatePhoneField(this, type)
        
        // ... 其他字段类型
        
        // 默认字段
        else -> generateDefaultField(this, type)
    }
}
```

### 💡 添加新字段类型

只需在 `GenFormUtil.kt` 中：

1. **添加when条件**:
```kotlin
// 新字段类型
ktName.contains("yourKeyword", ignoreCase = true) -> generateYourField(this, type)
```

2. **添加生成方法**:
```kotlin
private fun generateYourField(property: KSPropertyDeclaration, type: KSType): String {
    val name = getPropertyCamelName(property)
    val label = getPropertyLabel(property)
    val isRequired = isRequired(type)
    
    return """
        YourCustomField(
            value = state.value.$name?.toString() ?: "",
            onValueChange = { /* 处理逻辑 */ },
            label = $label,
            isRequired = $isRequired
        )
    """.trimIndent()
}
```

### 🎉 支持的字段类型

- ✅ **金额字段**: MoneyField
- ✅ **百分比字段**: PercentageField  
- ✅ **手机号字段**: AddTextField + RegexEnum.PHONE
- ✅ **邮箱字段**: AddTextField + RegexEnum.EMAIL
- ✅ **URL字段**: AddTextField + RegexEnum.URL
- ✅ **身份证字段**: AddTextField + RegexEnum.ID_CARD
- ✅ **银行卡字段**: AddTextField + RegexEnum.BANK_CARD
- ✅ **整数字段**: IntegerField
- ✅ **小数字段**: DecimalField
- ✅ **日期字段**: AddTextField + RegexEnum.DATE
- ✅ **布尔字段**: Switch
- ✅ **默认字段**: AddTextField

### 🚀 性能特点

- **单次解析**: `property.type.resolve()` 只调用一次
- **零抽象**: 没有策略模式等抽象层开销
- **直接匹配**: when语句直接匹配，无需遍历列表
- **内联优化**: 编译器可以更好地优化when语句

这个方案虽然看起来"原始"，但实际上是最高效、最直接的解决方案！🎯
