package com.addzero.kmp.jdbc.meta.public.table.impl

import com.addzero.kmp.jdbc.meta.public.table.IBizDotfiles
             import kotlinx.serialization.Serializable

/**
 * IBizDotfilesImpl 实现类
 * 实现 IBizDotfiles 接口
 * 配置文件管理
 */
@Serializable
data class IBizDotfilesImpl(
    override val id: Long,
        override val osType: Long,
        override val osStructure: String,
        override val defType: String,
        override val name: String,
        override val value: String,
        override val describtion: String?,
        override val status: Long,
        override val fileUrl: String?,
        override val location: String?
) : IBizDotfiles
