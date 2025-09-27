# EasyCode KSP Velocity 插件

这是一个基于KSP和Velocity模板引擎的代码生成插件，类似于EasyCode的功能。

## 功能特点

1. 用户只需在 `resources/templates/**` 目录下创建 `.vm` 模板文件即可生成代码
2. 自动扫描模板文件并根据模板生成对应代码
3. 支持自定义元数据提取器和Velocity上下文填充逻辑
4. 支持跳过已存在文件的配置

## 使用方法

### 1. 创建模板文件

在 `src/commonMain/resources/templates/` 目录下创建 `.vm` 模板文件，例如：

```
## Entity.vm
package $packageName

data class $className(
#foreach($field in $fields)
    val $field.name: $field.type#if($foreach.hasNext),
#end
)
```

### 2. 实现元数据提取器

创建一个类实现 `MetadataExtractor` 接口，或者使用提供的 `SimpleMetadataExtractor`：

```kotlin
val extractor = SimpleMetadataExtractor { resolver ->
    // 从resolver中提取元数据
    mapOf(
        "packageName" to "site.addzero.generated",
        "className" to "User",
        "fields" to listOf(
            mapOf("name" to "id", "type" to "Int"),
            mapOf("name" to "name", "type" to "String")
        )
    )
}
```

### 3. 创建处理器

继承 `VelocityTemplateProcessor` 并实现 `populateContext` 方法：

```kotlin
class MyProcessor(
    environment: SymbolProcessorEnvironment
) : VelocityTemplateProcessor<Map<String, Any>>(environment, extractor) {
    
    override fun populateContext(context: VelocityContext, metadata: Map<String, Any>) {
        metadata.forEach { (key, value) ->
            context.put(key, value)
        }
    }
}
```

### 4. 注册处理器

在 `SymbolProcessorProvider` 中注册你的处理器：

```kotlin
class MyProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MyProcessor(environment)
    }
}
```

## 模板变量

模板中可以使用的变量取决于你在 `populateContext` 方法中放入VelocityContext的内容。

## 输出路径

默认情况下，生成的文件会放在 `build/generated/ksp/src/commonMain/kotlin/` 目录下，
保持与模板文件相同的目录结构。

例如：`resources/templates/entity/DataClass.vm` 会生成 
`build/generated/ksp/src/commonMain/kotlin/entity/DataClass.kt`