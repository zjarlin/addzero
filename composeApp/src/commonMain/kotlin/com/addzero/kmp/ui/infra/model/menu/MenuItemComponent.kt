//package com.addzero.kmp.ui.infra.menu
//
//import androidx.compose.foundation.hoverable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsHoveredAsState
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ExpandLess
//import androidx.compose.material.icons.filled.ExpandMore
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getColumnValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Popup
//import androidx.navigation.NavController
//import com.addzero.kmp.ui.infra.model.menu.MenuNode
//import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
//
///**
// * 菜单项组件
// *
// * @param item 菜单节点
// * @param navController 导航控制器
// * @param navHostInitialized 导航图是否已初始化
// * @param menuViewModel 菜单视图模型
// */
//@Composable
//fun MenuItemComponent(
//    item: MenuNode,
//    navController: NavController,
//    navHostInitialized: Boolean,
//    menuViewModel: MenuViewModel
//) {
//    // 仅对非叶子节点维护折叠状态
//    var isExpanded by remember { mutableStateOf(true) }
//
//    // 创建交互源以检测悬浮
//    val interactionSource = remember { MutableInteractionSource() }
//    // 获取悬浮状态
//    val isHovered by interactionSource.collectIsHoveredAsState()
//
//    Column {
//        Box {
//            // 悬浮提示 - 当菜单折叠且鼠标悬浮时显示
//            if (!menuViewModel.isExpanded && isHovered) {
//                MenuTooltip(item.title)
//            }
//
//            MenuButton(
//                item = item,
//                isExpanded = isExpanded,
//                interactionSource = interactionSource,
//                menuViewModel = menuViewModel,
//                navController = navController,
//                navHostInitialized = navHostInitialized,
//                onExpandToggle = { isExpanded = !isExpanded }
//            )
//        }
//
//        // 渲染子菜单，仅当父菜单展开时
//        if (!item.leafFlag && isExpanded) {
//            SubMenuItems(
//                children = item.children,
//                navController = navController,
//                navHostInitialized = navHostInitialized,
//                menuViewModel = menuViewModel
//            )
//        }
//
//        Spacer(modifier = Modifier.height(4.dp))
//    }
//}
//
///**
// * 菜单按钮组件
// */
//@Composable
//private fun MenuButton(
//    item: MenuNode,
//    isExpanded: Boolean,
//    interactionSource: MutableInteractionSource,
//    menuViewModel: MenuViewModel,
//    navController: NavController,
//    navHostInitialized: Boolean,
//    onExpandToggle: () -> Unit
//) {
//    TextButton(
//        onClick = {
//            if (item.leafFlag) {
//                // 叶子节点 - 执行导航
//                menuViewModel.updateRoute(item.path)
//                // 使用NavController导航，仅当导航图已初始化时
//                if (navHostInitialized) {
//                    try {
//                        navController.navigate(item.path) {
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    } catch (e: Exception) {
//                        println("菜单导航异常: ${e.message}")
//                    }
//                }
//            } else {
//                // 父节点 - 切换展开/折叠状态
//                onExpandToggle()
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .hoverable(interactionSource), // 使用hoverable修饰符检测悬浮
//        interactionSource = interactionSource,
//        colors = ButtonDefaults.textButtonColors(
//            containerColor = if (item.path == menuViewModel.currentRoute)
//                MaterialTheme.colorScheme.primaryContainer
//            else
//                MaterialTheme.colorScheme.surface
//        )
//    ) {
//        MenuButtonContent(
//            item = item,
//            isExpanded = isExpanded,
//            menuViewModel = menuViewModel
//        )
//    }
//}
//
///**
// * 菜单按钮内容
// */
//@Composable
//private fun MenuButtonContent(
//    item: MenuNode,
//    isExpanded: Boolean,
//    menuViewModel: MenuViewModel
//) {
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // 显示菜单图标
//            Icon(
//                imageVector = item.icon ?: Icons.Default.ExpandMore,
//                contentDescription = item.title, // 添加内容描述，便于访问性
//                tint = if (item.path == menuViewModel.currentRoute)
//                    MaterialTheme.colorScheme.primary
//                else
//                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
//                // 设置图标大小，当菜单收起时使用较大尺寸
//                modifier = Modifier.size(if (!menuViewModel.isExpanded) 24.dp else 20.dp)
//            )
//
//            // 只在展开状态下显示文本
//            if (menuViewModel.isExpanded) {
//                Text(
//                    text = item.title,
//                    style = MaterialTheme.typography.bodyLarge,
//                    maxLines = Int.MAX_VALUE
//                )
//            }
//        }
//
//        // 为非叶子节点显示展开/折叠图标
//        if (!item.leafFlag && menuViewModel.isExpanded) {
//            Icon(
//                imageVector = if (isExpanded)
//                    Icons.Default.ExpandLess else
//                    Icons.Default.ExpandMore,
//                contentDescription = if (isExpanded) "折叠" else "展开"
//            )
//        }
//    }
//}
//
///**
// * 菜单悬浮提示
// */
//@Composable
//private fun MenuTooltip(title: String) {
//    Popup(
//        alignment = Alignment.CenterEnd,
//        offset = IntOffset(8, 0)
//    ) {
//        Surface(
//            color = MaterialTheme.colorScheme.surfaceVariant,
//            shape = MaterialTheme.shapes.small,
//            shadowElevation = 4.dp
//        ) {
//            Text(
//                text = title,
//                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}
//
///**
// * 子菜单项组件
// */
//@Composable
//private fun SubMenuItems(
//    children: List<MenuNode>,
//    navController: NavController,
//    navHostInitialized: Boolean,
//    menuViewModel: MenuViewModel
//) {
//    Column(modifier = Modifier.padding(start = 16.dp)) {
//        children.forEach { child ->
//            MenuItemComponent(child, navController, navHostInitialized, menuViewModel)
//        }
//    }
//}
