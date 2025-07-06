package com.addzero.kmp.mock

import com.addzero.kmp.jdbc.meta.public.table.impl.ISysDeptImpl

// 示例部门数据（使用 Long 类型的 id 和 parentId）
val mockSysDepts = listOf(
    ISysDeptImpl(
        id = 1L,
        name = "总公司",
        parentId = null  // 根节点 parentId 为 null
    ),
    ISysDeptImpl(
        id = 2L,
        name = "研发中心",
        parentId = 1L
    ),
    ISysDeptImpl(
        id = 3L,
        name = "产品部",
        parentId = 2L
    ),
    ISysDeptImpl(
        id = 4L,
        name = "开发部",
        parentId = 2L
    ),
    ISysDeptImpl(
        id = 5L,
        name = "测试部",
        parentId = 2L
    ),
    ISysDeptImpl(
        id = 6L,
        name = "市场中心",
        parentId = 1L
    ),
    ISysDeptImpl(
        id = 7L,
        name = "销售部",
        parentId = 6L
    ),
    ISysDeptImpl(
        id = 8L,
        name = "财务部",
        parentId = 1L
    ),
    ISysDeptImpl(
        id = 9L,
        name = "人力资源部",
        parentId = 1L
    )
)
