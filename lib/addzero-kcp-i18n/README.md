# KCP 国际化插件 (I18N Plugin)

这个插件是一个Kotlin编译器插件，允许开发者使用自然母语编写Kotlin代码，插件在编译时扫描代码中的字符串字面量，并根据开发者提供的资源文件将其替换为目标语言的对应值。

## 功能特性

- 使用 `@I18N` 注解标记需要国际化的函数和类
- 使用 `@I18NProperty` 注解标记需要国际化的属性
- 编译时自动将字符串字面量替换为目标语言的对应值
- 支持自定义翻译键值
- 支持通过编译器插件配置设置目标方言

## 使用方法

### 1. 添加依赖

在你的 `build.gradle.kts` 文件中添加依赖：

```kotlin
dependencies {
    implementation("site.addzero.kcp:addzero-kcp-i18n:+")
}
```

### 2. 配置插件

在 `build.gradle.kts` 中配置目标语言和资源路径：

```kotlin
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-P")
        freeCompilerArgs.add("plugin:site.addzero.kcp.i18n.plugin.I18NCompilerPlugin:targetLocale=en")
        freeCompilerArgs.add("-P")
        freeCompilerArgs.add("plugin:site.addzero.kcp.i18n.plugin.I18NCompilerPlugin:resourceBasePath=i18n")
    }
}
```

### 3. 编写国际化代码

```kotlin
// 例如，在Compose中使用
Text("你好") // 编译时会替换为 Text("hello")

// 或者在普通函数中使用
fun showMessage() {
    println("欢迎使用我们的系统!") // 编译时会替换为 println("Welcome to our system!")
}
```

### 4. 配置资源文件

开发者需要在自己的项目中提供资源文件，插件将从这些文件中查找翻译：

在 `src/main/resources/i18n/` 目录下创建资源文件：

**en.properties:**
```properties
# 键命名规范: 文件名_函数名_组件名_参数名_value
Main_main_println_text_你好=Hello
Main_main_println_text_欢迎使用我们的系统!=Welcome to our system!
```

**zh.properties:**
```properties
# 键命名规范: 文件名_函数名_组件名_参数名_value
Main_main_println_text_你好=你好
Main_main_println_text_欢迎使用我们的系统!=欢迎使用我们的系统!
```

## 编译时行为

在编译时，插件会将代码中的字符串字面量替换为目标语言的对应值：

```kotlin
// 源代码
Text("你好")
println("欢迎使用我们的系统!")

// 编译后的等效代码（当targetLocale=en时）
Text("Hello")
println("Welcome to our system!")
```

## 示例项目

在 `example/` 目录中提供了一个完整的示例项目，展示了如何使用该插件：

```
example/
├── build.gradle.kts          # 项目构建配置
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── Main.kt       # 使用中文字符串的源代码
│   │   └── resources/
│   │       └── i18n/
│   │           ├── en.properties  # 英文翻译
│   │           └── zh.properties  # 中文翻译（原语言）
```

## 测试

插件包含以下测试用例：

1. `TestResourceBundleManager` - 测试资源包管理器的功能
2. `TestI18NPlugin` - 测试插件的基本功能

运行测试：
```bash
./gradlew :lib:addzero-kcp-i18n:test
```

## 注意事项

1. 该插件目前是一个概念验证实现，实际的IR转换逻辑需要更复杂的实现
2. 开发者需要在自己的项目中提供资源文件，插件不会内置任何翻译
3. 插件仅在编译时生效，运行时仍然使用标准的英文标识符
4. 键命名规范为 `文件名_函数名_组件名_参数名_value`，其中文件名、函数名和参数名是可选的
5. 资源文件应放置在 `src/main/resources/i18n/` 目录下，文件名应为语言代码（如 `en.properties`, `zh.properties`）