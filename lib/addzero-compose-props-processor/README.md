# Compose $attrs 处理器

类似Vue的`$attrs`功能，为Compose函数生成参数打包，让你能够轻松地封装和传递组件属性。

## 功能特性

- 🎯 **自动参数打包**: 为Compose函数生成参数打包类
- 🔧 **类型安全**: 保持完整的类型信息和空值安全
- 🚀 **零运行时开销**: 编译时代码生成，无运行时性能损耗
- 📝 **智能排除**: 支持排除特定参数（如modifier、content等）
- 🎨 **Vue风格**: 提供类似Vue `$attrs`的开发体验

## 快速开始

### 1. 添加注解

在你的Compose函数上添加`@ComposeAttrs`注解：

```kotlin
@ComposeAttrs
@Composable
fun MyText(
    text: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    @AttrsExclude modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        modifier = modifier
    )
}
```

### 2. 编译项目

运行KSP处理器：

```bash
./gradlew build
```

### 3. 使用生成的$attrs

KSP将自动生成以下代码：

```kotlin
// 生成的数据类
data class MyTextAttrs(
    val text: String,
    val color: Color = Color.Black,
    val fontSize: TextUnit = 14.sp,
    val fontWeight: FontWeight = FontWeight.Normal
)

// $attrs扩展属性
val MyTextAttrs.$attrs: MyTextAttrs
    get() = this

// 构建器函数
fun myTextAttrs(
    text: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal
): MyTextAttrs {
    return MyTextAttrs(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}
```

### 4. 在你的组件中使用

```kotlin
@Composable
fun MyComponent() {
    // 方式1：直接使用构建器
    val textAttrs = myTextAttrs(
        text = "Hello World",
        color = Color.Blue,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    
    // 使用$attrs
    MyText(textAttrs.$attrs)
    
    // 方式2：直接构造
    val attrs = MyTextAttrs(
        text = "Another Text",
        color = Color.Red
    )
    
    MyText(attrs.$attrs)
}
```

## 注解说明

### @ComposeAttrs

标记需要生成$attrs的Compose函数。

```kotlin
@ComposeAttrs
@Composable
fun MyComponent(/* 参数 */) {
    // 组件实现
}
```

### @AttrsExclude

排除某些参数不包含在$attrs中，通常用于：
- `modifier`: UI修饰符
- `content`: Composable内容
- `onClick`: 事件回调

```kotlin
@ComposeAttrs
@Composable
fun MyComponent(
    title: String,
    @AttrsExclude modifier: Modifier = Modifier,
    @AttrsExclude onClick: () -> Unit = {}
) {
    // 组件实现
}
```

## 使用场景

### 1. 组件封装

```kotlin
@ComposeAttrs
@Composable
fun StyledText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface,
    @AttrsExclude modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}

// 使用
val commonTextAttrs = styledTextAttrs(
    style = MaterialTheme.typography.headlineMedium,
    color = Color.Blue
)

StyledText(commonTextAttrs.$attrs.copy(text = "标题1"))
StyledText(commonTextAttrs.$attrs.copy(text = "标题2"))
```

### 2. 主题化组件

```kotlin
@ComposeAttrs
@Composable
fun ThemedButton(
    text: String,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Medium,
    @AttrsExclude modifier: Modifier = Modifier,
    @AttrsExclude onClick: () -> Unit = {}
) {
    // 按钮实现
}

// 创建主题化的按钮属性
val primaryButtonAttrs = themedButtonAttrs(
    variant = ButtonVariant.Primary,
    size = ButtonSize.Large
)

val secondaryButtonAttrs = themedButtonAttrs(
    variant = ButtonVariant.Secondary,
    size = ButtonSize.Medium
)
```

### 3. 表单组件

```kotlin
@ComposeAttrs
@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isRequired: Boolean = false,
    errorMessage: String? = null,
    @AttrsExclude modifier: Modifier = Modifier
) {
    // 表单字段实现
}

// 创建通用表单字段属性
val requiredFieldAttrs = formFieldAttrs(
    isRequired = true
)

FormField(
    requiredFieldAttrs.$attrs.copy(
        label = "用户名",
        value = username,
        onValueChange = { username = it }
    )
)
```

## 最佳实践

1. **合理使用@AttrsExclude**: 对于modifier、content、事件回调等特殊参数使用@AttrsExclude
2. **保持参数简单**: $attrs适合简单的属性传递，复杂逻辑建议直接传参
3. **命名规范**: 生成的类名遵循`{FunctionName}Attrs`格式
4. **类型安全**: 充分利用Kotlin的类型系统，避免使用Any类型

## 与Vue $attrs的对比

| 特性 | Vue $attrs | Compose $attrs |
|------|------------|----------------|
| 类型安全 | 运行时 | 编译时 |
| 性能 | 运行时开销 | 零运行时开销 |
| IDE支持 | 有限 | 完整支持 |
| 参数排除 | 自动 | 手动注解 |
| 使用方式 | `v-bind="$attrs"` | `component(attrs.$attrs)` |

## 注意事项

- KSP处理器需要在编译时运行，确保项目配置正确
- 生成的代码位于build目录，不要手动修改
- 参数类型必须是可序列化的Kotlin类型
- 暂不支持泛型参数的$attrs生成
