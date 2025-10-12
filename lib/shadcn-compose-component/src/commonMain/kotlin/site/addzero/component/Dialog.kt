package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius
import androidx.compose.ui.window.Dialog as ComposeDialog
import site.addzero.themes.colors

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 对话框组件。
 * 显示一个带有标题、描述和可自定义页脚的模态对话框。
 *
 * @param onDismissRequest 当用户尝试关闭对话框时调用的回调
 *  （例如，通过点击外部）。
 * @param open 控制对话框可见性的布尔状态。
 * @param modifier 应用于对话框内容区域的修饰符。
 * @param header 对话框头部的可组合内容，
 *  通常包含标题（例如，使用 [DialogTitle] 和 [DialogDescription]）。
 * @param body 对话框主体的可组合内容（例如，输入字段、列表等）。
 * @param footer 对话框页脚的可组合内容（例如，操作按钮）。
 */
@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    body: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius

    if (open) {
        ComposeDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(colors.background, RoundedCornerShape(radius.lg))
                    .border(1.dp, colors.border, RoundedCornerShape(radius.lg))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        header()
                    }

                    Box(
                        modifier = Modifier
                            .offset(y = (-12).dp),
                    ) {
                        Button(
                            onClick = onDismissRequest,
                            size = ButtonSize.Icon,
                            variant = ButtonVariant.Ghost,
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "关闭对话框",
                                tint = colors.mutedForeground
                            )
                        }
                    }
                }
                body?.let {
                    Column { it() }
                }
                // 页脚（操作按钮）
                footer?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it()
                    }
                }
            }
        }
    }
}

/**
 * 对话框标题的可组合项。
 * 应该在 [Dialog] 的 `title` 插槽中使用。
 */
@Composable
fun DialogTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ProvideTextStyle(
        value = TextStyle(
            color = MaterialTheme.colors.foreground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * 对话框描述的可组合项。
 * 应该在 [Dialog] 的 `description` 插槽中使用。
 */
@Composable
fun DialogDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ProvideTextStyle(
        value = TextStyle(
            color = MaterialTheme.colors.mutedForeground,
            fontSize = 14.sp
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * ShadcnDialog `footer` 插槽中操作按钮的可组合项。
 * 通常用于主要操作（例如，"保存更改"）。
 * 使用带有 `ButtonVariant.Default` 的 [Button]。
 */
@Composable
fun DialogAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Default) {
        content()
    }
}

/**
 * ShadcnDialog `footer` 插槽中取消按钮的可组合项。
 * 通常用于次要操作（例如，"取消"）。
 * 使用带有 `ButtonVariant.Outline` 的 [Button]。
 */
@Composable
fun DialogCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Outline) {
        content()
    }
}
