package site.addzero.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import site.addzero.di.NavgationViewModel
import site.addzero.entity.sys.menu.EnumSysMenuType
import site.addzero.entity.sys.menu.SysMenuVO
import site.addzero.generated.RouteKeys
import site.addzero.util.data_structure.tree.List2TreeUtil
import org.koin.android.annotation.KoinViewModel

/**
 * 侧边路由菜单视图
 * @author zjarlin
 * @date 2025/09/29
 * @constructor 创建[SysRouteViewModel]
 */
@KoinViewModel
class SysRouteViewModel() : ViewModel() {
    fun updateRoute(string: String) {
        currentRoute = string
        NavgationViewModel.navigate(currentRoute)
    }
    //默认展开
    var isExpand by mutableStateOf(true)
    var currentRoute by mutableStateOf(RouteKeys.HOME_SCREEN)
    var keyword by mutableStateOf("")
        private set

    /**
     * 🎯 当前路由的元数据
     * 使用 derivedStateOf 确保只有当 currentRoute 变化时才重新计算
     */
    val currentRouteMetadata by derivedStateOf {
        RouteKeys.allMeta.find { it.routePath == currentRoute }
    }

    /**
     * 🏷️ 当前路由的标题
     * 使用 derivedStateOf 基于 currentRouteMetadata 计算
     */
    val currentRouteTitle by derivedStateOf {
        currentRouteMetadata?.title ?: "未知页面"
    }

    /**
     * 🎨 当前路由的图标
     * 使用 derivedStateOf 基于 currentRouteMetadata 计算
     */
    val currentRouteIcon by derivedStateOf {
        currentRouteMetadata?.icon ?: ""
    }

    /**
     * 📂 当前路由的分组
     * 使用 derivedStateOf 基于 currentRouteMetadata 计算
     */
    val currentRouteGroup by derivedStateOf {
        currentRouteMetadata?.value ?: ""
    }

    /**
     * 🔢 当前路由的排序
     * 使用 derivedStateOf 基于 currentRouteMetadata 计算
     */
    val currentRouteOrder by derivedStateOf {
        currentRouteMetadata?.order ?: 0.0
    }

    /**
     * 🔗 当前路由的完全限定名
     * 使用 derivedStateOf 基于 currentRouteMetadata 计算
     */
    val currentRouteQualifiedName by derivedStateOf {
        currentRouteMetadata?.qualifiedName ?: ""
    }

    var cacleBreadcrumb by mutableStateOf(run {

        val flatMenuList = getAllSysMenu()
//        val associate = flatMenuList.associate { it.path to it }

        val allSysMenuToTree = allSysMenuToTree(flatMenuList)


//        val vO = associate[currentRoute]

        val breadcrumb = List2TreeUtil.getBreadcrumbList<SysMenuVO>(
            list = allSysMenuToTree,
            targetId = currentRoute,
            getId = SysMenuVO::path,
            getParentId = SysMenuVO::parentPath,
            getChildren = SysMenuVO::children,
            setChildren = { self, children -> self.children = children },
        )

//        val search = treeClient(
//            getId = SysMenuVO::path,
//            getParentId = SysMenuVO::parentPath,
//            setChildren = { c -> children = c.toMutableList() },
////            setChildren = SysMenuVO::children.setter,
//            dataList = allSysMenuToTree,
//            getChildren = SysMenuVO::children,
//        ).search<SysMenuVO> {
//            SysMenuVO::path eq currentRoute
//        }

        breadcrumb
    })

    var menuItems by mutableStateOf(
        run {
            val flatMenuList = getAllSysMenu()
            val allSysMenuToTree = allSysMenuToTree(flatMenuList)
            allSysMenuToTree
        }

    )

    private fun allSysMenuToTree(flatMenuList: List<SysMenuVO>): List<SysMenuVO> {
        val buildTree = List2TreeUtil.list2Tree(
            source = flatMenuList,
            idFun = { it.path },
            pidFun = { it.parentPath },
            getChildFun = { it.children },
            setChildFun = { self, children -> self.children = children.toMutableList() })


//        val buildTree = TreeUtil.buildTree(list = flatMenuList, getId = { it.path }, getParentId = { it.parentPath }, setChildren = { c -> children = c.toMutableList() })
        return buildTree
    }

    private fun getAllSysMenu(): List<SysMenuVO> {
        var allMeta = RouteKeys.allMeta


        val visualNode = allMeta.filter { it.value.isNotBlank() }.map { it.value }.distinct().map {
            SysMenuVO(
                path = it, title = it, enumSysMenuType = EnumSysMenuType.MENU
            )
        }
        val associate = visualNode.associate { it.title to it.path }


        val menuDict = allMeta.associate { it.title to it.routePath }
        val allDict = menuDict + associate


        val flatMenuList = allMeta.map {
            val sysMenuVO = SysMenuVO(
                path = it.routePath,
                title = it.title,
                parentPath = run {
                    val groupName = it.value
                    val route = allDict[groupName]
                    route
                },
                icon = it.icon,
                sort = it.order,
                permissionCode = null,
            )
            sysMenuVO
        }
        return visualNode + flatMenuList
//        return flatMenuList
    }

    /**
     * 🗺️ 路由元数据映射表
     * 使用 derivedStateOf 缓存路由映射，避免重复查找
     */
    private val routeMetadataMap by derivedStateOf {
        RouteKeys.allMeta.associateBy { it.routePath }
    }

    /**
     * 🔍 根据路由键获取菜单项
     * 基于缓存的元数据创建菜单项
     */
    fun getRouteByKey(routeKey: String): SysMenuVO? {
        val metadata = routeMetadataMap[routeKey] ?: return null
        return SysMenuVO(
            path = metadata.routePath,
            title = metadata.title,
            icon = metadata.icon,
            sort = metadata.order,
            enumSysMenuType = EnumSysMenuType.SCREEN
        )
    }

    /**
     * 🏷️ 根据路由键获取路由标题（兼容旧版本）
     * 使用缓存的映射表快速获取标题
     */
    fun getRouteTitleByKey(routeKey: String): String {
        return routeMetadataMap[routeKey]?.title ?: "未知页面"
    }


}
