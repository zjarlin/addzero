package com.addzero.kmp.context

/**
 * Settings 扩展属性
 * 
 * 提供便捷的访问方式，与其他模块保持一致的命名风格
 */

/**
 * API客户端输出目录扩展属性
 * 兼容旧的命名方式
 */
val SettingContext.apiclientOutPutDir: String
    get() = settings.apiClientOutputDir

/**
 * 后端源码目录扩展属性
 */
val SettingContext.backendSourceDir: String
    get() = settings.backendSourceDir

/**
 * 后端编译目录扩展属性
 */
val SettingContext.backendBuildDir: String
    get() = settings.backendBuildDir

/**
 * 后端控制器输出目录扩展属性
 */
val SettingContext.backendControllerOutputDir: String
    get() = settings.backendControllerOutputDir

/**
 * 后端实体输出目录扩展属性
 */
val SettingContext.backendEntityOutputDir: String
    get() = settings.backendEntityOutputDir

/**
 * 同构体输出目录扩展属性
 */
val SettingContext.isomorphicOutputDir: String
    get() = settings.isomorphicOutputDir

/**
 * 表单输出目录扩展属性
 */
val SettingContext.formOutputDir: String
    get() = settings.formOutputDir

/**
 * 枚举输出目录扩展属性
 */
val SettingContext.enumOutputDir: String
    get() = settings.enumOutputDir

/**
 * Compose源码目录扩展属性
 */
val SettingContext.composeSourceDir: String
    get() = settings.composeSourceDir

/**
 * Compose编译目录扩展属性
 */
val SettingContext.composeBuildDir: String
    get() = settings.composeBuildDir

/**
 * Shared源码目录扩展属性
 */
val SettingContext.sharedSourceDir: String
    get() = settings.sharedSourceDir

/**
 * Shared编译目录扩展属性
 */
val SettingContext.sharedBuildDir: String
    get() = settings.sharedBuildDir
