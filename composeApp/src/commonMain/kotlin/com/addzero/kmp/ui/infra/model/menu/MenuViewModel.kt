package com.addzero.kmp.ui.infra.model.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.di.NavgationService
import com.addzero.kmp.entity.sys.menu.EnumSysMenuType
import com.addzero.kmp.entity.sys.menu.SysMenuVO
import com.addzero.kmp.generated.RouteKeys
import com.addzero.kmp.generated.RouteTable
import com.addzero.kmp.util.data_structure.tree.List2TreeUtil
import org.koin.android.annotation.KoinViewModel

object MenuViewModel {


    fun updateRoute(string: String) {
        currentRoute = string
        NavgationService.navigate(currentRoute)
    }

    //默认展开
    var isExpand by mutableStateOf(true)
    var currentRoute by mutableStateOf(RouteKeys.HOME_SCREEN)
    var keyword by mutableStateOf("")
        private set

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
        var allMeta = RouteTable.allMeta


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

    fun getRouteByKey(string: String): String {
        val associate = RouteTable.allMeta.associate { it.routePath to it }
        val route = associate[string] ?: throw RuntimeException("Route not found: $string")
        return route.title
    }


}
