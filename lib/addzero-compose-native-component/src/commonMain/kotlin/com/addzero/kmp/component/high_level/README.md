# 高阶组件库

这个目录包含了一系列可复用的高阶组件，用于简化UI开发。

## AddTabs - 多标签页组件

一个强大的多标签页组件，支持自定义样式、动画和内容。类似于图片中"Search Results"和"Favorites"的标签页效果。

### 特性

- 支持任意数量的标签页
- 平滑的标签切换动画
- 带有下划线指示器，跟随当前选中的标签
- 支持自定义标签和内容样式
- 提供两种使用模式：完整组件和自定义标签行

### 基本用法

```kotlin
// 1. 定义标签页内容
val tabs = listOf(
    TabItem("搜索结果") {
        // 此处是第一个标签页的内容
        Text("搜索结果内容")
    },
    TabItem("收藏夹") {
        // 此处是第二个标签页的内容
        Text("收藏夹内容")
    }
)

// 2. 使用AddTabs组件
AddTabs(
    tabs = tabs,
    initialTabIndex = 0, // 可选，默认选中第一个标签
    modifier = Modifier.fillMaxWidth()
)
```

### 自定义样式

可以通过参数自定义标签页的样式：

```kotlin
AddTabs(
    tabs = tabs,
    initialTabIndex = 0,
    modifier = Modifier.fillMaxWidth(),
    tabRowModifier = Modifier.background(Color.LightGray),
    contentModifier = Modifier.padding(16.dp),
    indicatorColor = Color.Red,
    selectedTabTextColor = Color.Red,
    unselectedTabTextColor = Color.Gray,
    tabRowBackground = Color.White
)
```

### 使用CustomTabRow进行更灵活的自定义

如果需要更灵活的自定义，可以使用`CustomTabRow`组件：

```kotlin
// 标签内容状态
var selectedTabIndex by remember { mutableIntStateOf(0) }
val tabTitles = listOf("最新", "热门", "推荐")

// 1. 使用CustomTabRow
CustomTabRow(
    tabs = tabTitles,
    selectedTabIndex = selectedTabIndex,
    onTabSelected = { selectedTabIndex = it },
    tabContent = { title, index, isSelected ->
        // 自定义标签内容
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (index == 0) Icons.Default.Star else if (index == 1) Icons.Default.Favorite else Icons.Default.ThumbUp,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
            Text(
                text = title,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
)

// 2. 根据选中的标签显示对应内容
when (selectedTabIndex) {
    0 -> LatestContent()
    1 -> HotContent() 
    else -> RecommendedContent()
}
```

## 实现原理

`AddTabs`组件内部使用了以下技术：

1. **位置跟踪**：使用`onGloballyPositioned`获取每个标签的位置和宽度
2. **动画**：使用`animateDpAsState`和`animateFloatAsState`实现平滑过渡
3. **内容切换**：使用`AnimatedContent`提供标签内容切换的动画效果

## 注意事项

- 标签页数量不宜过多，建议控制在2-5个之间
- 如果标签标题太长，可能需要调整布局或使用缩写
- 在性能敏感的场景中，标签内容最好懒加载 