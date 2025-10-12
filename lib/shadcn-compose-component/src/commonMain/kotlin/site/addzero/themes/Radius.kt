package site.addzero.themes

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

/**
 * Shadcn 主题圆角半径接口，定义了组件系统中使用的所有圆角尺寸
 */
interface ShadcnRadius {
    /** 基础圆角半径 */
    val radius: Dp
    /** 小圆角半径 */
    val sm: Dp
    /** 中等圆角半径 */
    val md: Dp
    /** 大圆角半径 */
    val lg: Dp
    /** 超大圆角半径 */
    val xl: Dp
    /** 完全圆角（圆形） */
    val full: Dp
}

/**
 * 默认圆角半径配置，实现了 [ShadcnRadius] 接口
 *
 * 基于 8.dp 的基础圆角半径，计算出各种尺寸的圆角值
 */
object Radius : ShadcnRadius {
    override val radius: Dp = 8.dp
    override val sm: Dp = max(0.dp, radius - 4.dp)
    override val md: Dp = max(0.dp, radius - 2.dp)
    override val lg: Dp = radius
    override val xl: Dp = max(0.dp, radius + 4.dp)
    override val full: Dp = 999.dp
}
