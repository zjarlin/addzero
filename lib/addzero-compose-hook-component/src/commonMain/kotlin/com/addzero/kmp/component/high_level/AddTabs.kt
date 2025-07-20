package com.addzero.kmp.component.high_level

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 标签页数据类
 *
 * @param title 标签标题
 * @param content 标签内容
 */
data class TabItem(
    val title: String,
    val content: @Composable () -> Unit,
)

/**
 * 多标签页组件
 *
 * @param tabs 标签页列表
 * @param initialTabIndex 初始选中的标签索引
 * @param modifier 修饰符
 * @param tabRowModifier 标签行修饰符
 * @param contentModifier 内容区域修饰符
 * @param indicatorColor 指示器颜色
 * @param selectedTabTextColor 选中标签文字颜色
 * @param unselectedTabTextColor 未选中标签文字颜色
 * @param tabRowBackground 标签行背景色
 */
@Composable
fun AddTabs(
    tabs: List<TabItem>,
    initialTabIndex: Int = 0,
    modifier: Modifier = Modifier,
    tabRowModifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    selectedTabTextColor: Color = MaterialTheme.colorScheme.primary,
    unselectedTabTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    tabRowBackground: Color = MaterialTheme.colorScheme.surface
) {
    if (tabs.isEmpty()) return

    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }

    // 添加动画效果
    val indicatorOffset by animateFloatAsState(
        targetValue = selectedTabIndex.toFloat(),
        animationSpec = tween(300),
        label = "indicatorOffset"
    )

    Column(modifier = modifier) {
        // 标签行
        Surface(
            modifier = tabRowModifier.fillMaxWidth(),
            shadowElevation = 4.dp,
            color = tabRowBackground
        ) {
            Column {
                // 使用SubcomposeLayout确保指示器精确对齐标签
                TabRowWithIndicator(
                    count = tabs.size,
                    selectedIndex = indicatorOffset,
                    indicatorColor = indicatorColor,
                    tabContent = { index ->
                        val tab = tabs[index]
                        val isSelected = index == selectedTabIndex
                        val tabTextColor = if (isSelected) selectedTabTextColor else unselectedTabTextColor
                        val textAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.6f,
                            label = "tabTextAlpha"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTabIndex = index }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.title,
                                color = tabTextColor,
                                fontSize = 18.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.alpha(textAlpha)
                            )
                        }
                    }
                )

                // 分隔线
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray.copy(alpha = 0.2f))
                )
            }
        }

        // 内容区域
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                slideInHorizontally { width -> direction * width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -direction * width } + fadeOut()
            },
            modifier = contentModifier,
            label = "tabContent"
        ) { targetTabIndex ->
            tabs[targetTabIndex].content()
        }
    }
}

/**
 * 带指示器的标签行 - 使用SubcomposeLayout确保精确对齐
 */
@Composable
private fun TabRowWithIndicator(
    count: Int,
    selectedIndex: Float,
    indicatorColor: Color,
    tabContent: @Composable (Int) -> Unit
) {
    SubcomposeLayout(
        modifier = Modifier.fillMaxWidth()
    ) { constraints ->
        val tabPlaceables = (0 until count).map { index ->
            val tabId = "tab_$index"
            subcompose(tabId) {
                Box(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                        }
                        .layoutId(tabId),
                    contentAlignment = Alignment.Center
                ) {
                    tabContent(index)
                }
            }.map { it.measure(Constraints.fixedWidth(constraints.maxWidth / count)) }
        }

        val tabConstraints = constraints.copy(minWidth = constraints.maxWidth / count)

        // 计算指示器放置位置
        val tabWidth = constraints.maxWidth / count
        val indicatorPlaceable = subcompose("indicator") {
            Box(
                modifier = Modifier
                    .width(tabWidth.toDp())
                    .height(3.dp)
                    .background(
                        color = indicatorColor,
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                    )
            )
        }.first().measure(Constraints.fixed(tabWidth, 3.dp.roundToPx()))

        val tabHeight = tabPlaceables.maxOfOrNull { it.first().height } ?: 0
        val totalHeight = tabHeight + indicatorPlaceable.height

        layout(constraints.maxWidth, totalHeight) {
            val indicatorX = (selectedIndex * tabWidth).toInt()
            val indicatorY = tabHeight - indicatorPlaceable.height

            // 放置所有标签
            tabPlaceables.forEachIndexed { index, placeables ->
                val tabX = index * tabWidth
                placeables.forEach { placeable ->
                    placeable.placeRelative(tabX, 0)
                }
            }

            // 放置指示器
            indicatorPlaceable.placeRelative(indicatorX, indicatorY)
        }
    }
}

/**
 * 自定义标签组件
 */
@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tabContent: @Composable (String, Int, Boolean) -> Unit = { title, _, isSelected ->
        Text(
            text = title,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.6f
            ),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(16.dp)
        )
    }
) {
    // 添加动画效果
    val indicatorOffset by animateFloatAsState(
        targetValue = selectedTabIndex.toFloat(),
        animationSpec = tween(300),
        label = "indicatorOffset"
    )

    Column(modifier = modifier) {
        // 使用SubcomposeLayout确保指示器精确对齐标签
        TabRowWithIndicator(
            count = tabs.size,
            selectedIndex = indicatorOffset,
            indicatorColor = MaterialTheme.colorScheme.primary,
            tabContent = { index ->
                val isSelected = index == selectedTabIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    tabContent(tabs[index], index, isSelected)
                }
            }
        )

        // 分隔线
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
    }
} 