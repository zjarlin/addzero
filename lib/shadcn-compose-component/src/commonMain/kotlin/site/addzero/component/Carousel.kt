package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.addzero.themes.colors
import kotlinx.coroutines.delay

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 轮播组件，利用 HorizontalPager 实现吸附行为。
 *
 * @param modifier 应用于轮播容器的修饰符。
 * @param autoScroll 如果为 true，轮播将在延迟后自动滚动到下一页。
 * @param autoScrollDelayMillis 自动滚动之间的延迟毫秒数。仅在 [autoScroll] 为 true 时有效。
 * @param componentSpacing 轮播项目和指示器组件之间的间距。
 * @param contentPadding 应用于分页器内容的内边距。
 * @param showIndicator 是否显示轮播指示器。
 * @param indicatorStyle 轮播指示器的样式。
 * @param itemSpacing 轮播中各个页面（项目）之间的间距。
 * @param itemCount 轮播中的页面（项目）总数。
 * @param pageSize 轮播中每个页面（项目）的大小。
 * @param onItemChanged 当当前页面（项目）改变时调用的回调函数。
 * @param content 轮播每个页面的可组合内容。
 */
@Composable
fun Carousel(
    modifier: Modifier = Modifier,
    autoScroll: Boolean = false,
    autoScrollDelayMillis: Long = 3000,
    componentSpacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp, 0.dp),
    showIndicator: Boolean = false,
    indicatorStyle: IndicatorStyle = CarouselDefaults.carouselIndicator(),
    itemSpacing: Dp = 8.dp,
    itemCount: Int,
    pageSize: PageSize = PageSize.Fill,
    onItemChanged: ((Int) -> Unit)? = null,
    content: @Composable PagerScope.(position: Int) -> Unit
) {
    val pagerState = rememberPagerState { itemCount }

    // 当当前页面改变时调用 onItemChanged 回调
    LaunchedEffect(pagerState.currentPage) {
        onItemChanged?.invoke(pagerState.currentPage)
    }

    if (autoScroll && itemCount > 1) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(autoScrollDelayMillis)
                val nextPage = (pagerState.currentPage + 1) % itemCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(componentSpacing)
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                pageSize = pageSize,
                pageSpacing = itemSpacing,
                pageContent = content,
            )
        }

        if (showIndicator) {
            CarouselIndicator(pagerState, itemCount, indicatorStyle)
        }
    }
}

@Composable
fun CarouselIndicator(state: PagerState, size: Int, style: IndicatorStyle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(size) {
            val dimens = if (state.currentPage == it) {
                style.activeSize
            } else {
                style.inactiveSize
            }
            Spacer(
                modifier = Modifier
                    .padding(style.spacing)
                    .size(dimens)
                    .background(
                        color = if (state.currentPage == it) {
                            style.activeColor
                        } else {
                            style.inactiveColor
                        },
                        shape = style.shape
                    )
            )
        }
    }
}

data class IndicatorStyle(
    val activeColor: Color,
    val inactiveColor: Color,
    val activeSize: Dp,
    val inactiveSize: Dp,
    val shape: Shape,
    val spacing: Dp
)

object CarouselDefaults {
    @Composable
    fun carouselIndicator(): IndicatorStyle {
        return IndicatorStyle(
            activeColor = MaterialTheme.colors.foreground,
            inactiveColor = MaterialTheme.colors.mutedForeground,
            activeSize = 12.dp,
            inactiveSize = 8.dp,
            spacing = 8.dp,
            shape = CircleShape
        )
    }
}
