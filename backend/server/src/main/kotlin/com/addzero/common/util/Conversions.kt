package com.addzero.common.util

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.lang.Pair
import java.time.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * 转换工具类
 *
 * @author zjarlin
 * @see Class
 *
 * @since 2022/06/15
 */
object Conversions {
    /**
     * LocalDate转Date
     *
     * @param localDate 当地日期
     * @return 返回时间
     * @author zjarlin
     * @since 2022/06/29
     */
    fun toDate(localDate: LocalDate): Date? {
        if (Objects.isNull(localDate)) {
            return null
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime 当地日期时间
     * @return 返回时间
     * @author zjarlin
     * @since 2022/06/29
     */
    fun toDate(localDateTime: LocalDateTime): Date? {
        if (Objects.isNull(localDateTime)) {
            return null
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    /**
     * Date转LocalDate
     *
     * @param date 日期
     * @return 返回日期
     * @author zjarlin
     * @since 2022/06/29
     */
    fun toLocalDate(date: Date): LocalDate? {
        if (Objects.isNull(date)) {
            return null
        }
        return Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    /**
     * Date转LocalDateTime
     *
     * @param date 日期
     * @return 返回时间
     * @author zjarlin
     * @since 2022/06/29
     */
    fun toLocalDateTime(date: Date): LocalDateTime? {
        if (Objects.isNull(date)) {
            return null
        }
        return Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun getWeek(localDate: LocalDate): String? {
        val dayOfWeek: DayOfWeek = localDate.dayOfWeek
        return getString(dayOfWeek)
    }

    fun getString(dayOfWeek: DayOfWeek): String? {
        val hashMap: HashMap<DayOfWeek, String> = hashMapOf(
            DayOfWeek.MONDAY to "周一",
            DayOfWeek.TUESDAY to "周二",
            DayOfWeek.WEDNESDAY to "周三",
            DayOfWeek.THURSDAY to "周四",
            DayOfWeek.FRIDAY to "周五",
            DayOfWeek.SATURDAY to "周六",
            DayOfWeek.SUNDAY to "周日"
        )
        val s = hashMap[dayOfWeek]
        return s
    }

    fun getWeek(localDateTime: LocalDateTime): String? {
        val dayOfWeek: DayOfWeek = localDateTime.dayOfWeek
        return getString(dayOfWeek)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val localDates = getsACollectionOfAllDaysInASpecifiedMonth(2022, 9)
        println(localDates)
    }

    /**
     * 获取指定年月当月所有天数集合
     *
     * @param year
     * @param month 月 入参
     * @return [Collection]<[LocalDate]>
     * @author addzero
     * @since 2022/10/27
     */
    fun getsACollectionOfAllDaysInASpecifiedMonth(year: Int, month: Int): Collection<LocalDate> {
        val of = LocalDate.of(year, month, 1)
        val firstDay = of.with(TemporalAdjusters.firstDayOfMonth()) // 获取当前月的第一天
        val lastDay = of.with(TemporalAdjusters.lastDayOfMonth()) // 获取当前月的最后一天
        val intervalNumber = firstDay.until(lastDay, ChronoUnit.DAYS) + 1
        //long daysToSubtract = firstDay.toEpochDay();
        //日期差几天
        //LocalDate localDate = lastDay.minusDays(daysToSubtract);
        //时间间隔个数
        //long intervalNumber = localDate.toEpochDay()+1;
        val collect1: Collection<LocalDate> =
            Stream.iterate(firstDay) { e: LocalDate -> e.plusDays(1) }.limit(intervalNumber).collect(
                Collectors.toSet()
            )
        return collect1
    }

    /**
     * 获取指定日期集合在当月中补集
     *
     * @param srcLocalDate 收集 入参
     * @return [Collection]<[LocalDate]>
     * @author addzero
     * @since 2022/10/27
     */
    fun midMonthSupplement(srcLocalDate: Collection<LocalDate>): Collection<LocalDate> {
        val year = srcLocalDate.stream().map { obj: LocalDate -> obj.year }.findAny().orElse(null)
        val month = srcLocalDate.stream().map { obj: LocalDate -> obj.monthValue }.findAny().orElse(null)
        //获取当月数据天数集合
        val localDates = getsACollectionOfAllDaysInASpecifiedMonth(
            year!!, month!!
        )
        val subtract: Collection<LocalDate> = CollUtil.subtract<LocalDate>(localDates, srcLocalDate)
        return subtract
    }

    /**
     * 获取指定年月有多少个工作日）
     *
     * @param year
     * @param month
     * @return
     */
    fun countWorkDay(year: Int, month: Int): Int {
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        // 月份是从0开始计算，所以需要减1
        c.set(Calendar.MONTH, month - 1)

        // 当月最后一天的日期
        val max: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        // 开始日期为1号
        var start = 1
        // 计数
        var count = 0
        while (start <= max) {
            c.set(Calendar.DAY_OF_MONTH, start)
            if (isWorkDay(c)) {
                count++
            }
            start++
        }
        return count
    }

    // 判断是否工作日（未排除法定节假日，由于涉及到农历节日，处理很麻烦）
    fun isWorkDay(c: Calendar): Boolean {
        // 获取星期,1~7,其中1代表星期日，2代表星期一 ... 7代表星期六
        val week: Int = c.get(Calendar.DAY_OF_WEEK)
        // 不是周六和周日的都认为是工作日
        return week != Calendar.SUNDAY && week != Calendar.SATURDAY
    }

    val minMaxOfOneDay: Pair<LocalDateTime, LocalDateTime>?
        /**
         * 得到一天中最小时间和最大时间
         *
         *
         * 入参
         *
         * @return [Pair]<[LocalDateTime], [LocalDateTime]>
         * @author addzero
         * @since 2022/10/31
         */
        get() {
            val now = LocalDate.now()
            //LocalDate of1 = LocalDate.of(2022, 10, 28);
            //LocalDateTime start = LocalDateTime.of(of1, LocalTime.MIN);
            //LocalDateTime end = LocalDateTime.of(of1, LocalTime.MAX);
            val start = LocalDateTime.of(now, LocalTime.MIN)
            val end = LocalDateTime.of(now, LocalTime.MAX)
            val of = Pair.of(start, end)
            return of
        }

    fun addDays(date: Date, days: Int): Date {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.add(Calendar.DATE, days)
        return calendar.time
    }
}