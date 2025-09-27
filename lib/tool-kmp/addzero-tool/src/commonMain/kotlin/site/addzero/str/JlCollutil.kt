package site.addzero.str


//JlCollutil

// infix fun<T> MutableCollection<T>.`+?`(defaultExcludeFields:
//                                        MutableCollection<T>):
//         List<T> {
//    defaultExcludeFields.forEach {
//        CollUtil.addIfAbsent(this,it)
//    }
//    return this.filter { it.isNotNull() }
//}

fun <T, K> List<T>.addAllIfAbsentByKey(other: Collection<T>, keySelector: (T) -> K): Boolean? {
    var modified = false
    other.forEach { element ->
        if (none { keySelector(it) == keySelector(element) }) {
            this.add(element)
            modified = true
        }
    }
    return modified
}

fun <E> List<E>.removeAt(i: Int) {
    this.toMutableList().removeAt(i)
}

fun <T> List<T>.add(currentNode: T) {
    this.toMutableList().add(currentNode)

}

fun <E> List<E>.addAll(allLeafNodes: List<E>) {
    this.toMutableList().addAll(allLeafNodes)
}

fun <E> List<E>.removeIf(predicate: (E) -> Boolean) {
    val toMutableList = this.toMutableList()
    toMutableList.removeIf(predicate)
}

/**
 * 根据多个比较条件计算两个集合的差集。
 *
 * @param T 集合元素的类型
 * @param other 另一个集合
 * @param predicates 多个 lambda 表达式，用于自定义比较规则
 * @return 差集（当前集合中不存在于 `other` 集合中的元素）
 */
fun <T> Collection<T>.differenceBy(
    other: Collection<T>,
    vararg predicates: (T, T) -> Boolean,
): List<T> {
    return this.filter { item ->
        other.none { otherItem ->
            predicates.all { predicate -> predicate(item, otherItem) }
        }
    }
}

/**
 * 根据多个比较条件计算两个集合的交集。
 *
 * @param T 集合元素的类型
 * @param other 另一个集合
 * @param predicates 多个 lambda 表达式，用于自定义比较规则
 * @return 交集（同时存在于两个集合中的元素）
 */
fun <T> Collection<T>.intersectBy(
    other: Collection<T>,
    vararg predicates: (T, T) -> Boolean,
): List<T> {
    return this.filter { item ->
        other.any { otherItem ->
            predicates.all { predicate -> predicate(item, otherItem) }
        }
    }
}



