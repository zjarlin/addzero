package weatherutil

import java.time.LocalDate
import java.time.YearMonth

internal object Assist {
    fun isFutureDate(year: Int, month: Int): Boolean {
        // 获取当前日期和月份
        val today = LocalDate.now()
        val currentYear = today.getYear()
        val currentMonth = today.getMonthValue()
        // 判断输入的年份和月份是否在当前日期之后
        if (year > currentYear) {
            return true
        } else if (year == currentYear && month > currentMonth) {
            return true
        }
        // 判断输入的年份和月份是否在当前月份之后，跨年情况
        val inputYearMonth = YearMonth.of(year, month)
        val currentYearMonth = YearMonth.of(currentYear, currentMonth)
        return inputYearMonth.isAfter(currentYearMonth)
    }

//    fun splitDateAndWeekday(input: String): Pair<String, String> {
//        return try {
//            val parts = input.split(" ")
//            if (parts.size == 3) {
//                Pair(parts[0], parts[2]) // 提取日期和星期
//            } else {
//                Pair(input, input) // 格式错误，返回原字符串
//            }
//        } catch (e: Exception) {
//            Pair(input, "") // 异常时返回原字符串
//        }
//    }

    fun String.splitDateAndWeekday(): Pair<String, String> {
        val parts = this.trim().split("\\s+".toRegex())
        return if (parts.size == 2) Pair(parts[0], parts[1]) else Pair(this, this)
    }

    // 测试
    fun main() {
        val input = "2003-05-05 周二"
        val (date, weekday) = input.splitDateAndWeekday()
        println("日期: $date, 星期: $weekday")
    }

}