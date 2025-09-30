package site.addzero.screens.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import site.addzero.component.button.AddIconButton
import site.addzero.component.card.AddCard
import site.addzero.generated.isomorphic.SysAiPromptIso
import site.addzero.viewmodel.AiPromptViewModel
import site.addzero.viewmodel.ChatMessage
import site.addzero.viewmodel.ChatViewModel
import com.mikepenz.markdown.m3.Markdown
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.BuildKonfig
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight

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
    val aiPromptViewModel = koinViewModel<AiPromptViewModel>()


    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()


    // 可爱的动画效果
    val heartBeat by rememberInfiniteTransition(label = "heartBeat").animateFloat(
        initialValue = 1f, targetValue = 1.1f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine), repeatMode = RepeatMode.Reverse
        ), label = "heartBeat"
    )

    Surface(
        modifier = Modifier.width(800.dp).fillMaxHeight().shadow(
            elevation = 12.dp, shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp), clip = false
        ).clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)), color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(0.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Labubu风格的可爱顶部栏
            LabubuTopBar(
                onClose = { chatViewModel.showChatBot = false },
                onNewChat = { chatViewModel.startNewChat() },
                heartBeat = heartBeat
            )
            // Labubu风格的聊天消息区 - 使用SafeSelectionContainer包装
            SelectionContainer(
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // 常用提示词区域（仅在没有消息时显示）
                    if (chatViewModel.chatMessages.isEmpty()) {
                        LabubuPromptSuggestions(
                            prompts = aiPromptViewModel.prompts,
                            onPromptSelected = { prompt ->
                                chatViewModel.chatInput = prompt.content
                            },
                            onRefresh = {
                                aiPromptViewModel.loadPrompts()
                            })
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
                input = chatViewModel.chatInput, onInputChange = { chatViewModel.chatInput = it }, onSend = {
                    if (chatViewModel.chatInput.isNotBlank()) {
                        chatViewModel.sendMessage()
                        chatViewModel.chatInput = ""
                    }
                }, enabled = chatViewModel.chatInput.isNotBlank()
            )
        }
    }


}

// Labubu风格的顶部栏
@Composable
private fun LabubuTopBar(
    onClose: () -> Unit, onNewChat: () -> Unit, heartBeat: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(72.dp).background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically
    ) {

        // 可爱的AI头像
        Box(
            modifier = Modifier.size(40.dp).scale(heartBeat).background(
                MaterialTheme.colorScheme.surface, CircleShape
            ), contentAlignment = Alignment.Center
        ) {
            Avatar()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = BuildKonfig.AI_NAME, style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = ComposeFontWeight.Bold, fontSize = 18.sp
                ), color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = BuildKonfig.AI_DESCRIPTION,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AddIconButton(text = "新建聊天") { onNewChat() }
        AddIconButton(text = "关闭", imageVector = Icons.Default.Close) { onClose() }
    }
}


