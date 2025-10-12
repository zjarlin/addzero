package site.addzero.component.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import site.addzero.themes.colors
import site.addzero.themes.radius

/**
 * 搜索框大小枚举
 */
enum class SearchInputSize {
    Small,
    Medium,
    Large
}

/**
 * 搜索框变体枚举
 */
enum class SearchInputVariant {
    Outline,    // 轮廓样式（默认）
    Filled,     // 填充样式
    Ghost       // 幽灵样式（无边框）
}

/**
 * Shadcn 风格的搜索框组件
 * 基于 OutlinedTextField 实现，提供更好的用户体验和样式
 *
 * @param value 当前搜索值
 * @param onValueChange 值变化回调
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param size 搜索框大小
 * @param variant 搜索框变体
 * @param enabled 是否启用
 * @param readOnly 是否只读
 * @param onSearch 搜索回调
 * @param onClearSearch 清除搜索回调
 * @param showClearButton 是否显示清除按钮
 * @param leadingIcon 前置图标
 * @param trailingIcon 后置图标
 * @param keyboardOptions 键盘选项
 * @param keyboardActions 键盘动作
 */
@Composable
fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    size: SearchInputSize = SearchInputSize.Medium,
    variant: SearchInputVariant = SearchInputVariant.Outline,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onSearch: () -> Unit = {},
    onClearSearch: () -> Unit = { onValueChange("") },
    showClearButton: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            tint = colors.mutedForeground
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = { onSearch() })
) {
    // 根据大小设置高度
    val height = when (size) {
        SearchInputSize.Small -> 36.dp
        SearchInputSize.Medium -> 40.dp
        SearchInputSize.Large -> 48.dp
    }

    // 根据变体设置样式
    val textFieldColors = when (variant) {
        SearchInputVariant.Outline -> OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.ring,
            unfocusedBorderColor = colors.input,
            disabledBorderColor = colors.border,
            errorBorderColor = colors.destructive,
            focusedTextColor = colors.foreground,
            unfocusedTextColor = colors.foreground,
            disabledTextColor = colors.mutedForeground,
            cursorColor = colors.primary,
            focusedPlaceholderColor = colors.mutedForeground,
            unfocusedPlaceholderColor = colors.mutedForeground,
            disabledPlaceholderColor = colors.mutedForeground,
            focusedLeadingIconColor = colors.mutedForeground,
            unfocusedLeadingIconColor = colors.mutedForeground,
            focusedTrailingIconColor = colors.mutedForeground,
            unfocusedTrailingIconColor = colors.mutedForeground
        )
        SearchInputVariant.Filled -> TextFieldDefaults.colors(
            focusedTextColor = colors.foreground,
            unfocusedTextColor = colors.foreground,
            disabledTextColor = colors.mutedForeground,
            cursorColor = colors.primary,
            focusedIndicatorColor = colors.ring,
            unfocusedIndicatorColor = colors.border,
            disabledIndicatorColor = colors.mutedForeground,
            errorIndicatorColor = colors.destructive,
            focusedContainerColor = colors.muted,
            unfocusedContainerColor = colors.background,
            disabledContainerColor = colors.muted,
            errorContainerColor = colors.destructive.copy(alpha = 0.1f),
            focusedPlaceholderColor = colors.mutedForeground,
            unfocusedPlaceholderColor = colors.mutedForeground,
            disabledPlaceholderColor = colors.mutedForeground,
            focusedLeadingIconColor = colors.mutedForeground,
            unfocusedLeadingIconColor = colors.mutedForeground,
            focusedTrailingIconColor = colors.mutedForeground,
            unfocusedTrailingIconColor = colors.mutedForeground
        )
        SearchInputVariant.Ghost -> OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.ring,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = colors.destructive,
            focusedTextColor = colors.foreground,
            unfocusedTextColor = colors.foreground,
            disabledTextColor = colors.mutedForeground,
            cursorColor = colors.primary,
            focusedPlaceholderColor = colors.mutedForeground,
            unfocusedPlaceholderColor = colors.mutedForeground,
            disabledPlaceholderColor = colors.mutedForeground,
            focusedLeadingIconColor = colors.mutedForeground,
            unfocusedLeadingIconColor = colors.mutedForeground,
            focusedTrailingIconColor = colors.mutedForeground,
            unfocusedTrailingIconColor = colors.mutedForeground,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    }

    // 组合后置图标
    val combinedTrailingIcon: @Composable (() -> Unit)? = {
        Row {
            // 自定义后置图标
            trailingIcon?.invoke()

            // 清除按钮
            if (showClearButton && value.isNotEmpty() && !readOnly) {
                IconButton(
                    onClick = onClearSearch,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清除",
                        tint = colors.mutedForeground
                    )
                }
            }
        }
    }

    when (variant) {
        SearchInputVariant.Outline, SearchInputVariant.Ghost -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier.height(height),
                enabled = enabled,
                readOnly = readOnly,
                placeholder = { Text(placeholder) },
                leadingIcon = leadingIcon,
                trailingIcon = combinedTrailingIcon,
                colors = textFieldColors,
                singleLine = true,
                shape = RoundedCornerShape(MaterialTheme.radius.md),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions
            )
        }
        SearchInputVariant.Filled -> {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier.height(height),
                enabled = enabled,
                readOnly = readOnly,
                placeholder = { Text(placeholder) },
                leadingIcon = leadingIcon,
                trailingIcon = combinedTrailingIcon,
                colors = textFieldColors,
                singleLine = true,
                shape = RoundedCornerShape(MaterialTheme.radius.md),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions
            )
        }
    }
}

/**
 * 简化版搜索框组件
 * 使用默认轮廓样式
 */
@Composable
fun SimpleSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    onSearch: () -> Unit = {},
    onClearSearch: () -> Unit = { onValueChange("") }
) {
    SearchInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        size = SearchInputSize.Medium,
        variant = SearchInputVariant.Outline,
        onSearch = onSearch,
        onClearSearch = onClearSearch
    )
}

/**
 * 幽灵样式搜索框
 * 常用于导航栏等需要无边框的场合
 */
@Composable
fun GhostSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    size: SearchInputSize = SearchInputSize.Small,
    onSearch: () -> Unit = {},
    onClearSearch: () -> Unit = { onValueChange("") }
) {
    SearchInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        size = size,
        variant = SearchInputVariant.Ghost,
        onSearch = onSearch,
        onClearSearch = onClearSearch
    )
}

/**
 * 轮廓样式搜索框
 * 常用于表单等需要明确边框的场合
 */
@Composable
fun OutlineSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    size: SearchInputSize = SearchInputSize.Medium,
    onSearch: () -> Unit = {},
    onClearSearch: () -> Unit = { onValueChange("") }
) {
    SearchInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        size = size,
        variant = SearchInputVariant.Outline,
        onSearch = onSearch,
        onClearSearch = onClearSearch
    )
}

/**
 * 填充样式搜索框
 * 常用于需要突出显示的场合
 */
@Composable
fun FilledSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    size: SearchInputSize = SearchInputSize.Medium,
    onSearch: () -> Unit = {},
    onClearSearch: () -> Unit = { onValueChange("") }
) {
    SearchInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        size = size,
        variant = SearchInputVariant.Filled,
        onSearch = onSearch,
        onClearSearch = onClearSearch
    )
}
