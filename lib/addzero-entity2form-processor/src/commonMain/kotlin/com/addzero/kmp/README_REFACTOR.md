# Entity2Form 处理器重构文档

## 🎯 重构目标

本次重构将原有的耦合式 `generateIsomorphicForm` 方法拆分为独立的同构体生成和表单生成，并采用两阶段处理架构。

## 📋 重构内容

### 1. 架构重构

#### 原架构问题

- `generateIsomorphicForm` 方法耦合了同构体和表单生成
- 在 `process()` 阶段直接生成代码，无法优化生成顺序
- 生成路径硬编码，不够灵活

#### 新架构优势

```
com.addzero.kmp.Entity2FormProcessor (两阶段处理)
├── process() 阶段：收集实体元数据
└── finish() 阶段：生成代码
    ├── 1. 生成所有同构体 (优先)
    └── 2. 生成所有表单
```

### 2. 代码拆分

#### 原方法

```kotlin
fun generateIsomorphicForm() {
    // 同构体 + 表单耦合生成
}
```

#### 新方法

```kotlin
// 独立的同构体生成
fun generateIsomorphicClass()

// 独立的表单生成  
fun generateForm()

// 兼容性方法（已标记为 @Deprecated）
fun generateIsomorphicForm()
```

### 3. 生成顺序优化

#### 新的生成顺序

1. **第一阶段**：收集所有实体元数据
2. **第二阶段**：
    - 优先生成所有同构体类
    - 然后生成所有表单

#### 优势

- 确保同构体在表单之前生成
- 避免依赖问题
- 支持增量编译

### 4. 生成目录优化

#### 跨模块生成策略

```
backend 模块 (分析符号)
├── 分析 Jimmer 实体
└── 生成代码到其他模块

shared 模块 (同构体)
├── 源码目录: shared/src/commonMain/kotlin/
└── 包名: com.addzero.kmp.isomorphic

composeApp 模块 (表单)
├── Build 目录: composeApp/build/generated/ksp/
├── 源码目录: composeApp/src/commonMain/kotlin/ (可选)
└── 包名: com.addzero.kmp.forms
```

#### 目录选择策略

- **同构体** → `shared` 源码目录（需要被多模块共享）
- **表单** → `composeApp` build 目录（避免源码污染，支持增量编译）

### 5. 配置化管理

#### GenerationConfig 配置类

```kotlin
object GenerationConfig {
    object Isomorphic {
        const val OUTPUT_DIR = "shared/src/commonMain/kotlin/..."
        const val PACKAGE_NAME = "com.addzero.kmp.isomorphic"
    }
    
    object Form {
        const val OUTPUT_DIR = "composeApp/build/generated/ksp/..."
        const val PACKAGE_NAME = "com.addzero.kmp.forms"
    }
    
    fun getFormOutputDir(useBuildDir: Boolean = true): String
}
```

## 🚀 使用方式

### 新处理器使用

```kotlin
// KSP 自动调用新处理器
com.addzero.kmp.Entity2FormProcessor
├── 自动收集实体元数据
├── 优先生成同构体
└── 然后生成表单
```

### 手动调用（如需要）

```kotlin
// 生成同构体
generateIsomorphicDataClass(
    ksClass = entity,
    outputDir = GenerationConfig.Isomorphic.OUTPUT_DIR,
    packageName = GenerationConfig.Isomorphic.PACKAGE_NAME
)

// 生成表单
generateForm(
    ksClass = entity,
    outputDir = GenerationConfig.getFormOutputDir(),
    packageName = GenerationConfig.Form.PACKAGE_NAME
)
```

## 📊 重构效果

### 性能优化

- ✅ 两阶段处理，避免重复解析
- ✅ 元数据收集与代码生成分离
- ✅ 支持增量编译

### 代码质量

- ✅ 单一职责：同构体生成 vs 表单生成
- ✅ 配置化管理：统一的路径和包名配置
- ✅ 向后兼容：保留原有 API（标记为 deprecated）

### 生成策略

- ✅ 同构体优先生成
- ✅ 跨模块生成支持
- ✅ 灵活的输出目录选择

## 🔄 迁移指南

### 对于现有代码

- 无需修改：KSP 自动使用新处理器
- 兼容性：原有 `generateIsomorphicForm` 方法仍可用

### 对于新开发

- 推荐：使用新的独立生成方法
- 配置：通过 `GenerationConfig` 管理路径

## 🎉 总结

本次重构实现了：

1. **解耦**：同构体和表单生成独立
2. **两阶段**：元数据收集 + 代码生成
3. **优先级**：同构体优先于表单
4. **跨模块**：backend → shared/composeApp
5. **优化**：配置化、增量编译、代码质量提升
