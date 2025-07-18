package com.addzero.kmp.ui.infra

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.ui.infra.model.favorite.FavoriteTab
import com.addzero.kmp.ui.infra.model.favorite.FavoriteTabsViewModel
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import com.addzero.kmp.compose.icons.IconMap
import org.koin.compose.viewmodel.koinViewModel

/**
 * 常用标签页栏组件
 * 显示在顶部栏中的常用页面快捷访问
 */
@Composable
fun FavoriteTabsBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val favoriteViewModel = koinViewModel<FavoriteTabsViewModel>()
    val favoriteTabs = favoriteViewModel.favoriteTabs
    val currentRoute = MenuViewModel.currentRoute
    
    if (favoriteTabs.isEmpty() && !favoriteViewModel.isLoading) {
        // 如果没有常用标签页，显示应用名称
        Text(
            text = "Addzero",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        return
    }

    // 直接显示内容，不使用Surface包装（因为已经在TopAppBar中）
    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 标签页列表（可水平滚动）
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            favoriteTabs.forEach { tab ->
                FavoriteTabItem(
                    tab = tab,
                    isActive = currentRoute == tab.routeKey,
                    onClick = {
                        MenuViewModel.updateRoute(tab.routeKey)
                    },
                    onRemove = {
                        favoriteViewModel.removeFromFavorites(tab.routeKey)
                    }
                )
            }
        }

        // 添加当前页面到常用标签页按钮
        if (!favoriteViewModel.isFavorite(currentRoute)) {
            Spacer(modifier = Modifier.width(8.dp))
            AddIconButton(
                imageVector = Icons.Default.StarBorder,
                text = "添加到常用",
                onClick = {
                    favoriteViewModel.addToFavorites(currentRoute)
                },
                modifier = Modifier.size(28.dp)
            )
        }
    }
    
    // 显示错误信息
    favoriteViewModel.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // TODO: 显示错误提示
            println("常用标签页错误: $error")
        }
    }
}

/**
 * 常用标签页项
 */
@Composable
private fun FavoriteTabItem(
    tab: FavoriteTab,
    isActive: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isActive) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    val contentColor = if (isActive) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    
    var showRemoveButton by remember { mutableStateOf(false) }
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp)),
        color = backgroundColor,
        tonalElevation = if (isActive) 2.dp else 0.dp,
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 图标
            if (tab.icon.isNotBlank()) {
                val iconData = IconMap[tab.icon]
                Icon(
                    imageVector = iconData.vector,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            // 标题
            Text(
                text = tab.title,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // 移除按钮（仅在激活状态或鼠标悬停时显示）
            if (isActive) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "移除",
                        tint = contentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}


