package com.addzero.kmp.mock

import com.addzero.kmp.jdbc.meta.public.table.impl.ISysDictImpl
import com.addzero.kmp.jdbc.meta.public.table.impl.ISysDictItemImpl

/**
 * 创建模拟字典类型数据
 */
fun createMockDictTypes(): List<ISysDictImpl> {
    return listOf(
        ISysDictImpl(
            id = 1L,
            dictName = "用户状态",
            dictCode = "sys_user_status",
            description = "用户状态字典"
        ),
        ISysDictImpl(
            id = 2L,
            dictName = "系统开关",
            dictCode = "sys_toggle",
            description = "系统通用开关状态"
        ),
        ISysDictImpl(
            id = 3L,
            dictName = "性别",
            dictCode = "sys_gender",
            description = "用户性别"
        )
    )
}

/**
 * 创建模拟字典项数据
 */
fun createMockDictItems(): List<ISysDictItemImpl> {
    return listOf(
        // 用户状态字典项
        ISysDictItemImpl(
            id = 101L,
            dictId = 1L,
            itemText = "正常",
            itemValue = "1",
            description = "正常状态",
            sortOrder = 1L,
            status = 1L
        ),
        ISysDictItemImpl(
            id = 102L,
            dictId = 1L,
            itemText = "停用",
            itemValue = "0",
            description = "停用状态",
            sortOrder = 2L,
            status = 0L
        ),

        // 系统开关字典项
        ISysDictItemImpl(
            id = 201L,
            dictId = 2L,
            itemText = "开启",
            itemValue = "1",
            description = "开启状态",
            sortOrder = 1L,
            status = 1L
        ),
        ISysDictItemImpl(
            id = 202L,
            dictId = 2L,
            itemText = "关闭",
            itemValue = "0",
            description = "关闭状态",
            sortOrder = 2L,
            status = 1L
        ),

        // 性别字典项
        ISysDictItemImpl(
            id = 301L,
            dictId = 3L,
            itemText = "男",
            itemValue = "1",
            description = "男性",
            sortOrder = 1L,
            status = 1L
        ),
        ISysDictItemImpl(
            id = 302L,
            dictId = 3L,
            itemText = "女",
            itemValue = "2",
            description = "女性",
            sortOrder = 2L,
            status = 1L
        ),
        ISysDictItemImpl(
            id = 303L,
            dictId = 3L,
            itemText = "未知",
            itemValue = "0",
            description = "未知性别",
            sortOrder = 3L,
            status = 1L
        )
    )
}
