package com.addzero.kmp.component.tree_command

/**
 * 树命令类型枚举
 */
enum class TreeCommand {
    SEARCH,          // 搜索功能
    MULTI_SELECT,    // 多选功能
    EXPAND_ALL,      // 全部展开
    COLLAPSE_ALL,    // 全部折叠
    REFRESH,         // 刷新树
    FILTER,          // 过滤节点
    SORT,            // 排序节点
    ADD_NODE,        // 添加节点
    EDIT_NODE,       // 编辑节点
    DELETE_NODE,     // 删除节点
    DRAG_DROP,       // 拖拽排序
    EXPORT,          // 导出数据
    IMPORT           // 导入数据
}