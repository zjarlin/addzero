package com.addzero.kmp.config

/**
 * 代码生成配置
 *
 * 统一管理同构体和表单的生成路径和配置
 */
object GenerationConfig {

    /**
     * 同构体生成配置
     */
    object Isomorphic {
        // 生成到 shared 模块源码目录
        const val OUTPUT_DIR = "shared/src/commonMain/kotlin/com/addzero/kmp/isomorphic"
        const val PACKAGE_NAME = "com.addzero.kmp.isomorphic"
        const val SUFFIX = "Iso"
    }

    /**
     * 表单生成配置
     */
    object Form {
        // 生成到 composeApp 模块 build 目录（KSP 生成目录）
        const val OUTPUT_DIR = "composeApp/build/generated/ksp/commonMain/kotlin/com/addzero/kmp/forms"

        // 也可以选择生成到源码目录（开发时方便查看）
        const val SOURCE_OUTPUT_DIR = "composeApp/src/commonMain/kotlin/com/addzero/kmp/forms"
        const val PACKAGE_NAME = "com.addzero.kmp.forms"
    }

    /**
     * 实体相关配置
     */
    object Entity {
        const val JIMMER_PACKAGE = "com.addzero.web.modules"
        const val ANNOTATION = "org.babyfish.jimmer.sql.Entity"
    }

    /**
     * 获取实际的表单输出目录
     *
     * @param useBuildDir 是否使用 build 目录，true=build目录，false=源码目录
     */
    fun getFormOutputDir(useBuildDir: Boolean = true): String {
        return if (useBuildDir) Form.OUTPUT_DIR else Form.SOURCE_OUTPUT_DIR
    }

    /**
     * 跨模块生成策略
     *
     * 当前架构：
     * - backend 模块：分析 Jimmer 实体符号
     * - shared 模块：生成同构体类（源码目录）
     * - composeApp 模块：生成表单代码（build 目录）
     *
     * 这种跨模块生成的优势：
     * 1. 同构体在 shared 模块，可被多个模块共享
     * 2. 表单在 composeApp 模块，专门用于 UI
     * 3. 使用 build 目录避免源码污染，支持增量编译
     */
    object CrossModule {
        /**
         * 是否启用跨模块生成
         */
        const val ENABLED = true

        /**
         * 同构体生成：backend -> shared
         */
        const val ISO_CROSS_MODULE = true

        /**
         * 表单生成：backend -> composeApp
         */
        const val FORM_CROSS_MODULE = true
    }
}
