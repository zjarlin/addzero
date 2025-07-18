package com.addzero.web.modules.controller

import org.springframework.web.bind.annotation.*

/**
 * 常用标签页控制器
 * 管理用户的常用路由标签页
 */
@RestController
@RequestMapping("/sysMenu")
class FavoriteTabsController {
    
    /**
     * 获取用户常用路由键列表
     */
    @GetMapping("/getFavoriteRoutes")
    fun getFavoriteRoutes(): List<String> {
        return listOf(
            "/home",
            "/system/role", 
            "/system/user",
            "/system/dict",
            "/demo/table-example"
        )
    }
    
    /**
     * 添加常用路由
     */
    @PostMapping("/addFavoriteRoute")
    fun addFavoriteRoute(@RequestBody request: Map<String, String>): Boolean {
        val routeKey = request["routeKey"] ?: return false
        
        // TODO: 保存到数据库
        println("添加常用路由: $routeKey")
        
        return true
    }
    
    /**
     * 移除常用路由
     */
    @DeleteMapping("/removeFavoriteRoute")
    fun removeFavoriteRoute(@RequestBody request: Map<String, String>): Boolean {
        val routeKey = request["routeKey"] ?: return false
        
        // TODO: 从数据库删除
        println("移除常用路由: $routeKey")
        
        return true
    }
    
    /**
     * 更新常用路由顺序
     */
    @PostMapping("/updateFavoriteRoutesOrder")
    fun updateFavoriteRoutesOrder(@RequestBody request: Map<String, List<String>>): Boolean {
        val routeKeys = request["routeKeys"] ?: return false
        
        // TODO: 更新数据库中的顺序
        println("更新常用路由顺序: $routeKeys")
        
        return true
    }
}
