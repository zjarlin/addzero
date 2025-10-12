package site.addzero.component

import site.addzero.themes.colors
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius

/**
 * @param selectedTabIndex 当前选中标签页的索引。
 * @param onTabSelected 当标签页被选中时调用的回调函数。
 * @param tabs 标签页名称列表。
 * @param modifier 应用于标签页容器的修饰符。
 * @param content 每个标签页的可组合内容。
 */
@Composable
fun Tabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable (tabIndex: Int) -> Unit
) {
    Column(modifier = modifier) {
        TabsList(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
            tabs = tabs
        )

        content(selectedTabIndex)
    }
}

/**
 * @param selectedTabIndex 当前选中标签页的索引。
 * @param onTabSelected 当标签页被选中时调用的回调函数。
 * @param tabs 标签页名称列表。
 * @param modifier 应用于标签页容器的修饰符。
 */
@Composable
fun TabsList(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier
) {
    val radius = MaterialTheme.radius
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.muted,
                shape = RoundedCornerShape(radius.md)
            )
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, tab ->
                TabsTrigger(
                    text = tab,
                    isSelected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * @param text 要显示的文本。
 * @param isSelected 标签页当前是否被选中。
 * @param onClick 当标签页被点击时调用的回调函数。
 * @param modifier 应用于标签页的修饰符。
 */
@Composable
fun TabsTrigger(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) colors.background else colors.muted,
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected)
            colors.foreground
        else
            colors.mutedForeground,
        animationSpec = tween(200),
        label = "textColor"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius.sm))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

/**
 * Shadcn 风格的 TabsContent 组件
 */
@Composable
fun TabsContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        content()
    }
}

/**
 * 创建动画颜色状态的辅助函数
 */
@Composable
private fun animateColorAsState(
    targetValue: Color,
    animationSpec: androidx.compose.animation.core.AnimationSpec<Float> = tween(),
    label: String = "ColorAnimation"
): State<Color> {
    val red by animateFloatAsState(
        targetValue = targetValue.red,
        animationSpec = animationSpec,
        label = "${label}_red"
    )
    val green by animateFloatAsState(
        targetValue = targetValue.green,
        animationSpec = animationSpec,
        label = "${label}_green"
    )
    val blue by animateFloatAsState(
        targetValue = targetValue.blue,
        animationSpec = animationSpec,
        label = "${label}_blue"
    )
    val alpha by animateFloatAsState(
        targetValue = targetValue.alpha,
        animationSpec = animationSpec,
        label = "${label}_alpha"
    )

    val colorState = remember(red, green, blue, alpha) {
        derivedStateOf { Color(red, green, blue, alpha) }
    }

    return colorState
}
