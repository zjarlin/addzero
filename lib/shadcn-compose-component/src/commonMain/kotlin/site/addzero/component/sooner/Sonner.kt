package site.addzero.component.sooner

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.colors
import site.addzero.component.Button
import site.addzero.component.ButtonSize
import site.addzero.component.ButtonVariant
import site.addzero.themes.radius

@Composable
fun Sonner(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    variant: SonnerVariant = SonnerVariant.Default
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    val containerColor = when (variant) {
        SonnerVariant.Default -> colors.snackbar
        SonnerVariant.Destructive -> colors.destructive
    }

    val contentColor = when (variant) {
        SonnerVariant.Default -> colors.foreground
        SonnerVariant.Destructive -> colors.destructiveForeground
    }

    val actionContentColor = when (variant) {
        SonnerVariant.Default -> colors.mutedForeground
        SonnerVariant.Destructive -> colors.destructiveForeground
    }

    val border = when (variant) {
        SonnerVariant.Default -> colors.border
        SonnerVariant.Destructive -> colors.destructive
    }
    Snackbar(
        modifier = modifier
            .padding(16.dp)
            .border(1.dp, border, RoundedCornerShape(radius.lg)),
        action = if (actionLabel != null && onActionClick != null && onDismiss == null) {
            {
                if (variant == SonnerVariant.Destructive) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable(onClick = onActionClick)
                    ) {
                        Text(actionLabel, color = colors.destructiveForeground)
                    }
                } else {
                    Button(
                        onClick = onActionClick,
                        size = ButtonSize.Sm,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(actionLabel)
                    }
                }
            }
        } else null,
        dismissAction = if (onDismiss != null) {
            {
                if (variant == SonnerVariant.Destructive) {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable(onClick = onDismiss)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = colors.destructiveForeground
                        )
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        variant = ButtonVariant.Ghost,
                        size = ButtonSize.Icon,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }
            }
        } else null,
        shape = RoundedCornerShape(radius.lg),
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        actionOnNewLine = false
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}
