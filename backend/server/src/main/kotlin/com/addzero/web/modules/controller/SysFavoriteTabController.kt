package com.addzero.web.modules.controller

import com.addzero.common.consts.sql
import com.addzero.model.entity.SysFavoriteTab
import com.addzero.model.entity.routeKey
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.desc
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysFavoriteTab")
class SysFavoriteTabController {

    /**
     * 获取最常用的Top5路由
     */
    @GetMapping("/topFavoriteRoutes")
    fun topFavoriteRoutes(top: Int = 5): List<String> {
        val execute = sql.executeQuery(SysFavoriteTab::class, top) {
            groupBy(table.routeKey)
            orderBy(count(table.routeKey).desc())
            select(
                table.routeKey,
                count(table.routeKey)
            )
        }
        val map = execute.map {
            it._1
        }
        return map
    }

    @PostMapping("/add")
    fun add(@RequestBody sysFavoriteTab: SysFavoriteTab): Boolean {
        val save = sql.save(sysFavoriteTab) {
            setMode(SaveMode.INSERT_ONLY)
        }
        val rowAffected = save.isRowAffected
        return rowAffected

    }
}


