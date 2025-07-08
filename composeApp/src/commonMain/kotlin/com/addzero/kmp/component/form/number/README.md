# 数字输入框组件

基于 `AddTextField` 的专用数字输入组件集合，提供四种不同类型的数字输入框：整数、小数、金额、百分比。

## 组件列表

### 1. AddIntegerField - 整数输入框

专用于整数输入的组件，支持正负数控制。

**特性：**
- 基于 `RegexEnum.INTEGER` 和 `RegexEnum.POSITIVE_INTEGER` 验证
- 支持允许/禁止负数
- 自动使用数字键盘
- 默认图标：Numbers

**使用示例：**
```kotlin
// 基础整数输入
AddIntegerField(
    value = integerValue,
    onValueChange = { integerValue = it },
    label = "整数（允许负数）"
)

// 正整数输入
AddIntegerField(
    value = positiveValue,
    onValueChange = { positiveValue = it },
    label = "正整数",
    allowNegative = false
)
```

### 2. AddDecimalField - 小数输入框

专用于小数输入的组件。

**特性：**
- 基于 `RegexEnum.DECIMAL` 验证
- 自动使用小数键盘
- 默认图标：Numbers

**使用示例：**
```kotlin
// 基础小数输入
AddDecimalField(
    value = decimalValue,
    onValueChange = { decimalValue = it },
    label = "小数"
)
```

### 3. AddMoneyField - 金额输入框

专用于金额输入的组件，基于 `RegexEnum.MONEY` 验证。

**特性：**
- 基于 `RegexEnum.MONEY` 验证（只允许正数）
- 支持货币符号显示
- 自动使用小数键盘
- 默认图标：AttachMoney

**使用示例：**
```kotlin
// 基础金额输入
AddMoneyField(
    value = moneyValue,
    onValueChange = { moneyValue = it },
    label = "金额",
    currency = "¥"
)
```

### 4. AddPercentageField - 百分比输入框

专用于百分比输入的组件。

**特性：**
- 基于 `RegexEnum.DECIMAL` 验证
- 可选择显示/隐藏百分号
- 自动使用小数键盘
- 默认图标：Percent

**使用示例：**
```kotlin
// 基础百分比输入
AddPercentageField(
    value = percentageValue,
    onValueChange = { percentageValue = it },
    label = "百分比"
)

// 不显示百分号
AddPercentageField(
    value = rateValue,
    onValueChange = { rateValue = it },
    label = "比率",
    showPercentSymbol = false
)
```

## 共同特性

所有数字输入框都继承了 `AddTextField` 的完整功能：

- **验证系统**：正则表达式验证 + 自定义验证器
- **错误处理**：实时错误显示和错误消息回调
- **远程验证**：支持服务器端验证
- **表单集成**：与表单系统完全兼容
- **自定义样式**：支持图标、占位符、支持文本等
- **禁用状态**：支持禁用输入
- **必填验证**：支持必填字段验证

## 验证逻辑

### 整数验证
- 使用 `RegexEnum.INTEGER`（允许负数）或 `RegexEnum.POSITIVE_INTEGER`（仅正数）

### 小数验证
- 使用 `RegexEnum.DECIMAL`

### 金额验证
- 使用 `RegexEnum.MONEY`（仅允许正数和小数）

### 百分比验证
- 使用 `RegexEnum.DECIMAL`

## 键盘类型

- **整数输入框**：`KeyboardType.Number`
- **小数输入框**：`KeyboardType.Decimal`
- **金额输入框**：`KeyboardType.Decimal`
- **百分比输入框**：`KeyboardType.Decimal`

## 示例代码

完整的使用示例请参考 `NumberFieldExample.kt` 文件。

## 注意事项

1. 所有组件都基于 `AddTextField`，保持了一致的API设计
2. 验证逻辑直接使用 `RegexEnum` 中的预定义模式
3. 所有组件都支持远程验证和表单集成
4. 错误消息支持中文显示
