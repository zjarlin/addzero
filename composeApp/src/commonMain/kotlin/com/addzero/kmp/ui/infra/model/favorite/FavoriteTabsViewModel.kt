package com.addzero.kmp.ui.infra.model.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.core.network.AddHttpClient
import com.addzero.kmp.generated.RouteKeys
import com.addzero.kmp.generated.RouteTable
import com.addzero.kmp.generated.api.ApiProvider.sysFavoriteTabApi
import com.addzero.kmp.generated.isomorphic.SysFavoriteTabIso
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
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
    var favoriteTabs by mutableStateOf<List<SysFavoriteTabIso>>(emptyList())
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
                var favoriteRouteKeys = sysFavoriteTabApi.topFavoriteRoutes(5)

                var associateBy = RouteTable.allMeta.associateBy { it.routePath }
                // 将路由键转换为标签页对象
                val tabs = favoriteRouteKeys.mapNotNull { routeKey ->
                    var route = associateBy[routeKey]
                    val menu = MenuViewModel.getRouteByKey(routeKey)
                    menu?.let {
                        SysFavoriteTabIso(
                            routeKey = routeKey,
                        )
                    }
                }
                
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
                    val newTab = SysFavoriteTabIso(
                        routeKey = routeKey,
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
            RouteKeys.DICT_MANAGER_SCREEN,
            RouteKeys.SYS_DEPT_SCREEN,
        )
        
        val tabs = defaultRouteKeys.mapIndexedNotNull { index, routeKey ->
            val menu = MenuViewModel.getRouteByKey(routeKey)
            menu?.let {
                SysFavoriteTabIso(
                    routeKey = routeKey,
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
@Deprecated("daosidj")
data class FavoriteTab(
    val routeKey: String,
    val title: String,
    val icon: String,
    val order: Int
)
