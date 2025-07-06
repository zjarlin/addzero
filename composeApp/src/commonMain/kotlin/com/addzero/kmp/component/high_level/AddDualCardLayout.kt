package com.addzero.kmp.component.high_level

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 双卡片布局组件
 * @param modifier 整体修饰符
 * @param leftCardWidth 左侧卡片宽度，默认300dp
 * @param cardElevation 卡片阴影高度，默认4dp
 * @param cardShape 卡片圆角形状，默认16dp
 * @param cardSpacing 卡片间距，默认20dp
 * @param contentPadding 整体内容内边距，默认16dp
 * @param backgroundColor 背景颜色，默认使用 MaterialTheme 的 surfaceVariant
 * @param leftCardColor 左侧卡片颜色，默认使用 MaterialTheme 的 surface
 * @param rightCardColor 右侧卡片颜色，默认使用 MaterialTheme 的 surface
 * @param leftContent 左侧卡片内容
 * @param rightContent 右侧卡片内容
 */
@Composable
fun AddDualCardLayout(
    modifier: Modifier = Modifier,
    leftCardWidth: Int = 300,
    cardElevation: Int = 4,
    cardShape: Int = 16,
    cardSpacing: Int = 20,
    contentPadding: Int = 16,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    leftCardColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    rightCardColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding.dp)
        ) {
            // 左侧卡片
            Card(
                modifier = Modifier
                    .width(leftCardWidth.dp)
                    .fillMaxHeight()
                    .shadow(
                        elevation = cardElevation.dp,
                        shape = RoundedCornerShape(cardShape.dp),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(cardShape.dp),
                colors = CardDefaults.cardColors(
                    containerColor = leftCardColor
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cardShape.dp))
                ) {
                    leftContent()
                }
            }

            // 卡片间距
            Spacer(modifier = Modifier.width(cardSpacing.dp))

            // 右侧卡片
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .shadow(
                        elevation = cardElevation.dp,
                        shape = RoundedCornerShape(cardShape.dp),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(cardShape.dp),
                colors = CardDefaults.cardColors(
                    containerColor = rightCardColor
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(cardShape.dp))
                ) {
                    rightContent()
                }
            }
        }
    }
} 