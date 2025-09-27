package site.addzero.str

/**
 * 根据多个比较条件计算两个集合的差集。
 */
fun <T> Collection<T>.differenceBy(
    predicates: Array<(T, T) -> Boolean>,
    other: Collection<T>
): List<T> {
    return this.filter { item ->
        other.none { otherItem ->
            predicates.all { predicate -> predicate(item, otherItem) }
        }
    }
}

data class Person(val id: Int, val name: String, val age: Int)
