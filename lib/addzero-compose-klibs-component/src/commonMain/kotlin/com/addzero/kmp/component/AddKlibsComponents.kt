package com.addzero.kmp.component

/**
 * 🎯 AddZero 三方库组件统一 API
 *
 * 这个文件提供了所有三方库组件的统一访问入口，
 * 扩展了原生组件库的功能。
 */

// ================================
// 文件相关组件 API 暴露
// ================================

// 文件上传组件 - 直接使用原来的包名，无需 typealias
// typealias AddFileUploader = com.addzero.kmp.component.file_picker.AddFileUploader
// typealias AddFilePicker = com.addzero.kmp.component.file_picker.AddFilePicker
// typealias FileUploadState = com.addzero.kmp.component.file_picker.FileUploadState
// typealias UploadedFile = com.addzero.kmp.component.file_picker.UploadedFile

// 文件工具函数
// val formatFileSize = com.addzero.kmp.component.file_picker::formatFileSize

// ================================
// 图像相关组件 API 暴露
// ================================
// TODO: 基于 Coil 的图像加载组件
// typealias AddAsyncImage = com.addzero.kmp.component.klibs.image.AddAsyncImage
// typealias AddImageViewer = com.addzero.kmp.component.klibs.image.AddImageViewer

// ================================
// 网络相关组件 API 暴露
// ================================
// TODO: 基于 Ktor 的网络组件
// typealias AddApiClient = com.addzero.kmp.component.klibs.network.AddApiClient

// ================================
// 依赖注入相关组件 API 暴露
// ================================
// TODO: 基于 Koin 的依赖注入组件
// typealias AddKoinScope = com.addzero.kmp.component.klibs.di.AddKoinScope

// ================================
// 组件工厂函数
// ================================

/**
 * 创建标准的文件上传器
 */
@androidx.compose.runtime.Composable
fun createFileUploader(
    onFilesSelected: (com.addzero.kmp.component.file_picker.PlatformFiles) -> Unit,
    allowMultiple: Boolean = false,
    maxFileSize: Long = 10 * 1024 * 1024, // 10MB
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) = com.addzero.kmp.component.file_picker.AddFileUploader(
    onFilesSelected = onFilesSelected,
    allowMultiple = allowMultiple,
    maxFileSize = maxFileSize,
    modifier = modifier
)

/**
 * 创建图片上传器
 */
@androidx.compose.runtime.Composable
fun createImageUploader(
    onFilesSelected: (com.addzero.kmp.component.file_picker.PlatformFiles) -> Unit,
    allowMultiple: Boolean = false,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) = com.addzero.kmp.component.file_picker.AddFileUploader(
    onFilesSelected = onFilesSelected,
    allowMultiple = allowMultiple,
    acceptedTypes = listOf("jpg", "jpeg", "png", "gif", "webp"),
    maxFileSize = 5 * 1024 * 1024, // 5MB for images
    modifier = modifier
)

/**
 * 创建文档上传器
 */
@androidx.compose.runtime.Composable
fun createDocumentUploader(
    onFilesSelected: (com.addzero.kmp.component.file_picker.PlatformFiles) -> Unit,
    allowMultiple: Boolean = true,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) = com.addzero.kmp.component.file_picker.AddFileUploader(
    onFilesSelected = onFilesSelected,
    allowMultiple = allowMultiple,
    acceptedTypes = listOf("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"),
    maxFileSize = 20 * 1024 * 1024, // 20MB for documents
    modifier = modifier
)
