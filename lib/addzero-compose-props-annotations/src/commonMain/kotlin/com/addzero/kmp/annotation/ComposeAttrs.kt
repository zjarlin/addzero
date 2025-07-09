package com.addzero.kmp.annotation

/**
 * 标记需要生成$attrs的Compose函数
 * 类似Vue的$attrs功能，为Compose函数生成参数打包
 * 
 * 使用示例：
 * ```kotlin
 * @ComposeAttrs
 * @Composable
 * fun MyText(
 *     text: String,
 *     color: Color = Color.Black,
 *     fontSize: TextUnit = 14.sp,
 *     @AttrsExclude modifier: Modifier = Modifier
 * ) {
 *     Text(text = text, color = color, fontSize = fontSize, modifier = modifier)
 * }
 * ```
 * 
 * 生成的代码将包含：
 * - MyTextAttrs数据类
 * - $attrs扩展属性
 * - 构建器函数和应用函数
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ComposeAttrs

/**
 * 排除某些参数不包含在$attrs中
 * 通常用于排除modifier、content等特殊参数
 * 
 * 使用示例：
 * ```kotlin
 * @ComposeAttrs
 * @Composable
 * fun MyComponent(
 *     title: String,
 *     subtitle: String,
 *     @AttrsExclude modifier: Modifier = Modifier,
 *     @AttrsExclude content: @Composable () -> Unit = {}
 * ) {
 *     // 组件实现
 * }
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrsExclude
