package com.addzero.kmp.entity.sys.menu

import kotlinx.serialization.Serializable

enum class EnumSysMenuType {
    MENU, SCREEN
}

/**
 * 菜单元数据
 * 用于持久化存储和API传输
 */
@Serializable
data class SysMenuVO(
    val path: String,
    val title: String,
    val parentPath: String? = null,
    val icon: String = "",
    val sort: Double = 0.0,
    val permissionCode: String? = null,
    val visible: Boolean = true,
    val disabled: Boolean = false,
    var children: MutableList<SysMenuVO> = mutableListOf(),
    val enumSysMenuType: EnumSysMenuType = EnumSysMenuType.SCREEN
)