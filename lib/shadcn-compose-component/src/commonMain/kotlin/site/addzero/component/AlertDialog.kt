package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import site.addzero.themes.radius
import site.addzero.themes.colors

/**
 * 显示一个带有标题、描述和可自定义操作按钮的模态对话框。
 *
 * @param onDismissRequest 当用户尝试关闭对话框时调用的回调（例如，通过点击外部区域）。
 * @param open 控制对话框可见性的布尔状态。
 * @param modifier 应用于对话框内容区域的修饰符。
 * @param title 警告对话框标题的可组合内容。
 * @param description 警告对话框描述的可组合内容。
 * @param actions 警告对话框操作按钮的可组合内容（例如，AlertDialogAction、AlertDialogCancel）。
 */
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius

    if (open) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(colors.background, RoundedCornerShape(radius.lg))
                    .border(1.dp, colors.border, RoundedCornerShape(radius.lg))
                    .padding(24.dp)
            ) {
                // 头部（标题和描述）
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProvideTextStyle(
                        value = TextStyle(
                            color = colors.foreground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        title()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ProvideTextStyle(
                        value = TextStyle(
                            color = colors.mutedForeground,
                            fontSize = 14.sp
                        )
                    ) {
                        description()
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // 底部（操作按钮）
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, // justify-end
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        }
    }
}

/**
 * AlertDialog标题的可组合组件。
 * 应该在[AlertDialog]的`title`插槽中使用。
 */
@Composable
fun AlertDialogTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * AlertDialog描述的可组合组件。
 * 应该在[AlertDialog]的`description`插槽中使用。
 */
@Composable
fun AlertDialogDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * ShadcnAlertDialog的`actions`插槽中操作按钮的可组合组件。
 * 通常用于主要操作（例如，"继续"、"确认"）。
 * 使用带有`ButtonVariant.Default`的[Button]。
 */
@Composable
fun AlertDialogAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Default) {
        content()
    }
}

/**
 * ShadcnAlertDialog的`actions`插槽中取消按钮的可组合组件。
 * 通常用于次要操作（例如，"取消"）。
 * 使用带有`ButtonVariant.Outline`的[Button]。
 */
@Composable
fun AlertDialogCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Outline) {
        content()
    }
}
