package com.addzero.kmp.util.data_structure.coll

/**
 * 自定义排序工具类
 *
 * 用于根据指定参考列表进行自定义排序，支持跨平台
 */
object CustomSort {
    /**
     * 根据指定的参考列表对输入列表进行排序
     *
     * @param inputList 要排序的输入列表
     * @param getSortField 从输入项目中提取排序字段的函数
     * @param referenceList 参考排序顺序列表
     * @param preserveUnmatched 是否保留不在参考列表中的项目（默认为true）
     * @return 排序后的列表
     */
    fun <T, S> customSort(
        inputList: List<T>,
        getSortField: (T) -> S,
        referenceList: List<S>,
        preserveUnmatched: Boolean = true
    ): List<T> {
        // 创建参考列表的索引映射，用于确定排序优先级
        val indexMap = createIndexMap(referenceList)

        // 创建自定义比较器
        val customComparator = Comparator { item1: T, item2: T ->
            val field1Value = getSortField(item1)
            val field2Value = getSortField(item2)

            // 获取排序值，如果不在参考列表中，则赋予最大值（排最后）
            val score1 = indexMap[field1Value] ?: Int.MAX_VALUE
            val score2 = indexMap[field2Value] ?: Int.MAX_VALUE

            // 比较排序值
            score1.compareTo(score2)
        }

        // 如果需要保留不在参考列表中的项目
        return if (preserveUnmatched) {
            inputList.sortedWith(customComparator)
        } else {
            // 只保留参考列表中存在的项目
            inputList.filter { indexMap.containsKey(getSortField(it)) }
                .sortedWith(customComparator)
        }
    }

    /**
     * 创建参考列表的索引映射
     *
     * @param referenceList 参考列表
     * @return 索引映射，键为参考列表中的项目，值为其索引
     */
    private fun <S> createIndexMap(referenceList: List<S>): Map<S, Int> {
        return referenceList.withIndex().associate { (index, item) -> item to index }
    }

    /**
     * 根据多个参考列表按优先级排序
     *
     * @param inputList 要排序的输入列表
     * @param sortFields 排序字段提取函数列表，顺序即为优先级
     * @param referenceLists 对应的参考列表列表
     * @param preserveUnmatched 是否保留不在参考列表中的项目（默认为true）
     * @return 排序后的列表
     */
    fun <T> multiFieldSort(
        inputList: List<T>,
        sortFields: List<(T) -> Any?>,
        referenceLists: List<List<Any?>>,
        preserveUnmatched: Boolean = true
    ): List<T> {
        require(sortFields.size == referenceLists.size) {
            "排序字段数量必须与参考列表数量相同"
        }

        // 创建所有参考列表的索引映射
        val indexMaps = referenceLists.map { createIndexMap(it) }

        // 创建多字段比较器
        val multiFieldComparator = Comparator<T> { item1, item2 ->
            // 依次比较每个字段
            for (i in sortFields.indices) {
                val field = sortFields[i]
                val indexMap = indexMaps[i]

                val value1 = field(item1)
                val value2 = field(item2)

                val score1 = indexMap[value1] ?: Int.MAX_VALUE
                val score2 = indexMap[value2] ?: Int.MAX_VALUE

                val comparison = score1.compareTo(score2)
                if (comparison != 0) {
                    return@Comparator comparison
                }
            }
            // 所有字段都相等
            0
        }

        // 执行排序
        return if (preserveUnmatched) {
            inputList.sortedWith(multiFieldComparator)
        } else {
            // 只保留所有参考列表中至少一个存在的项目
            inputList.filter { item ->
                sortFields.withIndex().any { (i, field) ->
                    indexMaps[i].containsKey(field(item))
                }
            }.sortedWith(multiFieldComparator)
        }
    }
}
