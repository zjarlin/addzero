# 🤖 AI提示词建议组件

美化后的AI提示词建议组件，使用高阶组件和JetBrains风格设计。

## 📁 文件结构

```
com/addzero/kmp/screens/ai/
├── AiChatScreen.kt                    # 主聊天界面（包含LabubuPromptSuggestions）
├── PromptSuggestionsDemo.kt           # 演示和测试组件
└── README_PromptSuggestions.md        # 说明文档
```

## 🎨 设计特点

### 视觉风格
- ✨ **JetBrains风格**: 使用 `AddJetBrainsMellumCard` 荧光卡片
- 🌈 **多彩配色**: 4种卡片类型（Purple、Blue、Teal、Orange）
- 🎭 **HackathonCard风格**: 参考官方设计的卡片布局
- 💫 **荧光效果**: 悬浮时的边框发光效果

### 布局结构
- 📦 **高阶组件**: 使用 `AddMultiColumnContainer` 双列布局
- 📱 **响应式设计**: 自动适配不同屏幕尺寸
- 🎯 **ProductCardContent**: 统一的卡片内容结构

## 🚀 核心组件

### LabubuPromptSuggestions
主要的提示词建议组件：

```kotlin
@Composable
private fun LabubuPromptSuggestions(
    prompts: List<SysAiPromptIso>, 
    onPromptSelected: (SysAiPromptIso) -> Unit
)
```

**特性**：
- 标题区域带AI图标
- 空状态友好提示
- 使用高阶组件布局

### PromptGrid
网格布局组件：

```kotlin
@Composable
private fun PromptGrid(
    prompts: List<SysAiPromptIso>,
    onPromptSelected: (SysAiPromptIso) -> Unit
)
```

**实现**：
```kotlin
AddMultiColumnContainer(
    howMuchColumn = 2,
    items = prompts.map { prompt ->
        {
            PromptCard(
                prompt = prompt,
                onSelected = { onPromptSelected(prompt) }
            )
        }
    }
)
```

### PromptCard
单个提示词卡片：

```kotlin
@Composable
private fun PromptCard(
    prompt: SysAiPromptIso,
    onSelected: () -> Unit
)
```

**特性**：
- 根据ID自动选择卡片颜色
- 悬浮500ms后显示完整内容
- 使用 `ProductCardContent` 布局

## 🎯 智能匹配系统

### 图标匹配
根据提示词内容自动选择合适的图标：

| 关键词 | 图标 | 示例 |
|--------|------|------|
| 代码/编程/code | `Icons.Default.Code` | 代码优化 |
| 写作/文章/write | `Icons.Default.Edit` | 文档写作 |
| 翻译/translate | `Icons.Default.Translate` | 语言翻译 |
| 分析/analyze | `Icons.Default.Analytics` | 数据分析 |
| 创意/creative | `Icons.Default.Lightbulb` | 创意设计 |
| 学习/learn | `Icons.Default.School` | 学习助手 |

### 副标题匹配
自动生成相应的副标题：

| 内容类型 | 副标题 |
|----------|--------|
| 代码相关 | "代码助手" |
| 写作相关 | "写作助手" |
| 翻译相关 | "翻译助手" |
| 分析相关 | "分析助手" |
| 创意相关 | "创意助手" |
| 学习相关 | "学习助手" |
| 优化相关 | "优化助手" |
| 测试相关 | "测试助手" |
| 设计相关 | "设计助手" |
| 默认 | "AI助手" |

## 🖱️ 悬浮提示功能

### 触发条件
- 鼠标悬浮在卡片上
- 提示词内容长度 > 50个字符
- 延迟500ms显示，避免闪烁

### 视觉效果
- 淡入淡出动画（200ms）
- 圆角12dp的提示框
- 最大宽度350dp
- 阴影和高度效果

### 实现代码
```kotlin
// 悬浮状态检测
val interactionSource = remember { MutableInteractionSource() }
val isHovered by interactionSource.collectIsHoveredAsState()

// 延迟显示逻辑
LaunchedEffect(isHovered) {
    if (isHovered) {
        kotlinx.coroutines.delay(500)
        showTooltip = true
    } else {
        showTooltip = false
    }
}

// 动画效果
val tooltipAlpha by animateFloatAsState(
    targetValue = if (showTooltip && prompt.content.length > 50) 1f else 0f,
    animationSpec = tween(durationMillis = 200),
    label = "tooltip_alpha"
)
```

## 💡 使用示例

### 基础使用
```kotlin
@Composable
fun ChatScreen() {
    val prompts = listOf(
        SysAiPromptIso(
            id = 1,
            title = "代码优化专家",
            content = "请帮我优化这段代码，提高性能和可读性..."
        )
        // 更多提示词...
    )
    
    LabubuPromptSuggestions(
        prompts = prompts,
        onPromptSelected = { prompt ->
            // 处理选择的提示词
            println("选择了: ${prompt.title}")
        }
    )
}
```

### 演示组件
```kotlin
// 查看完整演示
PromptSuggestionsDemo()

// 简单测试
SimplePromptTest()
```

## 🎨 卡片类型配色

### 颜色分配规则
```kotlin
val cardTypes = listOf(
    MellumCardType.Purple,  // 紫色 - AI/智能
    MellumCardType.Blue,    // 蓝色 - 技术/开发
    MellumCardType.Teal,    // 青色 - 分析/数据
    MellumCardType.Orange   // 橙色 - 创意/设计
)

// 根据ID循环选择
val cardType = cardTypes[(prompt.id?.toInt() ?: 0) % cardTypes.size]
```

### 荧光效果
每种卡片类型都有独特的荧光色：
- **Purple**: 青色荧光 (`#00D4FF`)
- **Blue**: 亮青色荧光 (`#00FFFF`)
- **Teal**: 荧光绿色 (`#00FF88`)
- **Orange**: 荧光橙色 (`#FF6600`)

## 🔧 技术实现

### 高阶组件集成
使用你封装的 `AddMultiColumnContainer` 替代原生网格：

```kotlin
// 原来的实现
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 280.dp),
    // ...
)

// 现在的实现
AddMultiColumnContainer(
    howMuchColumn = 2,
    items = prompts.map { prompt -> { PromptCard(...) } }
)
```

### ProductCardContent结构
参考 `HackathonCard` 的内容布局：

```kotlin
ProductCardContent(
    title = prompt.title ?: "AI提示词",
    subtitle = getPromptSubtitle(prompt.content),  // 智能生成
    icon = getPromptIcon(prompt.content),          // 智能匹配
    description = prompt.content
)
```

## 🌟 最佳实践

1. **提示词数据**: 确保 `title` 和 `content` 字段有意义的内容
2. **性能优化**: 使用 `remember` 缓存交互状态
3. **用户体验**: 500ms延迟避免误触发提示
4. **视觉一致性**: 使用统一的卡片类型和配色
5. **响应式设计**: 双列布局适配不同屏幕

这个组件完美结合了你的高阶组件架构和JetBrains的现代化设计风格，为用户提供了优雅的AI提示词选择体验！🎉
