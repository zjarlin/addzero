package com.addzero.web.infra.jimmer.base.pagefactory

import com.addzero.kmp.entity.PageResult
import org.babyfish.jimmer.sql.ast.impl.query.PageSource
import org.babyfish.jimmer.sql.ast.query.PageFactory


fun <E, P> createPageFactory(block: (List<E>, Long, PageSource) -> P): PageFactory<E, P> {
    return PageFactory {
            rows,
            totalCount, source,
        ->
        val pageIndex = source.pageIndex
        val limit = source.pageSize
        val totalPageCount = if (limit == Int.MAX_VALUE) 1 else (totalCount + limit - 1) / limit
        block(rows, totalCount, source)
    }
}


/**
 * 通用分页工厂函数，生成分页结果
 * @param block 处理分页数据的自定义逻辑
 */
fun <E> createPageFactory(): PageFactory<E, PageResult<E>> {
    return PageFactory { rows, totalCount, source ->
        val pageIndex = source.pageIndex
        val pageSize = source.pageSize
        val totalPages = if (pageSize == Int.MAX_VALUE) 1 else (totalCount + pageSize - 1) / pageSize

        // 构建分页结果对象
        PageResult(
            content = rows,
            totalElements = totalCount,
            totalPages = totalPages.toInt(),
            pageIndex = pageIndex + 1, // 页码从 1 开始
            pageSize = pageSize,
            isFirst = pageIndex == 0,
            isLast = pageIndex + 1 >= totalPages
        )
    }
}
