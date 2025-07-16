# 🤖 AI提示词管理系统

这是一个完整的AI提示词管理系统，包含ViewModel、UI界面和使用示例。

## 📁 文件结构

```
com/addzero/kmp/
├── viewmodel/
│   └── AiPromptViewModel.kt          # 提示词管理ViewModel
├── screens/ai/
│   ├── AiPromptManagementScreen.kt   # 完整的管理界面
│   └── AiPromptManagementExample.kt  # 使用示例和概览界面
```

## 🚀 核心功能

### AiPromptViewModel
- ✅ **状态管理**: 使用 `var xxx by mutableStateOf() private set` 模式
- ✅ **CRUD操作**: 创建、读取、更新、删除提示词
- ✅ **搜索过滤**: 支持标题和内容搜索
- ✅ **错误处理**: 统一的错误状态管理
- ✅ **加载状态**: 完整的加载和保存状态
- ✅ **Koin集成**: 使用 `@KoinViewModel` 注解

### 主要方法

```kotlin
// 初始化和数据加载
fun initialize()                    // 初始化ViewModel
fun loadPrompts()                   // 加载所有提示词
fun loadCommonPrompts()             // 加载常用提示词（聊天界面用）
fun refresh()                       // 刷新数据

// CRUD操作
fun createPrompt(title: String, content: String)  // 创建提示词
fun updatePrompt(prompt: AiPrompt)                 // 更新提示词
fun deletePrompt(promptId: Long)                  // 删除提示词

// 搜索和过滤
fun searchPrompts(keyword: String)                // 搜索提示词
fun getFilteredPrompts(): List<AiPrompt>          // 获取过滤后的列表

// UI状态管理
fun showCreateDialog()                            // 显示创建对话框
fun showEditDialog(prompt: AiPrompt)              // 显示编辑对话框
fun hideEditDialog()                              // 隐藏对话框
fun clearError()                                  // 清除错误
```

## 🎨 UI组件

### AiPromptManagementScreen
完整的管理界面，包含：
- 📋 **提示词列表**: 支持编辑和删除
- 🔍 **搜索功能**: 实时搜索过滤
- ➕ **创建功能**: 新建提示词
- ✏️ **编辑对话框**: 修改现有提示词
- ❌ **错误提示**: 友好的错误显示

### AiPromptManagementExample
概览界面，包含：
- 📊 **统计卡片**: 显示提示词数量和使用情况
- ⚡ **快速操作**: 常用功能的快捷入口
- 📝 **最近提示词**: 显示最新的几个提示词

## 🔧 使用方法

### 1. 在Koin模块中注册ViewModel

```kotlin
// 在你的Koin模块中添加
single { AiPromptViewModel() }
```

### 2. 在Compose界面中使用

```kotlin
@Composable
fun MyScreen() {
    val promptViewModel = koinViewModel<AiPromptViewModel>()
    
    // 初始化数据
    LaunchedEffect(Unit) {
        promptViewModel.initialize()
    }
    
    // 使用完整管理界面
    AiPromptManagementScreen()
    
    // 或使用概览界面
    AiPromptManagementExample()
}
```

### 3. 在聊天界面中集成常用提示词

```kotlin
@Composable
fun ChatScreen() {
    val chatViewModel = koinViewModel<ChatViewModel>()
    val promptViewModel = koinViewModel<AiPromptViewModel>()
    
    LaunchedEffect(Unit) {
        promptViewModel.initialize()
    }
    
    // 使用常用提示词
    LabubuPromptSuggestions(
        prompts = promptViewModel.commonPrompts,
        isLoading = promptViewModel.isLoading,
        onPromptSelected = { prompt ->
            // 将提示词内容填入输入框
            chatInput = prompt.content
        }
    )
}
```

## 🎯 设计特点

### 1. 状态管理模式
- 使用 `var xxx by mutableStateOf() private set` 确保状态的封装性
- 所有状态变更都通过公共方法进行，保证数据一致性

### 2. 错误处理
- 统一的错误状态管理
- 友好的错误提示界面
- 自动错误清除机制

### 3. 用户体验
- 加载状态指示
- 保存状态反馈
- 实时搜索过滤
- 响应式UI设计

### 4. 组件化设计
- 可复用的UI组件
- 清晰的职责分离
- 灵活的集成方式

## 🔄 与现有系统集成

### ChatViewModel集成
现有的 `ChatViewModel` 可以通过以下方式使用提示词：

```kotlin
class ChatViewModel : ViewModel() {
    // 可以注入 AiPromptViewModel 或直接调用API
    fun loadCommonPrompts() {
        // 使用 AiPromptViewModel 的 commonPrompts
        // 或直接调用 aiApi.getPrompts()
    }
}
```

### API集成
确保后端提供以下API接口：
- `GET /ai/prompts` - 获取提示词列表
- `POST /ai/prompts` - 创建提示词
- `PUT /ai/prompts/{id}` - 更新提示词
- `DELETE /ai/prompts/{id}` - 删除提示词

## 📱 界面预览

### 管理界面特性
- 🎨 **JetBrains风格**: 使用AddJetBrainsGradientCard
- 🌈 **渐变效果**: 悬停时的荧光绿色效果
- 📱 **响应式布局**: 适配不同屏幕尺寸
- 🎭 **Material Design 3**: 遵循最新设计规范

### 交互体验
- ✨ **流畅动画**: 卡片悬停和点击效果
- 🔄 **实时反馈**: 加载和保存状态指示
- 🎯 **直观操作**: 清晰的按钮和图标
- 📝 **智能表单**: 自动验证和字符计数

这个系统提供了完整的提示词管理功能，可以轻松集成到现有的AI聊天系统中，提升用户的使用体验和工作效率。
