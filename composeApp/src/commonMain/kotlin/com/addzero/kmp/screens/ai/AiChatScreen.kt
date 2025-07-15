package com.addzero.kmp.screens.ai

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close

import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.addzero.kmp.component.high_level.AddMultiColumnContainer
import com.addzero.kmp.component.card.AddJetBrainsSimpleCard
import com.addzero.kmp.entity.sys.ai.AiPrompt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.addzero.kmp.component.card.AddJetBrainsGradientCard

import com.addzero.kmp.component.text.SafeSelectionContainer
import com.addzero.kmp.settings.SettingContext4Compose
import com.addzero.kmp.settings.SettingContext4Compose.AI_AVATAR_1
import com.addzero.kmp.settings.SettingContext4Compose.AI_DESCRIPTION

import com.addzero.kmp.viewmodel.ChatViewModel
import com.mikepenz.markdown.m3.Markdown
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

// Labubu风格的颜色主题
object LabubuColors {
    val PrimaryPink = Color(0xFFFF6B9D)
    val SecondaryPurple = Color(0xFF9B59B6)
    val AccentYellow = Color(0xFFFFC107)
    val SoftBlue = Color(0xFF74B9FF)
    val MintGreen = Color(0xFF00CEC9)
    val LightPink = Color(0xFFFFF0F5)
    val SoftGray = Color(0xFFF8F9FA)
    val DarkText = Color(0xFF2D3436)
    val LightText = Color(0xFF636E72)
}


@Composable
fun AiChatScreen() {
        AiChatScreenContent()
}

