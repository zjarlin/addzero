package com.addzero.common.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.function.Function
import java.util.function.Predicate

/**
 * @author addzero
 * @since 2022/10/1 9:15 AM
 */
object BigDecimals {
    /**
     * List一列求和
     *
     * @param collection 集合
     * @param getFun     得到乐趣
     * @return 返回信息
     * @author zjarlin
     * @since 2022/06/29
     */
    fun <E> sum(collection: Collection<E>, getFun: Function<E, BigDecimal>): BigDecimal {
        return collection.stream().filter { obj: E -> Objects.nonNull(obj) }
            .filter { c: E -> Objects.nonNull(getFun.apply(c)) }.map(getFun)
            .reduce(BigDecimal.ZERO) { obj: BigDecimal, augend: BigDecimal? -> obj.add(augend) }
    }

    /**
     * List一列乘积
     *
     * @param collection 集合
     * @param getFun     得到乐趣
     * @return 返回信息
     * @author zjarlin
     * @since 2022/06/29
     */
    fun <E> multiply(collection: Collection<E>, getFun: Function<E, BigDecimal>): BigDecimal {
        return collection.stream().filter { obj: E -> Objects.nonNull(obj) }
            .filter { c: E -> Objects.nonNull(getFun.apply(c)) }.map(getFun)
            .reduce(BigDecimal.ONE) { obj: BigDecimal, multiplicand: BigDecimal? -> obj.multiply(multiplicand) }
    }


    /**
     * sumproduct
     *
     * @param collection   集合
     * @param condition    条件
     * @param reservedBits 保留位
     * @param roundingMode 舍入模式
     * @param getFun       得到乐趣
     * @return [BigDecimal]
     * @author zjarlin
     * @since 2022/06/29
     */
    @SafeVarargs
    fun <E> sumproduct(
        collection: Collection<E>,
        condition: Predicate<E>?,
        reservedBits: Int,
        roundingMode: RoundingMode? = RoundingMode.HALF_UP,
        vararg getFun: Function<E, BigDecimal?>,
    ): BigDecimal {
        val reduce = collection.stream().filter { obj: E -> Objects.nonNull(obj) }.filter(condition).map { e: E ->
            val ret = arrayOf(BigDecimal.ONE)
            Arrays.stream(getFun).forEach { fn: Function<E, BigDecimal?> ->
                val bigDecimal = if (null == fn.apply(e)) BigDecimal.ZERO else fn.apply(e)!!
                ret[0] = ret[0].multiply(bigDecimal)
            }
            ret[0]
        }.reduce(BigDecimal.ZERO) { obj: BigDecimal, augend: BigDecimal? -> obj.add(augend) }
        return reduce.setScale(reservedBits, roundingMode)
    }
}