package com.addzero.kmp.component.autocomplet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * 自动完成输入框组件的状态
 */
class AutoCompleteState<T>(
    val currentInput: String,
    val currentSelected: T?
)

/**
 * 自动完成输入框组件（函数式实现）
 *
 * @param title 输入框标题
 * @param suggestions 建议列表
 * @param maxSuggestions 最大显示建议数量
 * @param getLabelFun 从建议项中获取显示文本的函数
 * @param onItemSelected 当选择一个建议项时的回调
 * @param modifier 组件修饰符
 * @param initialValue 初始输入值
 * @param initialSelected 初始选中项
 * @return 组件状态对象，包含当前输入和选中项
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AddAutoComplete(
    title: String,
    suggestions: List<T>,
    maxSuggestions: Int = 5,
    getLabelFun: (T) -> String = { it.toString() },
    onItemSelected: (T) -> Unit = {},
    modifier: Modifier = Modifier,
    initialValue: String = "",
    initialSelected: T? = null
) {
    // 状态管理
    var inputValue by remember { mutableStateOf(TextFieldValue(text = initialValue)) }
    var selected by remember { mutableStateOf(initialSelected) }
    var showSuggestions by remember { mutableStateOf(false) }
    var selectedSuggestionIndex by remember { mutableStateOf(-1) }
    var hasFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 当选中一个建议项
    val onSuggestionSelected = { suggestion: T ->
        selected = suggestion
        onItemSelected(suggestion)
        val newText = getLabelFun(suggestion)
        inputValue = TextFieldValue(
            text = newText,
            selection = TextRange(newText.length) // 光标定位在末尾
        )
    }

    // 过滤建议列表
    val filteredSuggestions = remember(inputValue.text, suggestions) {
        if (inputValue.text.isBlank()) {
            suggestions.take(maxSuggestions)
        } else {
            suggestions.filter {
                getLabelFun(it).contains(inputValue.text, ignoreCase = true)
            }.take(maxSuggestions)
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = { newValue ->
                inputValue = newValue
                showSuggestions = newValue.text.isNotBlank() || hasFocus
                selectedSuggestionIndex = -1
                selected = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    hasFocus = focusState.isFocused
                    showSuggestions = focusState.isFocused
                    if (focusState.isFocused) {
                        // 聚焦时将光标移到末尾
                        inputValue = inputValue.copy(
                            selection = TextRange(inputValue.text.length)
                        )
                    }
                }
                .onKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Tab -> {
                            if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                                selectedSuggestionIndex =
                                    (selectedSuggestionIndex + 1) % filteredSuggestions.size
                                true
                            } else {
                                false
                            }
                        }

                        Key.DirectionUp -> {
                            if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                                selectedSuggestionIndex = (selectedSuggestionIndex - 1).coerceAtLeast(0)
                                true
                            } else {
                                false
                            }
                        }

                        Key.DirectionDown -> {
                            if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                                selectedSuggestionIndex =
                                    (selectedSuggestionIndex + 1) % filteredSuggestions.size
                                true
                            } else {
                                false
                            }
                        }

                        Key.Enter -> {
                            if (showSuggestions && selectedSuggestionIndex >= 0) {
                                onSuggestionSelected(filteredSuggestions[selectedSuggestionIndex])
                                showSuggestions = false
                                keyboardController?.hide()
                                true
                            } else {
                                false
                            }
                        }

                        Key.Backspace -> {
                            if (inputValue.text.isEmpty()) {
                                showSuggestions = hasFocus
                            }
                            false
                        }

                        Key.Escape -> {
                            showSuggestions = false
                            true
                        }

                        else -> false
                    }
                }
                .focusRequester(focusRequester),
            label = { Text(title) },
            placeholder = { Text("请输入$title") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (showSuggestions && selectedSuggestionIndex >= 0) {
                        onSuggestionSelected(filteredSuggestions[selectedSuggestionIndex])
                    }
                    showSuggestions = false
                    keyboardController?.hide()
                }
            )
        )

        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .background(Color.White)
                ) {
                    items(filteredSuggestions) { suggestion ->
                        val isSelected = filteredSuggestions.indexOf(suggestion) == selectedSuggestionIndex
                        Text(
                            text = getLabelFun(suggestion),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSuggestionSelected(suggestion)
                                    showSuggestions = false
                                    keyboardController?.hide()
                                }
                                .background(if (isSelected) Color.LightGray else Color.White)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // 返回当前状态对象
//    return AutoCompleteState(
//        currentInput = inputValue.text,
//        currentSelected = selected
//    )
}
