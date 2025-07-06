package com.addzero.kmp.component.background

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.addzero.kmp.screens.ai.LabubuColors
import com.addzero.kmp.viewmodel.ChatBackgroundViewModel

/**
 * 聊天背景选择器对话框
 */
@Composable
fun ChatBackgroundSelectorDialog(
    backgroundViewModel: ChatBackgroundViewModel,
    onDismiss: () -> Unit
) {
    if (backgroundViewModel.showBackgroundSelector) {
        Dialog(onDismissRequest = onDismiss) {
            ChatBackgroundSelectorContent(
                backgroundViewModel = backgroundViewModel,
                onDismiss = onDismiss
            )
        }
    }
}

/**
 * 背景选择器内容
 */
@Composable
private fun ChatBackgroundSelectorContent(
    backgroundViewModel: ChatBackgroundViewModel,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨 选择聊天背景",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.PrimaryPink
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = LabubuColors.LightText
                    )
                }
            }
            
            // 当前背景信息
            CurrentBackgroundInfo(backgroundViewModel)
            
            HorizontalDivider()
            
            // 预设背景选择
            PresetBackgroundSection(backgroundViewModel)
            
            HorizontalDivider()
            
            // 自定义背景选项
            CustomBackgroundSection(backgroundViewModel)
            
            HorizontalDivider()
            
            // 背景设置
            BackgroundSettingsSection(backgroundViewModel)
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 底部按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        backgroundViewModel.resetToDefault()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("重置默认")
                }
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LabubuColors.PrimaryPink
                    )
                ) {
                    Text("完成")
                }
            }
        }
    }
}

/**
 * 当前背景信息
 */
@Composable
private fun CurrentBackgroundInfo(backgroundViewModel: ChatBackgroundViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LabubuColors.LightPink.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 当前背景预览
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                ChatBackground(
                    config = backgroundViewModel.currentBackground,
                    modifier = Modifier.fillMaxSize()
                ) {}
            }
            
            Column {
                Text(
                    text = "当前背景",
                    style = MaterialTheme.typography.bodySmall,
                    color = LabubuColors.LightText
                )
                Text(
                    text = backgroundViewModel.getCurrentBackgroundName(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = LabubuColors.DarkText
                )
            }
        }
    }
}

/**
 * 预设背景选择区域
 */
@Composable
private fun PresetBackgroundSection(backgroundViewModel: ChatBackgroundViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "🌈 预设背景",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(backgroundViewModel.presetBackgrounds) { (name, config) ->
                PresetBackgroundItem(
                    name = name,
                    config = config,
                    isSelected = backgroundViewModel.currentBackground == config,
                    onClick = { backgroundViewModel.setPresetBackground(config) }
                )
            }
        }
    }
}

/**
 * 预设背景项
 */
@Composable
private fun PresetBackgroundItem(
    name: String,
    config: ChatBackgroundConfig,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) LabubuColors.PrimaryPink else LabubuColors.LightText.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onClick() }
        ) {
            ChatBackground(
                config = config,
                modifier = Modifier.fillMaxSize()
            ) {}
            
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LabubuColors.PrimaryPink.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "已选择",
                        tint = LabubuColors.PrimaryPink,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) LabubuColors.PrimaryPink else LabubuColors.LightText
        )
    }
}

/**
 * 自定义背景区域
 */
@Composable
private fun CustomBackgroundSection(backgroundViewModel: ChatBackgroundViewModel) {
    var imageUrl by remember { mutableStateOf(backgroundViewModel.customImageUrl) }
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "🖼️ 自定义背景",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )
        
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("图片URL") },
            placeholder = { Text("输入图片链接...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (imageUrl.isNotBlank()) {
                            backgroundViewModel.setCustomImageBackground(imageUrl)
                        }
                    }
                ) {
                    Icon(Icons.Default.Check, contentDescription = "应用")
                }
            }
        )
    }
}

/**
 * 背景设置区域
 */
@Composable
private fun BackgroundSettingsSection(backgroundViewModel: ChatBackgroundViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "⚙️ 背景设置",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )
        
        // 透明度设置
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "透明度",
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText
            )
            
            Text(
                text = "${(backgroundViewModel.backgroundAlpha * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = LabubuColors.LightText
            )
        }
        
        Slider(
            value = backgroundViewModel.backgroundAlpha,
            onValueChange = { backgroundViewModel.updateBackgroundAlpha(it) },
            valueRange = 0.1f..1f,
            colors = SliderDefaults.colors(
                thumbColor = LabubuColors.PrimaryPink,
                activeTrackColor = LabubuColors.PrimaryPink
            )
        )
        
        // 覆盖层设置
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "启用覆盖层",
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText
            )
            
            Switch(
                checked = backgroundViewModel.enableOverlay,
                onCheckedChange = { backgroundViewModel.toggleOverlay() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LabubuColors.PrimaryPink,
                    checkedTrackColor = LabubuColors.PrimaryPink.copy(alpha = 0.5f)
                )
            )
        }
    }
}