// Labubu风格的聊天消息区
@Composable
private fun LabubuChatMessages(
    messages: List<ChatMessage>,
    scrollState: ScrollState,
    isAiThinking: Boolean = false,
    onRetryMessage: (String) -> Unit = {},
    onRetryUserMessage: (String) -> Unit = {},
    retryingMessageId: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(scrollState, enabled = true), verticalArrangement = Arrangement.Bottom
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
                isVisible = true, modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// Labubu风格的聊天气泡
@Composable
private fun LabubuChatBubble(
    chatMessage: ChatMessage,
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
        visible = visible, enter = slideInHorizontally(
            initialOffsetX = { if (chatMessage.isUser) it else -it }, animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (chatMessage.isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!chatMessage.isUser) {
                // AI头像
                Box(
                    modifier = Modifier.size(32.dp).background(
                        Brush.radialGradient(
                            colors = listOf(
                                LabubuColors.SoftBlue, LabubuColors.MintGreen
                            )
                        ), CircleShape
                    ).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center
                ) {

                    Avatar()
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            // 消息气泡容器
            Column {
                // 消息气泡（带复制按钮）
                Box(
                    modifier = Modifier.background(
                        brush = if (chatMessage.isUser) {
                            Brush.linearGradient(
                                colors = listOf(
                                    LabubuColors.PrimaryPink, LabubuColors.SecondaryPurple
                                )
                            )
                        } else if (chatMessage.isError) {
                            // 错误消息使用红色渐变
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFEBEE), Color(0xFFFFCDD2)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White, LabubuColors.LightPink
                                )
                            )
                        }, shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (chatMessage.isUser) 20.dp else 4.dp,
                            bottomEnd = if (chatMessage.isUser) 4.dp else 20.dp
                        )
                    ).border(
                        1.dp, if (chatMessage.isUser) Color.Transparent
                        else if (chatMessage.isError) Color(0xFFE57373)
                        else LabubuColors.PrimaryPink.copy(alpha = 0.3f), RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (chatMessage.isUser) 20.dp else 4.dp,
                            bottomEnd = if (chatMessage.isUser) 4.dp else 20.dp
                        )
                    ).widthIn(max = 280.dp)
                ) {
                    // 消息内容
                    Markdown(
                        content = chatMessage.content, modifier = Modifier.fillMaxWidth().padding(
                            start = 16.dp, end = if (chatMessage.isUser) 72.dp else 40.dp, // 用户消息需要更多右边距
                            top = 12.dp, bottom = 12.dp
                        )
                    )

                    // 右上角按钮组 - 复制和重新发送
                    val clipboardManager = LocalClipboardManager.current
                    var showCopyFeedback by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
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
                            }, modifier = Modifier.size(28.dp)
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
                            delay(1000)
                            showCopyFeedback = false
                        }

                        Box(
                            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(
                                Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp)
                            ).padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "已复制", color = Color.White, fontSize = 10.sp
                            )
                        }
                    }
                }

                // AI错误消息的重试按钮（保留在下方）
                if (chatMessage.canRetry && chatMessage.isError && !chatMessage.isUser) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.width(40.dp)) // 对齐AI头像

                        // AI重试按钮
                        OutlinedButton(
                            onClick = { onRetryMessage(chatMessage.id) },
                            enabled = !isRetrying,
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isRetrying) Color.Gray else LabubuColors.PrimaryPink
                            )
                        ) {
                            if (isRetrying) {
                                // 显示加载动画
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = LabubuColors.PrimaryPink
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("重试中...", fontSize = 12.sp)
                            } else {
                                Icon(
                                    Icons.Default.Replay, contentDescription = "重试", modifier = Modifier.size(14.dp)
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
                    modifier = Modifier.size(32.dp).background(
                        Brush.radialGradient(
                            colors = listOf(
                                LabubuColors.AccentYellow, LabubuColors.PrimaryPink
                            )
                        ), CircleShape
                    ).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "😊", fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun Avatar() {
    AsyncImage(
        model = BuildKonfig.AI_AVATAR_1,
        contentDescription = null,
    )
}

// 🤖 美化的AI提示词建议组件
@Composable
fun LabubuPromptSuggestions(
    prompts: List<SysAiPromptIso>,
    onPromptSelected: (SysAiPromptIso) -> Unit,
    onRefresh: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题区域
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Psychology,
                contentDescription = "AI提示词",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "常用提示词",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (prompts.isEmpty()) {
            // 空状态
            EmptyPromptState(onRefresh = onRefresh)
        } else {
            // 提示词网格
            PromptGrid(
                prompts = prompts, onPromptSelected = onPromptSelected
            )
        }
    }
}

/**
 * 空状态组件
 */
@Composable
private fun EmptyPromptState(onRefresh: () -> Unit) {
    site.addzero.component.card.AddCard(
//        backgroundType = MellumCardType.Light
//        , modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.OutdoorGrill,
                contentDescription = "暂无提示词",
                modifier = Modifier.size(32.dp),
                tint = LocalContentColor.current.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "暂无可用的提示词",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "刷新提示词",
                        modifier = Modifier.size(16.dp),
                        tint = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * 提示词网格组件 - 使用高阶组件
 */
@Composable
private fun PromptGrid(
    prompts: List<SysAiPromptIso>, onPromptSelected: (SysAiPromptIso) -> Unit
) {
    site.addzero.component.high_level.AddMultiColumnContainer(
        howMuchColumn = 1, items = prompts.map { prompt ->
            {
                PromptCard(
                    prompt = prompt, onSelected = { onPromptSelected(prompt) })
            }
        })
}

/**
 * 单个提示词卡片 - 参考HackathonCard样式
 */
@Composable
private fun PromptCard(
    prompt: SysAiPromptIso, onSelected: () -> Unit
) {
    val cardTypes = listOf(
        site.addzero.component.card.MellumCardType.Purple, site.addzero.component.card.MellumCardType.Blue, site.addzero.component.card.MellumCardType.Teal, site.addzero.component.card.MellumCardType.Orange
    )
    // 根据提示词ID选择卡片类型，确保一致性
    val cardType = cardTypes[(prompt.id?.toInt() ?: 0) % cardTypes.size]

    // 悬浮提示状态
    var showTooltip by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // 延迟显示提示，避免快速移动时闪烁
    LaunchedEffect(isHovered) {
        if (isHovered) {
            delay(500) // 延迟500ms显示
            showTooltip = true
        } else {
            showTooltip = false
        }
    }

    // 提示框透明度动画
    val tooltipAlpha by animateFloatAsState(
        targetValue = if (showTooltip && prompt.content.length > 50) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "tooltip_alpha"
    )

    Box {
        AddCard(
            onClick = onSelected,
            backgroundType = site.addzero.component.card.MellumCardType.Light,
            padding = 16.dp,
            modifier = Modifier.fillMaxWidth().hoverable(interactionSource)
        ) {
            Text(prompt.title)
//            ProductCardContent(
//                title = prompt.title,
//                subtitle = getPromptSubtitle(prompt.content),
//                icon = getPromptIcon(prompt.content),
//                description = prompt.content
//            )
        }

        // 悬浮提示框 - 显示完整内容
        if (tooltipAlpha > 0f) {
            Box(
                modifier = Modifier.fillMaxWidth().offset(y = (-12).dp).alpha(tooltipAlpha),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    modifier = Modifier.widthIn(max = 350.dp).padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shadowElevation = 12.dp,
                    tonalElevation = 8.dp
                ) {
                    Text(
                        text = prompt.content.trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier.padding(16.dp),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

/**
 * 根据提示词内容获取合适的图标
 */
private fun getPromptIcon(content: String): ImageVector {
    return when {
        content.contains("代码", ignoreCase = true) || content.contains(
            "编程",
            ignoreCase = true
        ) || content.contains("code", ignoreCase = true) -> Icons.Default.Code

        content.contains("写作", ignoreCase = true) || content.contains(
            "文章",
            ignoreCase = true
        ) || content.contains("write", ignoreCase = true) -> Icons.Default.Edit

        content.contains("翻译", ignoreCase = true) || content.contains(
            "translate",
            ignoreCase = true
        ) -> Icons.Default.Translate

        content.contains("分析", ignoreCase = true) || content.contains(
            "analyze",
            ignoreCase = true
        ) -> Icons.Default.Analytics

        content.contains("创意", ignoreCase = true) || content.contains(
            "创作",
            ignoreCase = true
        ) || content.contains("creative", ignoreCase = true) -> Icons.Default.Lightbulb

        content.contains("学习", ignoreCase = true) || content.contains(
            "教学",
            ignoreCase = true
        ) || content.contains("learn", ignoreCase = true) -> Icons.Default.School

        else -> Icons.Default.ChatBubbleOutline
    }
}

/**
 * 根据提示词内容生成副标题
 */
private fun getPromptSubtitle(content: String): String {
    return when {
        content.contains("代码", ignoreCase = true) || content.contains(
            "编程",
            ignoreCase = true
        ) || content.contains("code", ignoreCase = true) -> "代码助手"

        content.contains("写作", ignoreCase = true) || content.contains(
            "文章",
            ignoreCase = true
        ) || content.contains("write", ignoreCase = true) -> "写作助手"

        content.contains("翻译", ignoreCase = true) || content.contains("translate", ignoreCase = true) -> "翻译助手"

        content.contains("分析", ignoreCase = true) || content.contains("analyze", ignoreCase = true) -> "分析助手"

        content.contains("创意", ignoreCase = true) || content.contains(
            "创作",
            ignoreCase = true
        ) || content.contains("creative", ignoreCase = true) -> "创意助手"

        content.contains("学习", ignoreCase = true) || content.contains(
            "教学",
            ignoreCase = true
        ) || content.contains("learn", ignoreCase = true) -> "学习助手"

        content.contains("优化", ignoreCase = true) || content.contains("improve", ignoreCase = true) -> "优化助手"

        content.contains("测试", ignoreCase = true) || content.contains("test", ignoreCase = true) -> "测试助手"

        content.contains("设计", ignoreCase = true) || content.contains("design", ignoreCase = true) -> "设计助手"

        else -> "AI助手"
    }
}


