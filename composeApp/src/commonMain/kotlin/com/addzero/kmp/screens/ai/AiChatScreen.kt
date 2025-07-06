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
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.addzero.kmp.component.background.ChatBackground
import com.addzero.kmp.component.background.ChatBackgroundSelectorDialog
import com.addzero.kmp.component.text.SafeSelectionContainer
import com.addzero.kmp.settings.SettingContext4Compose.AI_AVATAR_1
import com.addzero.kmp.settings.SettingContext4Compose.AI_WELCOME_MSG
import com.addzero.kmp.viewmodel.ChatBackgroundViewModel
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
    val backgroundViewModel = koinViewModel<ChatBackgroundViewModel>()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }

    // 可爱的动画效果
    val heartBeat by rememberInfiniteTransition(label = "heartBeat").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "heartBeat"
    )

    ChatBackground(
        config = backgroundViewModel.currentBackground,
        modifier = Modifier
            .width(420.dp)
            .fillMaxHeight()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
                clip = false
            )
            .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
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
                onBackgroundSettings = { backgroundViewModel.showSelector() },
                heartBeat = heartBeat
            )
            // Labubu风格的聊天消息区 - 使用SafeSelectionContainer包装
            SafeSelectionContainer(
                modifier = Modifier.weight(1f)
            ) {
                LabubuChatMessages(
                    messages = chatViewModel.chatMessages,
                    scrollState = scrollState,
                    isAiThinking = chatViewModel.isAiThinking,
                    modifier = Modifier.fillMaxSize()
                )
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

    // 背景选择器对话框
    ChatBackgroundSelectorDialog(
        backgroundViewModel = backgroundViewModel,
        onDismiss = { backgroundViewModel.hideSelector() }
    )
}

// Labubu风格的顶部栏
@Composable
private fun LabubuTopBar(
    onClose: () -> Unit,
    onNewChat: () -> Unit,
    onBackgroundSettings: () -> Unit,
    heartBeat: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        LabubuColors.PrimaryPink,
                        LabubuColors.SecondaryPurple
                    )
                )
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 可爱的AI头像
        Box(
            modifier = Modifier
                .size(40.dp)
                .scale(heartBeat)
                .background(
                    Color.White,
                    CircleShape
                )
                .border(2.dp, LabubuColors.AccentYellow, CircleShape),
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
                text = "Labubu AI",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = Color.White
            )
            Text(
                text = "在线 • 随时为您服务",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 背景设置按钮
        IconButton(
            onClick = onBackgroundSettings,
            modifier = Modifier
                .size(36.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Palette,
                contentDescription = "背景设置",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

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
                    Brush.radialGradient(
                        colors = listOf(
                            LabubuColors.AccentYellow.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.3f)
                        )
                    ),
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
                    tint = Color.White,
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
                    Color.White.copy(alpha = 0.2f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "关闭",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


// Labubu风格的聊天消息区
@Composable
private fun LabubuChatMessages(
    messages: List<Pair<Boolean, String>>,
    scrollState: ScrollState,
    isAiThinking: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(scrollState, enabled = true),
        verticalArrangement = Arrangement.Bottom
    ) {
        // 欢迎消息
        if (messages.isEmpty()) {
            LabubuWelcomeMessage(AI_WELCOME_MSG)
        }

        // 聊天消息
        messages.forEachIndexed { index, (isUser, msg) ->
            LabubuChatBubble(
                message = msg,
                isUser = isUser,
                animationDelay = index * 100
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

// Labubu风格的欢迎消息
@Composable
private fun LabubuWelcomeMessage(welcomMsg: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 大号可爱头像
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            LabubuColors.AccentYellow,
                            LabubuColors.PrimaryPink
                        )
                    ),
                    CircleShape
                )
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {

            Avatar()

//            Text(
//                text = "🤖",
//                fontSize = 40.sp
//            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = welcomMsg,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "有什么可以帮助您的吗？ 💕",
            style = MaterialTheme.typography.bodyLarge,
            color = LabubuColors.LightText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 可爱的装饰
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            LabubuColors.PrimaryPink.copy(alpha = 0.6f),
                            CircleShape
                        )
                )
            }
        }
    }
}

// Labubu风格的聊天气泡
@Composable
private fun LabubuChatBubble(
    message: String,
    isUser: Boolean,
    animationDelay: Int = 0
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
            initialOffsetX = { if (isUser) it else -it },
            animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
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

            // 消息气泡
            Box(
                modifier = Modifier
                    .background(
                        brush = if (isUser) {
                            Brush.linearGradient(
                                colors = listOf(
                                    LabubuColors.PrimaryPink,
                                    LabubuColors.SecondaryPurple
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
                            bottomStart = if (isUser) 20.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 20.dp
                        )
                    )
                    .border(
                        1.dp,
                        if (isUser) Color.Transparent else LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (isUser) 20.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 20.dp
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 280.dp)
            ) {
                Markdown( message)
//                Text(
//                    text = message,
//                    color = if (isUser) Color.White else LabubuColors.DarkText,
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        lineHeight = 20.sp
//                    )
//                )
            }

            if (isUser) {
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

