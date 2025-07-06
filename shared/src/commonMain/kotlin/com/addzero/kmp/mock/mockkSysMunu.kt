package com.addzero.kmp.mock

import com.addzero.kmp.entity.sys.menu.SysMenuVO

/**
 * todo 目前菜单由前台ksp路由表接管,后续接入RBAC权限可以把路由表送入后台处理
 * @return [Map<String, SysMenuVO>]
 */
@Deprecated("目前菜单由前台ksp路由表接管,后续接入RBAC权限可以把路由表送入后台处理")
fun mockkSysMunu(): Map<String, SysMenuVO> {


    val defaultSysMenuVO = mapOf(

        "/home" to SysMenuVO(
            path = "/home", title = "首页", icon = "Home", sort = 1.0
        ), "/system/user" to SysMenuVO(
            path = "/system/user", title = "用户管理", parentPath = "/system",
            icon = "Person", sort = 1.0
        ), "/system/role" to SysMenuVO(
            path = "/system/role", title = "角色管理", parentPath = "/system",
            icon = "Group", sort = 2.0
        ), "/system/permission" to SysMenuVO(
            path = "/system/permission", title = "权限管理", parentPath =
                "/system", icon = "Security", sort = 3.0
        ), "/system/menu" to SysMenuVO(
            path = "/system/menu", title = "菜单管理", parentPath = "/system",
            icon = "Menu", sort = 4.0
        ), "/system/dept" to SysMenuVO(
            path = "/system/dept", title = "部门管理", parentPath = "/system", icon = "Business", sort = 5.0
        ), "/system/dict" to SysMenuVO(
            path = "/system/dict", title = "数据字典", parentPath = "/system", icon = "Book", sort = 6.0
        ), "/system/log" to SysMenuVO(
            path = "/system/log", title = "操作日志", parentPath = "/system", icon = "History", sort = 7.0
        ), "/system" to SysMenuVO(
            path = "/system", title = "系统管理", icon = "Settings", sort = 2.0
        ), "/demo/example1" to SysMenuVO(
            path = "/demo/example1", title = "示例1", parentPath = "/demo", icon = "Favorite", sort = 1.0
        ), "/demo/example2" to SysMenuVO(
            path = "/demo/example2", title = "示例2", parentPath = "/demo", icon = "Star", sort = 2.0
        ), "/demo/addbutton" to SysMenuVO(
            path = "/demo/addbutton", title = "按钮示例", parentPath = "/demo", icon = "AddCircle", sort = 3.0
        ), "/demo/markdown" to SysMenuVO(
            path = "/demo/markdown", title = "Markdown示例", parentPath = "/demo", icon = "Description", sort = 4.0
        ), "/demo/table-example" to SysMenuVO(
            path = "/demo/table-example", title = "表格示例", parentPath = "/demo", icon = "List", sort = 5.0
        ), "/demo" to SysMenuVO(
            path = "/demo", title = "功能示例", icon = "Code", sort = 3.0
        )
    )
    return defaultSysMenuVO
}
