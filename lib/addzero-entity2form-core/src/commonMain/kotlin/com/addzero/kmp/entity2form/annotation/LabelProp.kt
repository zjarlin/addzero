package com.addzero.kmp.entity2form.annotation

/**
 * 标记实体类中用作显示标签的属性
 * 
 * 用法：在实体类的属性上添加此注解，表示该属性应该用作下拉选择等组件的显示文本
 * 
 * 例如：
 * ```kotlin
 * interface SysDept {
 *     val id: Long
 *     
 *     @LabelProp
 *     val name: String  // 这个属性将用作显示标签
 *     
 *     val code: String
 * }
 * ```
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class LabelProp
