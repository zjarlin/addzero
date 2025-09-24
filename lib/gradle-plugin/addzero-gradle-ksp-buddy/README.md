# KSP Buddy Plugin

KSP Buddy是一个Gradle插件，使得KSP（Kotlin Symbol Processing）拥有强类型的传参体验

## 功能特性

1. **自动生成KSP配置脚本**：根据配置的mustMap自动生成KSP配置脚本
2. **可配置的生成路径**：可以自定义KSP预编译脚本的生成路径(也就是用户所关心的他们需要传递哪些ksp参数, 自动生成在build-logic (buildSrc) 目录下) 诶☝️🤓这个是给使用ksp插件的人看的,库开发者关注mustMap就可以
3. **SettingContext生成**：根据mustMap内容自动生成Settings数据类和SettingContext对象
4. **生成文件位置**：生成的SettingContext和Settings类位于build目录下，避免污染源码

## 使用方法

### 1. 应用插件

在你的模块的`build.gradle.kts`文件中应用插件：

```kotlin
plugins {
    id("site.addzero.ksp-buddy") version "0.0.608"
}
```

### 2. 配置插件

```kotlin
    // 举例说明:假设你要传递以下 KSP options参数
kspBuddy {
    mustMap.set(
        mapOf(
            "enumOutputPackage" to "site.addzero.generated.enums",
            "dictTableName" to "sys_dict",
            "dictIdColumn" to "id",
            "dictCodeColumn" to "dict_code",
            "dictNameColumn" to "dict_name",
            "dictItemTableName" to "sys_dict_item",
            "dictItemForeignKeyColumn" to "dict_id",
            "dictItemCodeColumn" to "item_value",
            "dictItemNameColumn" to "item_text",
        )
    )

    // 配置 KSP 脚本输出路径（默认值,不配置就是这个）
    kspScriptOutputDir = "build-logic/src/main/kotlin/conventions/generated"

    // 配置 SettingContext 生成
    settingContext = SettingContextConfig(
//                （默认值,不配置就是这个）

        contextClassName = "SettingContext",
//                （默认值,不配置就是这个）
        settingsClassName = "Settings",
//                （默认值,不配置就是这个）
        packageName = "site.addzero.context",
//                （默认jvm项目src/main/kotlin,这里举个kmp的例子）
        outputDir = "src/commonMain/kotlin",
        enabled = true
    )

}
```

### 3. 添加生成源码到编译路径（可选）

为了让编译器能够找到生成的源码，需要手动将生成目录添加到源码路径中：
KMP项目：
```kotlin
kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp-buddy")
        }
    }
}
```

或者对于JVM项目：

```kotlin
kotlin {
    sourceSets {
        main {
            kotlin.srcDir("build/generated/ksp-buddy")
        }
    }
}
```

### 4. 生成的文件

应用插件并配置后，插件会自动生成以下文件：

1. **KSP配置脚本**：在指定的输出目录中生成KSP预编译脚本，文件名格式为`ksp-config4{moduleName}.gradle.kts`
2. **Settings数据类**：在`build/generated/ksp-buddy`目录中生成Settings数据类，类的属性完全基于mustMap的内容
3. **SettingContext对象**：在`build/generated/ksp-buddy`目录中生成SettingContext对象，包含基于mustMap的初始化逻辑
## Q: 为什么不直接在ksp源码中声明data class,而是要在kts脚本中声明map?
## A: 借助gradle的强大的复合构建能力,我可以用循环创建map(例如扫描项目目录结构去生成一些路径传给ksp使用,还可以用kt逻辑去动态生成data class,比手敲data class快得多)
## ps: 库暂不支持继承配置 : 例如父模块也同样启用了ksp-buddy插件,子模块配置并不会融合父模块的配置
## 提醒: 请在process逻辑的第一行 
```kotlin
// 初始化设置上下文
override fun process(resolver: Resolver): List<KSAnnotated> {
    SettingContext.initialize(options)
//     之后 ksp  logic就可以静态使用 SettingContext.settings
    
}
```
