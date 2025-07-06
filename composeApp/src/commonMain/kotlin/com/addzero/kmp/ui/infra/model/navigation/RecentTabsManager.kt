package com.addzero.kmp.ui.infra.model.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.koin.android.annotation.KoinViewModel

/**
 * 最近访问标签页管理器
 *
 * 用于管理用户最近访问的页面，以标签页形式展示
 */
@KoinViewModel
class RecentTabsManager: ViewModel() {
    private  val MAX_TABS = 10 // 最大保存的标签页数量
    private  val MAX_CLOSED_TABS = 20 // 最大保存的已关闭标签页数量
    
    // 保存所有标签页
    private val _tabs = mutableStateListOf<TabInfo>()
    // 当前激活的标签页索引
    private var _currentTabIndex by mutableStateOf(-1)
    // 保存最近关闭的标签页，用于恢复功能
    private val _closedTabs = mutableStateListOf<TabInfo>()
    
    // 供外部访问的视图
    val tabs: List<TabInfo> get() = _tabs
    val currentTabIndex: Int get() = _currentTabIndex
    val currentTab: TabInfo? get() = if (_currentTabIndex >= 0 && _currentTabIndex < _tabs.size) _tabs[_currentTabIndex] else null
    
    /**
     * 添加或激活一个标签页
     *
     * @param route 路由路径
     * @param title 标签页标题
     */
    fun addOrActivateTab(route: String, title: String) {
        // 查找是否已存在此路由的标签
        val existingIndex = _tabs.indexOfFirst { it.route == route }
        
        if (existingIndex >= 0) {
            // 如果已存在，将其激活
            _currentTabIndex = existingIndex
            return
        }
        
        // 如果达到最大标签数，移除最早添加的一个
        if (_tabs.size >= MAX_TABS) {
            _tabs.removeAt(0)
        }
        
        // 添加新标签
        val newTab = TabInfo(route, title)
        _tabs.add(newTab)
        _currentTabIndex = _tabs.size - 1
    }
    
    /**
     * 激活指定索引的标签页
     *
     * @param index 标签页索引
     * @param navController 导航控制器
     */
    fun activateTab(index: Int, navController: NavController) {
        if (index >= 0 && index < _tabs.size) {
            _currentTabIndex = index
            navController.navigate(_tabs[index].route) {
                // 避免创建多个实例
                launchSingleTop = true
                // 恢复状态
                restoreState = true
            }
        }
    }
    
    /**
     * 关闭指定索引的标签页
     *
     * @param index 标签页索引
     * @param navController 导航控制器
     */
    fun closeTab(index: Int, navController: NavController) {
        if (index >= 0 && index < _tabs.size) {
            // 保存要关闭的标签页到关闭历史
            val closedTab = _tabs[index]
            addToClosedTabs(closedTab)
            
            // 记录当前激活的标签
            val currentTab = currentTab
            
            // 移除标签
            _tabs.removeAt(index)
            
            // 更新当前标签索引
            when {
                _tabs.isEmpty() -> _currentTabIndex = -1
                index < _currentTabIndex -> _currentTabIndex--
                index == _currentTabIndex -> {
                    // 如果关闭的是当前标签，切换到最后一个标签
                    _currentTabIndex = _tabs.size - 1
                    if (_currentTabIndex >= 0) {
                        navController.navigate(_tabs[_currentTabIndex].route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 将标签页添加到已关闭标签页列表
     * 
     * @param tab 要添加的标签页
     */
    private fun addToClosedTabs(tab: TabInfo) {
        // 避免重复添加
        if (_closedTabs.contains(tab)) {
            _closedTabs.remove(tab)
        }
        
        // 添加到最近关闭的标签列表
        _closedTabs.add(0, tab)
        
        // 如果超出最大保存数量，移除最早关闭的
        if (_closedTabs.size > MAX_CLOSED_TABS) {
            _closedTabs.removeAt(_closedTabs.size - 1)
        }
    }
    
    /**
     * 重新打开最近关闭的标签页
     * 
     * @param navController 导航控制器
     * @return 是否成功重新打开标签页
     */
    fun reopenLastClosedTab(navController: NavController): Boolean {
        if (_closedTabs.isEmpty()) {
            return false
        }
        
        // 获取最近关闭的标签页
        val tabToReopen = _closedTabs.removeAt(0)
        
        // 检查此路由是否已在打开的标签中
        val existingIndex = _tabs.indexOfFirst { it.route == tabToReopen.route }
        if (existingIndex >= 0) {
            // 如果已存在，直接激活
            activateTab(existingIndex, navController)
            return true
        }
        
        // 添加标签并导航
        _tabs.add(tabToReopen)
        _currentTabIndex = _tabs.size - 1
        navController.navigate(tabToReopen.route) {
            launchSingleTop = true
            restoreState = true
        }
        
        return true
    }
    
    /**
     * 关闭所有标签页
     */
    fun closeAllTabs() {
        // 将所有标签添加到关闭历史
        _tabs.forEach { addToClosedTabs(it) }
        
        _tabs.clear()
        _currentTabIndex = -1
    }

    /**
     * 清理所有标签页和已关闭标签页的状态
     */
    fun clear() {
        _tabs.clear()
        _currentTabIndex = -1
        _closedTabs.clear()
    }
}

/**
 * 标签页信息
 *
 * @property route 路由路径
 * @property title 标签页标题
 */
data class TabInfo(
    val route: String,
    val title: String
)