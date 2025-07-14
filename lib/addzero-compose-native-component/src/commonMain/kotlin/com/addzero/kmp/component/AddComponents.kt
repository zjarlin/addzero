package com.addzero.kmp.component

/**
 * 🎯 AddZero 组件库统一 API
 * 
 * 这个文件提供了所有组件的统一访问入口，业务代码只需要导入这个包，
 * 无需关心具体的组件实现来自哪个模块（原生组件还是三方库组件）。
 * 
 * 使用方式：
 * ```kotlin
 * import com.addzero.kmp.component.*
 * 
 * // 直接使用组件，无需关心实现细节
 * AddTextField(...)
 * AddButton(...)
 * AddFileUploader(...)
 * ```
 */

// ================================
// 原生组件 API 暴露
// ================================

// 表单组件 - 直接使用原来的包名，无需 typealias
// typealias AddTextField = com.addzero.kmp.component.form.text.AddTextField
// typealias AddPasswordField = com.addzero.kmp.component.form.text.AddPasswordField
// typealias AddEmailField = com.addzero.kmp.component.form.text.AddEmailField
// typealias AddPhoneField = com.addzero.kmp.component.form.text.AddPhoneField

// 按钮组件 - 直接使用原来的包名，无需 typealias
// typealias AddButton = com.addzero.kmp.component.button.AddButton
// typealias AddButtonType = com.addzero.kmp.component.button.AddButtonType
// typealias AddDeleteButton = com.addzero.kmp.component.button.AddDeleteButton
// typealias AddConfirmButton = com.addzero.kmp.component.button.AddConfirmButton
// typealias AddCancelButton = com.addzero.kmp.component.button.AddCancelButton
// typealias AddSaveButton = com.addzero.kmp.component.button.AddSaveButton

// ================================
// 三方库组件 API 暴露
// ================================
// 这些将在 addzero-compose-klibs-component 模块中定义

// 文件相关组件
// typealias AddFileUploader = com.addzero.kmp.component.klibs.file.AddFileUploader
// typealias AddFilePicker = com.addzero.kmp.component.klibs.file.AddFilePicker

// 网络相关组件
// typealias AddImageLoader = com.addzero.kmp.component.klibs.image.AddImageLoader

// 消息提示组件
// typealias AddToast = com.addzero.kmp.component.klibs.toast.AddToast

// ================================
// 组件工厂函数（可选）
// ================================

/**
 * 创建标准的表单文本输入框
 */
@androidx.compose.runtime.Composable
fun createTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) = com.addzero.kmp.component.form.text.AddTextField(
    value = value,
    onValueChange = onValueChange,
    label = label,
    isRequired = isRequired,
    modifier = modifier
)

/**
 * 创建标准的主要操作按钮
 */
@androidx.compose.runtime.Composable
fun createPrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loading: Boolean = false,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) = com.addzero.kmp.component.button.AddButton(
    text = text,
    onClick = onClick,
    enabled = enabled,
    loading = loading,
    type = com.addzero.kmp.component.button.AddButtonType.Primary,
    modifier = modifier
)

/**
 * 创建标准的次要操作按钮
 */
@androidx.compose.runtime.Composable
fun createSecondaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) = com.addzero.kmp.component.button.AddButton(
    text = text,
    onClick = onClick,
    enabled = enabled,
    type = com.addzero.kmp.component.button.AddButtonType.Secondary,
    modifier = modifier
)
