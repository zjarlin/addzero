package com.addzero.kmp.annotation

/**
 * 标记需要生成辅助工具的Compose函数
 * 自动生成State类、Widget函数、remember函数等完整的辅助工具集
 * 类似Vue的$attrs功能，但提供更完整的Compose开发支持
 *
 * 使用示例：
 * ```kotlin
 * @ComposeAssist
 * @Composable
 * fun MyText(
 *     text: String,
 *     color: Color = Color.Black,
 *     fontSize: TextUnit = 14.sp,
 *     @AssistExclude modifier: Modifier = Modifier
 * ) {
 *     Text(text = text, color = color, fontSize = fontSize, modifier = modifier)
 * }
 * ```
 *
 * 生成的代码将包含：
 * - MyTextState数据类
 * - rememberMyTextState函数
 * - MyTextWidget辅助函数
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ComposeAssist

