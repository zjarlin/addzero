package site.addzero.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

/**
 * 显示用户头像，如果图片不可用或加载失败则显示后备内容。
 * 使用 Coil 3.3.0 进行优化，提高性能和内存效率。
 *
 * @param modifier 可选的 [Modifier] 用于此头像组件。
 * @param size 头像的目标尺寸。默认为 40.dp。
 * @param imageUrl 要显示的图片URL。如果为null或空，行为将取决于
 *   [fallbackText]、[loadingContent] 和 [errorContent]。
 * @param contentDescription 头像图片的文本描述，用于无障碍访问。
 *   如果使用 [imageUrl]，建议提供此参数。
 * @param fallbackText 如果 [imageUrl] 未提供或加载失败时显示的文本，
 *   且没有提供特定的 [errorContent]。此文本通常是
 *   首字母缩写或占位符字符。
 * @param placeholderImage 可选的占位图片URL，在加载时显示。
 * @param errorImage 可选的错误图片URL，在加载失败时显示。
 * @param loadingContent 可选的可组合lambda函数，在 [imageUrl] 图片
 *   加载时显示。如果为null，将显示 [placeholderImage] 或后备文本。
 * @param errorContent 可选的可组合lambda函数，如果从 [imageUrl]
 *   加载图片失败时显示。如果为null，将显示 [errorImage] 或后备文本。
 * @param contentScale 图片（如果从 [imageUrl] 加载）在头像边界内如何缩放。
 *   默认为 [ContentScale.Crop]。
 * @param enableGifAnimation 是否启用GIF动画支持。默认为true。
 *   Coil 3 通过 network-ktor3 集成内置了GIF支持。
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    imageUrl: String? = null,
    contentDescription: String? = null,
    fallbackText: String,
    placeholderImage: String? = null,
    errorImage: String? = null,
    loadingContent: @Composable (() -> Unit)? = null,
    errorContent: @Composable (() -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    enableGifAnimation: Boolean = true
) {
    // val colors = MaterialTheme.colors // 注释：Material3 颜色，暂未使用

    val painter = rememberAsyncImagePainter(
        model = imageUrl
    )
    val state by painter.state.collectAsState()

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // 检查图片是否成功加载或正在加载中
        when (state) {
            AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Loading -> {
                if (loadingContent != null) {
                    loadingContent()
                } else {
                    Text(
                        text = fallbackText,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = (size.value * 0.4).sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale
                )
            }

            is AsyncImagePainter.State.Error -> {
                if (errorContent != null) {
                    errorContent()
                } else {
                    Text(
                        text = fallbackText,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = (size.value * 0.4).sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
