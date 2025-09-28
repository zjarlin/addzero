# KSP 代码生成器用户指南

## 如何自定义代码生成

本项目提供了一个基于 KSP (Kotlin Symbol Processing) 的代码生成框架，允许用户自定义代码生成逻辑。

### 1. 创建自定义 AbsCodegenContextProvider

用户需要创建自己的 [AbsCodegenContextProvider](file:///Users/zjarlin/IdeaProjects/addzero/lib/ksp/common/addzero-ksp-easycode-jimmer/src/commonMain/kotlin/site/addzero/easycode/inter/AbsCodegenContextProvider.kt#L10-L28) 实现：

```kotlin
class MyCodegenContextProvider : AbsCodegenContextProvider<MyTemplateContext>() {
    override fun getEnumClass(): Class<MyTemplateContext> = MyTemplateContext::class.java
}
```

### 2. 创建自定义模板上下文枚举

用户需要定义一个枚举实现 [TemplateContext](file:///Users/zjarlin/IdeaProjects/addzero/lib/ksp/common/addzero-ksp-easycode/src/commonMain/kotlin/site/addzero/easycode/inter/TemplateContext.kt#L5-L31) 接口：

```kotlin
enum class MyTemplateContext : TemplateContext<EntityMetadata> {
    ENTITY_DTO {
        override val getModulePath: String
            get() = "shared/src/commonMain/kotlin"

        override val getPkg: String
            get() = "site.addzero.generated.dto"

        override val getFileName: (EntityMetadata, Map<String, String>) -> String
            get() = { metadata, _ -> "${metadata.name}Dto" }

        override val templatePath: String
            get() = "templates/entity-dto.vm"
    };
}
```

### 3. 注册你的实现

通过 Java ServiceLoader 机制注册你的实现：

1. 在你的模块中创建 `META-INF/services/site.addzero.easycode.inter.AbsCodegenContextProvider` 文件
2. 在文件中添加你的实现类的完整类名：

```
com.yourpackage.MyCodegenContextProvider
```

### 4. 添加依赖

在你的模块的 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation(projects.lib.ksp.common.addzeroKspEasycodeJimmer)
}
```

### 5. 运行代码生成

执行 Gradle 任务：

```bash
./gradlew kspKotlin
```

这将触发 KSP 处理器，使用你的自定义配置生成代码。