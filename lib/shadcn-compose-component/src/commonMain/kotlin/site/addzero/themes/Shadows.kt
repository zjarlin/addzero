package site.addzero.themes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import site.addzero.component.BoxShadow

/**
 * Shadcn 主题阴影接口，定义了组件系统中使用的所有阴影效果
 */
interface ShadcnShadows {
    /** 小阴影效果 */
    val sm: BoxShadow
    /** 中等阴影效果 */
    val md: BoxShadow
    /** 大阴影效果 */
    val lg: BoxShadow
    /** 超大阴影效果 */
    val xl: BoxShadow
    /** 超超大阴影效果 */
    val xxl: BoxShadow
}

/**
 * 默认阴影配置，实现了 [ShadcnShadows] 接口
 *
 * 提供了从小到超超大五种不同强度的阴影效果，使用半透明灰色
 */
object Shadows : ShadcnShadows {
    override val sm = BoxShadow(
        offsetX = 2.dp,
        offsetY = 4.dp,
        blurRadius = 2.dp,
        spread = 2.dp,
        color = Color.Gray.copy(alpha = 0.3f)
    )

    override val md = BoxShadow(
        offsetX = 2.dp,
        offsetY = 4.dp,
        blurRadius = 4.dp,
        spread = 2.dp,
        color = Color.Gray.copy(alpha = 0.3f)
    )

    override val lg = BoxShadow(
        offsetX = 4.dp,
        offsetY = 4.dp,
        blurRadius = 12.dp,
        spread = 2.dp,
        color = Color.Gray.copy(alpha = 0.3f)
    )

    override val xl = BoxShadow(
        offsetX = 4.dp,
        offsetY = 4.dp,
        blurRadius = 16.dp,
        spread = 4.dp,
        color = Color.Gray.copy(alpha = 0.3f)
    )

    override val xxl = BoxShadow(
        offsetX = 4.dp,
        offsetY = 4.dp,
        blurRadius = 20.dp,
        spread = 6.dp,
        color = Color.Gray.copy(alpha = 0.3f)
    )
}
