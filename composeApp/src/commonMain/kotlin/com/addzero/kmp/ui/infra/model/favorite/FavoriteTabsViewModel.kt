package com.addzero.kmp.ui.infra.model.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.entity.sys.menu.SysMenuVO
import com.addzero.kmp.core.network.AddHttpClient
import com.addzero.kmp.generated.api.ApiProvider.favoriteTabsApi
import com.addzero.kmp.generated.api.FavoriteTabsApi
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * 常用标签页ViewModel
 * 管理用户的常用路由标签页
 */
@KoinViewModel
class FavoriteTabsViewModel : ViewModel() {

    // HTTP客户端
    private val httpClient = AddHttpClient.httpclient

    // 常用标签页列表
    var favoriteTabs by mutableStateOf<List<FavoriteTab>>(emptyList())
        private set

    // 加载状态
    var isLoading by mutableStateOf(false)
        private set

    // 错误信息
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadFavoriteTabs()
    }
    
    /**
     * 从后台加载常用标签页
     */
    fun loadFavoriteTabs() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                // 调用后台API获取常用路由键
                var favoriteRouteKeys = favoriteTabsApi.getFavoriteRoutes()

                // 将路由键转换为标签页对象
                val tabs = favoriteRouteKeys.mapNotNull { routeKey ->
                    val menu = MenuViewModel.getRouteByKey(routeKey)
                    menu?.let { 
                        FavoriteTab(
                            routeKey = routeKey,
                            title = it.title,
                            icon = it.icon,
                            order = it.sort.toInt()
                        )
                    }
                }.sortedBy { it.order }
                
                favoriteTabs = tabs
                
            } catch (e: Exception) {
                errorMessage = "加载常用标签页失败: ${e.message}"
                // 使用默认的常用标签页
                loadDefaultFavoriteTabs()
            } finally {
                isLoading = false
            }
        }
    }
    
    /**
     * 添加到常用标签页
     */
    fun addToFavorites(routeKey: String) {
        viewModelScope.launch {
            try {
                val menu = MenuViewModel.getRouteByKey(routeKey)
                if (menu != null && !favoriteTabs.any { it.routeKey == routeKey }) {
                    val newTab = FavoriteTab(
                        routeKey = routeKey,
                        title = menu.title,
                        icon = menu.icon,
                        order = favoriteTabs.size
                    )
                    
                    favoriteTabs = favoriteTabs + newTab
                    
                    // 同步到后台
                    httpClient.post("/sysMenu/addFavoriteRoute") {
                        contentType(ContentType.Application.Json)
                        setBody(mapOf("routeKey" to routeKey))
                    }
                }
            } catch (e: Exception) {
                errorMessage = "添加常用标签页失败: ${e.message}"
            }
        }
    }
    
    /**
     * 从常用标签页移除
     */
    fun removeFromFavorites(routeKey: String) {
        viewModelScope.launch {
            try {
                favoriteTabs = favoriteTabs.filter { it.routeKey != routeKey }
                
                // 同步到后台
                httpClient.delete("/sysMenu/removeFavoriteRoute") {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("routeKey" to routeKey))
                }
                
            } catch (e: Exception) {
                errorMessage = "移除常用标签页失败: ${e.message}"
            }
        }
    }
    
    /**
     * 重新排序常用标签页
     */
    fun reorderFavoriteTabs(newOrder: List<FavoriteTab>) {
        viewModelScope.launch {
            try {
                val reorderedTabs = newOrder.mapIndexed { index, tab ->
                    tab.copy(order = index)
                }
                favoriteTabs = reorderedTabs
                
                // 同步到后台
                val routeKeys = reorderedTabs.map { it.routeKey }
                httpClient.post("/sysMenu/updateFavoriteRoutesOrder") {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("routeKeys" to routeKeys))
                }
                
            } catch (e: Exception) {
                errorMessage = "重新排序失败: ${e.message}"
            }
        }
    }
    
    /**
     * 检查路由是否在常用标签页中
     */
    fun isFavorite(routeKey: String): Boolean {
        return favoriteTabs.any { it.routeKey == routeKey }
    }
    
    /**
     * 加载默认的常用标签页（当API调用失败时使用）
     */
    private fun loadDefaultFavoriteTabs() {
        val defaultRouteKeys = listOf(
            "/home",
            "/system/role", 
            "/system/user",
            "/system/dict"
        )
        
        val tabs = defaultRouteKeys.mapIndexedNotNull { index, routeKey ->
            val menu = MenuViewModel.getRouteByKey(routeKey)
            menu?.let { 
                FavoriteTab(
                    routeKey = routeKey,
                    title = it.title,
                    icon = it.icon,
                    order = index
                )
            }
        }
        
        favoriteTabs = tabs
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        errorMessage = null
    }
}

/**
 * 常用标签页数据类
 */
data class FavoriteTab(
    val routeKey: String,
    val title: String,
    val icon: String,
    val order: Int
)
