package site.addzero.ui.infra

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import site.addzero.component.search.GlobalSearchButtonWithShortcut
import site.addzero.component.search.GlobalSearchDialog
import site.addzero.component.search.SimpleSearchResult
import site.addzero.di.NavgationViewModel
import site.addzero.entity.sys.menu.SysMenuVO
import site.addzero.viewmodel.SysRouteViewModel

/**
 * 系统路由搜索栏组件
 *
 * 这是一个轻量级的业务组件，使用组件库中的搜索组件
 *
 * @param modifier 修饰符
 * @param isSearchOpen 搜索框是否打开的外部状态，如果为null则使用内部状态
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
context(sysRouteViewModel: SysRouteViewModel, navgationViewModel: NavgationViewModel)
fun AddSysRouteSearchBar(
    modifier: Modifier = Modifier,
    isSearchOpen: MutableState<Boolean>? = null
) {
    val navController = navgationViewModel.getNavController()
    val internalSearchOpen = remember { mutableStateOf(false) }
    val searchOpenState = isSearchOpen ?: internalSearchOpen

    // 获取所有菜单项
    val menuItems = remember(sysRouteViewModel) {
        flattenMenuItems(sysRouteViewModel.menuItems)
    }

    // 打开搜索弹窗
    val openSearch = {
        searchOpenState.value = true
    }

    // 关闭搜索弹窗
    val closeSearch = {
        searchOpenState.value = false
    }

    // 全局键盘事件监听
    Box(
        modifier = modifier.onPreviewKeyEvent { keyEvent ->
            when {
                // Cmd+K：打开搜索框
                (keyEvent.key == Key.K && keyEvent.isMetaPressed &&
                        keyEvent.type == KeyEventType.KeyDown) -> {
                    openSearch()
                    true
                }
                else -> false
            }
        }
    ) {
        // 搜索按钮
        GlobalSearchButtonWithShortcut(
            onClick = openSearch,
            onKeyPressed = { keyEvent ->
                // 处理其他快捷键逻辑
                false
            }
        )

        // 搜索弹窗
        GlobalSearchDialog(
            isVisible = searchOpenState.value,
            onDismiss = closeSearch,
            searchItems = menuItems,
            renderFunction = { menuItem ->
                SimpleSearchResult(
                    data = menuItem,
                    title = menuItem.title,
                    subtitle = menuItem.parentPath?.let { parentPath ->
                        menuItems.find { it.path == parentPath }?.title
                    },
                    icon = menuItem.icon.takeIf { it.isNotEmpty() }
                )
            },
            onResultClick = { menuItem ->
                // 根据结果ID导航到对应菜单
                navigateToMenuItem(menuItem.path, navController)
                closeSearch()
            },
            placeholder = "搜索菜单 (Cmd+K)"
        )
    }
}

/**
 * 递归展平菜单项，转换为列表
 */
private fun flattenMenuItems(menuItems: List<SysMenuVO>): List<SysMenuVO> {
    val result = mutableListOf<SysMenuVO>()

    fun traverse(items: List<SysMenuVO>) {
        for (item in items) {
            // 排除没有路径的菜单项
            if (item.path.isNotBlank()) {
                result.add(item)
            }

            if (item.children.isNotEmpty()) {
                traverse(item.children)
            }
        }
    }

    traverse(menuItems)
    return result.distinct()
}

/**
 * 根据菜单项ID导航到指定菜单
 */
private fun navigateToMenuItem(menuItemId: String, navController: NavController) {
    navController.navigate(menuItemId) {
        launchSingleTop = true
        restoreState = true
    }
}