@Composable
private fun AiChatScreenContent() {
    val chatViewModel = koinViewModel<ChatViewModel>()

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }

    // 初始化ViewModel
    LaunchedEffect(Unit) {
        chatViewModel.initialize()
    }

    // 可爱的动画效果
    val heartBeat by rememberInfiniteTransition(label = "heartBeat").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "heartBeat"
    )

    androidx.compose.material3.Surface(
        modifier = Modifier
            .width(420.dp)
            .fillMaxHeight()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
                clip = false
            )
            .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Labubu风格的可爱顶部栏
            LabubuTopBar(
                onClose = { chatViewModel.showChatBot = false },
                onNewChat = { chatViewModel.startNewChat() },
                heartBeat = heartBeat
            )
            // Labubu风格的聊天消息区 - 使用SafeSelectionContainer包装
            SafeSelectionContainer(
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // 常用提示词区域（仅在没有消息时显示）
                    if (chatViewModel.chatMessages.isEmpty()) {
                        LabubuPromptSuggestions(
                            prompts = chatViewModel.commonPrompts,
                            isLoading = chatViewModel.isLoadingPrompts,
                            onPromptSelected = { prompt ->
                                input = prompt.content
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // 聊天消息
                    LabubuChatMessages(
                        messages = chatViewModel.chatMessages,
                        scrollState = scrollState,
                        isAiThinking = chatViewModel.isAiThinking,
                        onRetryMessage = { messageId -> chatViewModel.retryMessage(messageId) },
                        onRetryUserMessage = { message -> chatViewModel.sendMessage(message) },
                        retryingMessageId = chatViewModel.retryingMessageId,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 自动滚动到底部 - 消息变化或AI思考状态变化时都滚动
            LaunchedEffect(chatViewModel.chatMessages.size, chatViewModel.isAiThinking) {
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }

            // Labubu风格的输入区
            LabubuInputArea(
                input = input,
                onInputChange = { input = it },
                onSend = {
                    if (input.isNotBlank()) {
                        chatViewModel.sendMessage(input)
                        input = ""
                    }
                },
                enabled = input.isNotBlank()
            )
        }
    }


}

// Labubu风格的顶部栏
@Composable
private fun LabubuTopBar(
    onClose: () -> Unit,
    onNewChat: () -> Unit,
    heartBeat: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 可爱的AI头像
        Box(
            modifier = Modifier
                .size(40.dp)
                .scale(heartBeat)
                .background(
                    MaterialTheme.colorScheme.surface,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Avatar()

//            Text(
//                text = "🤖",
//                fontSize = 20.sp
//            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = SettingContext4Compose.AI_NAME,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = ComposeFontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = AI_DESCRIPTION,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))



        // 新聊天按钮 - 带旋转动画
        var isPressed by remember { mutableStateOf(false) }

        IconButton(
            onClick = {
                isPressed = true
                onNewChat()
            },
            modifier = Modifier
                .size(36.dp)
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    CircleShape
                )
        ) {
            AnimatedContent(
                targetState = isPressed,
                transitionSpec = {
                    (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
                }, label = "newChatIcon"
            ) { pressed ->
                Icon(
                    if (pressed) Icons.Default.Refresh else Icons.Default.Add,
                    contentDescription = "新聊天",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .scale(if (pressed) 1.2f else 1f)
                )
            }
        }

        // 重置按钮状态
        LaunchedEffect(isPressed) {
            if (isPressed) {
                kotlinx.coroutines.delay(300)
                isPressed = false
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 可爱的关闭按钮
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(36.dp)
                .background(
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "关闭",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


// Labubu风格的聊天消息区
@Composable
private fun LabubuChatMessages(
    messages: List<com.addzero.kmp.viewmodel.ChatMessage>,
    scrollState: ScrollState,
    isAiThinking: Boolean = false,
    onRetryMessage: (String) -> Unit = {},
    onRetryUserMessage: (String) -> Unit = {},
    retryingMessageId: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(scrollState, enabled = true),
        verticalArrangement = Arrangement.Bottom
    ) {

        // 聊天消息
        messages.forEachIndexed { index, chatMessage ->
            LabubuChatBubble(
                chatMessage = chatMessage,
                animationDelay = index * 100,
                onRetryMessage = onRetryMessage,
                onRetryUserMessage = onRetryUserMessage,
                isRetrying = retryingMessageId == chatMessage.id,
                isAiThinking = isAiThinking
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // AI思考动画
        if (isAiThinking) {
            AiThinkingAnimation(
                isVisible = true,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// Labubu风格的聊天气泡
@Composable
private fun LabubuChatBubble(
    chatMessage: com.addzero.kmp.viewmodel.ChatMessage,
    animationDelay: Int = 0,
    onRetryMessage: (String) -> Unit = {},
    onRetryUserMessage: (String) -> Unit = {},
    isRetrying: Boolean = false,
    isAiThinking: Boolean = false
) {
    // 入场动画
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { if (chatMessage.isUser) it else -it },
            animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (chatMessage.isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!chatMessage.isUser) {
                // AI头像
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    LabubuColors.SoftBlue,
                                    LabubuColors.MintGreen
                                )
                            ),
                            CircleShape
                        )
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    Avatar()
//                    Text(
//                        text = "🤖",
//                        fontSize = 16.sp
//                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            // 消息气泡容器
            Column {
                // 消息气泡（带复制按钮）
                Box(
                    modifier = Modifier
                        .background(
                            brush = if (chatMessage.isUser) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        LabubuColors.PrimaryPink,
                                        LabubuColors.SecondaryPurple
                                    )
                                )
                            } else if (chatMessage.isError) {
                                // 错误消息使用红色渐变
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFFEBEE),
                                        Color(0xFFFFCDD2)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White,
                                        LabubuColors.LightPink
                                    )
                                )
                            },
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = if (chatMessage.isUser) 20.dp else 4.dp,
                                bottomEnd = if (chatMessage.isUser) 4.dp else 20.dp
                            )
                        )
                        .border(
                            1.dp,
                            if (chatMessage.isUser) Color.Transparent
                            else if (chatMessage.isError) Color(0xFFE57373)
                            else LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = if (chatMessage.isUser) 20.dp else 4.dp,
                                bottomEnd = if (chatMessage.isUser) 4.dp else 20.dp
                            )
                        )
                        .widthIn(max = 280.dp)
                ) {
                    // 消息内容
                    Markdown(
                        content = chatMessage.content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = if (chatMessage.isUser) 72.dp else 40.dp, // 用户消息需要更多右边距
                                top = 12.dp,
                                bottom = 12.dp
                            )
                    )

                    // 右上角按钮组 - 复制和重新发送
                    val clipboardManager = LocalClipboardManager.current
                    var showCopyFeedback by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // 重新发送按钮（仅用户消息显示）
                        if (chatMessage.isUser) {
                            IconButton(
                                onClick = { onRetryUserMessage(chatMessage.content) },
                                enabled = !isAiThinking, // AI思考时禁用
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "重新发送",
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isAiThinking) {
                                        Color.Gray.copy(alpha = 0.5f)
                                    } else {
                                        Color.White.copy(alpha = 0.7f)
                                    }
                                )
                            }
                        }

                        // 复制按钮
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(chatMessage.content))
                                showCopyFeedback = true
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "复制消息",
                                modifier = Modifier.size(14.dp),
                                tint = if (chatMessage.isUser) {
                                    Color.White.copy(alpha = 0.7f)
                                } else {
                                    LabubuColors.PrimaryPink.copy(alpha = 0.7f)
                                }
                            )
                        }
                    }

                    // 复制反馈动画
                    if (showCopyFeedback) {
                        LaunchedEffect(showCopyFeedback) {
                            kotlinx.coroutines.delay(1000)
                            showCopyFeedback = false
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.8f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "已复制",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // AI错误消息的重试按钮（保留在下方）
                if (chatMessage.canRetry && chatMessage.isError && !chatMessage.isUser) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.width(40.dp)) // 对齐AI头像

                        // AI重试按钮
                        androidx.compose.material3.OutlinedButton(
                            onClick = { onRetryMessage(chatMessage.id) },
                            enabled = !isRetrying,
                            modifier = Modifier.height(32.dp),
                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isRetrying) Color.Gray else LabubuColors.PrimaryPink
                            )
                        ) {
                            if (isRetrying) {
                                // 显示加载动画
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = LabubuColors.PrimaryPink
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("重试中...", fontSize = 12.sp)
                            } else {
                                Icon(
                                    Icons.Default.Replay,
                                    contentDescription = "重试",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("重试", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            if (chatMessage.isUser) {
                Spacer(modifier = Modifier.width(8.dp))
                // 用户头像
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    LabubuColors.AccentYellow,
                                    LabubuColors.PrimaryPink
                                )
                            ),
                            CircleShape
                        )
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "😊",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun Avatar() {
    AsyncImage(
        model = AI_AVATAR_1,
        contentDescription = null,

        )
}

// Labubu风格的常用提示词建议
@Composable
private fun LabubuPromptSuggestions(
    prompts: List<AiPrompt>,
    isLoading: Boolean,
    onPromptSelected: (AiPrompt) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        // 标题
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = "常用提示词",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "💡 常用提示词",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = ComposeFontWeight.Medium
            )
        }

        // 加载状态或提示词网格
        if (isLoading) {
            // 加载状态
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "正在加载提示词...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (prompts.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无可用的提示词",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // 提示词网格 - 使用AddMultiColumnContainer和JetBrains风格卡片
            AddMultiColumnContainer(
                howMuchColumn = 2,
                horizontalGap = 12,
                verticalGap = 12,
                modifier = Modifier,
                items = prompts.take(4).map { prompt ->
                    {
                        AddJetBrainsGradientCard(
                            onClick = { onPromptSelected(prompt) },
                            modifier = Modifier.fillMaxWidth(),
                            hoverColor = ComposeColor(0xFF6B73FF)
                        ) {
                            Text(
                                text = prompt.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 16.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            )
        }
    }
}


