package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius
import site.addzero.themes.colors

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 抽屉（底部表单）组件。
 * 显示一个带有标题、描述和可自定义页脚的模态底部表单。
 *
 * @param onDismissRequest 当用户尝试关闭抽屉时调用的回调（例如，通过向下滑动或点击外部）。
 * @param open 控制抽屉可见性的布尔状态。
 * @param modifier 应用于抽屉内容区域的修饰符。
 * @param title 抽屉标题的可组合内容。
 * @param description 抽屉描述的可组合内容。
 * @param footer 抽屉页脚的可组合内容（例如，操作按钮）。
 * @param content 抽屉的主要内容，位于描述和页脚之间。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(
    onDismissRequest: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
    showCloseButton: Boolean = false,
    shouldDismissOnBackPress: Boolean = true
) {
    val colors = MaterialTheme.colors

    if (open) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = colors.background,
            contentColor = colors.foreground,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            dragHandle = {
                // 自定义拖拽手柄以匹配 Shadcn 的美学，通常是一条简单的线
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val halfStroke = strokeWidthPx / 2f
                            val cornerRadiusPx = 12.dp.toPx()

                            // 绘制直线顶部段
                            drawLine(
                                color = colors.border,
                                start = Offset(cornerRadiusPx, halfStroke),
                                end = Offset(size.width - cornerRadiusPx, halfStroke),
                                strokeWidth = strokeWidthPx
                            )

                            // 绘制边框的左上角弧段
                            drawArc(
                                color = colors.border,
                                startAngle = 180f,
                                sweepAngle = 90f,
                                useCenter = false,
                                topLeft = Offset(0f, 0f),
                                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                                style = Stroke(width = strokeWidthPx + 2)
                            )

                            // 绘制边框的右上角弧段
                            drawArc(
                                color = colors.border,
                                startAngle = 270f,
                                sweepAngle = 90f,
                                useCenter = false,
                                topLeft = Offset(size.width - cornerRadiusPx * 2, 0f),
                                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                                style = Stroke(width = strokeWidthPx + 2)
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                colors.mutedForeground,
                                RoundedCornerShape(MaterialTheme.radius.full)
                            )
                    )
                }
            },
            modifier = modifier,
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // 头部（标题和描述）
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
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

                    if (showCloseButton) {
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
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 主要内容插槽
                Column(modifier = Modifier.fillMaxWidth()) {
                    content()
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 页脚（操作按钮）
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, // justify-end
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    footer()
                }
            }
        }
    }
}

/**
 * ShadcnDrawer 标题的可组合项。
 * 应该在 [Drawer] 的 `title` 插槽中使用。
 */
@Composable
fun DrawerTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * ShadcnDrawer 描述的可组合项。
 * 应该在 [Drawer] 的 `description` 插槽中使用。
 */
@Composable
fun DrawerDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * ShadcnDrawer `footer` 插槽中操作按钮的可组合项。
 * 通常用于主要操作（例如，"保存更改"）。
 * 使用带有 `ButtonVariant.Default` 的 [Button]。
 */
@Composable
fun DrawerAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Default) {
        content()
    }
}

/**
 * ShadcnDrawer `footer` 插槽中取消按钮的可组合项。
 * 通常用于次要操作（例如，"取消"）。
 * 使用带有 `ButtonVariant.Outline` 的 [Button]。
 */
@Composable
fun DrawerCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Outline) {
        content()
    }
}
