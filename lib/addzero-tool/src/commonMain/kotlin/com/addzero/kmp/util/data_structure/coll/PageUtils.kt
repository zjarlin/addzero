package com.addzero.kmp.util.data_structure.coll

import kotlin.math.max

object PageUtils {
    /**
     * 将列表转换为分页对象
     *
     * @param list 原始列表
     * @param pageNo 当前页码（从1开始）
     * @param pageSize 每页大小
     * @param setPageNoFun 设置页码的函数
     * @param setRowFun 设置记录列表的函数
     * @param setTotalFun 设置总记录数的函数
     * @param createPageFun 创建分页对象的函数
     * @return 分页对象
     */
    fun <T, Page> list2Page(
        list: List<T>,
        pageNo: Int,
        pageSize: Int,
        setPageNoFun: (Page, Int) -> Unit,
        setRowFun: (Page, List<T>) -> Unit,
        setTotalFun: (Page, Int) -> Unit,
        createPageFun: () -> Page
    ): Page {
        // 计算起始索引
        val startIndex = ((max(1.0, pageNo.toDouble()) - 1) * pageSize).toInt()
        // 计算结束索引
        val endIndex = minOf(startIndex + pageSize, list.size)

        // 获取分页数据
        val data = list.subList(startIndex, endIndex)

        // 计算总页数
        val totalRecords = list.size
        val totalPages = (totalRecords + pageSize - 1) / pageSize

        // 创建分页对象
        val page = createPageFun()

        // 设置分页信息
        setPageNoFun(page, pageNo)
        setRowFun(page, data)
        setTotalFun(page, totalRecords)

        return page
    }

}


