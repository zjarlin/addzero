package site.addzero.util.str


/**
 * 清理字符串中的空白字符
 * 包括：
 * 1. 移除首尾空白字符
 * 2. 将连续的空白字符替换为单个空格
 * 3. 移除不可见字符
 *
 * @param str 要清理的字符串
 * @return 清理后的字符串
 */
fun String?.cleanBlank(): String {
    if (this.isNullOrEmpty()) return ""

    return this!!.trim()
        .replace(Regex("\\s+"), " ") // 将连续的空白字符替换为单个空格
        .filter { it.isDefined() } // 移除不可见字符
}

/**
 * 判断字符是否可见
 */
private fun Char.isDefined(): Boolean {
    return this.code in 32..126 || this.code in 0x4E00..0x9FFF
}

/**
 * 删除字符串中最后一次出现的指定字符。
 * 注意这和removeSuffixifnot有所不同(该方法只是移除最后一个字符,而不是最后出现的字符,例如如最后一个是空格就翻车了)
 *
 * @param str 字符串
 * @param ch 要删除的字符
 * @return 删除指定字符后的字符串
 */
fun removeLastCharOccurrence(str: String, ch: Char): String {
    if (str.isNullOrEmpty()) {
        return ""
    }

    val lastIndex = str.lastIndexOf(ch) // 获取指定字符最后一次出现的位置
    return if (lastIndex != -1) {
        // 如果找到了指定字符，则删除它
        str.substring(0, lastIndex) + str.substring(lastIndex + 1)
    } else {
        // 如果没有找到指定字符，则返回原字符串
        str!!
    }
}


fun <T> groupBySeparator(lines: List<T>, predicate: (T) -> Boolean): Map<T, List<T>> {
    val separatorIndices = lines.indices.filter { predicate(lines[it]) }
    return separatorIndices.mapIndexed { index, spe ->
        val next = if (index + 1 < separatorIndices.size) {
            separatorIndices[index + 1]
        } else {
            lines.size // 如果没有下一个分隔符，取行的总数
        }

        val subList = lines.subList(spe + 1, next)
        lines[spe] to subList // 使用 Pair 进行配对
    }.toMap()
}


fun String.makeSurroundWith(fix: String): String {
    val addPrefixIfNot = this.addPrefixIfNot(fix)
    val addSuffixIfNot = addPrefixIfNot.addSuffixIfNot(fix)
    return addSuffixIfNot

}


/**
 * 如果字符串不以指定后缀结尾，则添加该后缀
 *
 * @param suffix 要添加的后缀
 * @param ignoreCase 是否忽略大小写，默认为false
 * @return 添加后缀后的字符串
 */
fun String?.addSuffixIfNot(suffix: String, ignoreCase: Boolean = false): String {
    if (this == null) {
        return suffix
    }

    return if (this.endsWith(suffix, ignoreCase)) {
        this
    } else {
        this + suffix
    }
}


fun String?.isNotBlank(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * 扩展函数：移除重复符号
 */
fun String?.removeDuplicateSymbol(duplicateElement: String): String {
    if (this.isNullOrEmpty() || duplicateElement.isEmpty()) {
        return this ?: ""
    }

    val sb = StringBuilder()
    var previous = "" // 初始化前一个元素，用于比较
    var i = 0

    while (i < this.length) {
        val elementLength = duplicateElement.length
        if (i + elementLength <= this.length && this.substring(i, i + elementLength) == duplicateElement) {
            if (previous != duplicateElement) {
                sb.append(duplicateElement)
                previous = duplicateElement
            }
            i += elementLength
        } else {
            sb.append(this[i])
            previous = this[i].toString()
            i++
        }
    }
    return sb.toString()
}

/**
 * 扩展函数：清理多余的char
 */
fun String?.removeDuplicateSymbol(symbol: Char): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val sb = StringBuilder()
    var prevIsSymbol = false

    for (c in this!!.toCharArray()) {
        if (c == symbol) {
            if (!prevIsSymbol) {
                sb.append(c)
                prevIsSymbol = true
            }
        } else {
            sb.append(c)
            prevIsSymbol = false
        }
    }
    return sb.toString()
}

/**
 * 扩展函数：提取路径部分
 */
fun String.getPathFromRight(n: Int): String? {
    val parts = this.split(".").filter { it.isNotEmpty() }

    if (parts!!.size < n) {
        return this // 输入字符串中的路径部分不足n个，返回整个输入字符串
    }

    return parts.dropLast(n).joinToString(".")
}


fun String?.lowerCase(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val lowerCase = this.lowerCase()
    return lowerCase

}

/**
 * 如果字符串不以指定前缀开头，则添加该前缀
 *
 * @param prefix 要添加的前缀
 * @param ignoreCase 是否忽略大小写，默认为false
 * @return 添加前缀后的字符串
 */
fun String?.addPrefixIfNot(prefix: String, ignoreCase: Boolean = false): String {
    if (this.isNullOrEmpty()) return ""

    val string = if (ignoreCase) {
        if (this!!.startsWith(prefix, ignoreCase = true)) this else prefix + this
    } else {
        if (this!!.startsWith(prefix)) this else prefix + this
    }
    return string
}


/**
 * 字符串首字母小写
 */
fun String.lowerFirst(): String {
    if (isEmpty()) return this
    return first().lowercase() + substring(1)
}


/**
 * 检查字符串是否不在列表中（不区分大小写）
 */
infix fun String.ignoreCaseNotIn(collection: Collection<String>): Boolean {
    val b = this ignoreCaseIn collection
    return !b
}

/**
 * 检查字符串是否在列表中（不区分大小写）
 */
infix fun String.ignoreCaseIn(collection: Collection<String>): Boolean =
    collection.any { it.equals(this, ignoreCase = true) }

/**
 * 检查字符串是否包含任意一个给定的子字符串（忽略大小写）
 * @param substrings 要检查的子字符串集合
 * @return 如果包含任意一个子字符串则返回true，否则返回false
 */
fun String.containsAnyIgnoreCase(vararg substrings: String): Boolean {
    if (substrings.isEmpty()) return false
    val lowerThis = this.lowercase()
    return substrings.any { it.lowercase() in lowerThis }
}

fun String.toBigCamelCase(): String {
    return this.split("_").joinToString("") {
        it.replaceFirstChar { char -> char.uppercase() }
    }
}

/**
 * 将列名转换为驼峰命名
 */
fun String.toLowCamelCase(): String {
    return this.split("_").joinToString("") {
        it.replaceFirstChar { char -> char.uppercase() }
    }.replaceFirstChar { it.lowercase() }
}

// 添加从 JlStrUtil.kt 迁移的方法

/**
 * 检查字符序列是否包含任意一个给定的测试字符序列
 *
 * @param testStrs 要检查的测试字符序列集合
 * @return 如果包含任意一个测试字符序列则返回true，否则返回false
 */
fun CharSequence.containsAny(vararg testStrs: CharSequence): Boolean {
    if (this.isEmpty() || testStrs.isEmpty()) {
        return false
    }

    for (testStr in testStrs) {
        if (this.contains(testStr)) {
            return true
        }
    }

    return false
}

infix fun String.ignoreCaseLike(other: String): Boolean {
    return this.contains(other, ignoreCase = true)
}

object JlStrUtil {

    /**
     * 清理字符串中的空白字符
     * 包括：
     * 1. 移除首尾空白字符
     * 2. 将连续的空白字符替换为单个空格
     * 3. 移除不可见字符
     *
     * @param str 要清理的字符串
     * @return 清理后的字符串
     */
    fun String?.cleanBlank(): String {
        if (this.isNullOrEmpty()) return ""

        return this!!.trim()
            .replace(Regex("\\s+"), " ") // 将连续的空白字符替换为单个空格
            .filter { it.isDefined() } // 移除不可见字符
    }

    /**
     * 扩展函数：检查是否包含中文
     */
    fun CharSequence?.containsChinese(): Boolean {
        if (this.isNullOrEmpty()) {
            return false
        }
        val containsMatchIn = Regex("[\\u4e00-\\u9fa5]").containsMatchIn(this)
        return containsMatchIn
    }

    /**
     * 判断字符是否可见
     */
    private fun Char.isDefined(): Boolean {
        return this.code in 32..126 || this.code in 0x4E00..0x9FFF
    }

    /**
     * 删除字符串中最后一次出现的指定字符。
     * 注意这和removeSuffixifnot有所不同(该方法只是移除最后一个字符,而不是最后出现的字符,例如如最后一个是空格就翻车了)
     *
     * @param str 字符串
     * @param ch 要删除的字符
     * @return 删除指定字符后的字符串
     */
    fun removeLastCharOccurrence(str: String, ch: Char): String {
        if (str.isNullOrEmpty()) {
            return ""
        }

        val lastIndex = str.lastIndexOf(ch) // 获取指定字符最后一次出现的位置
        return if (lastIndex != -1) {
            // 如果找到了指定字符，则删除它
            str.substring(0, lastIndex) + str.substring(lastIndex + 1)
        } else {
            // 如果没有找到指定字符，则返回原字符串
            str!!
        }
    }


    fun <T> groupBySeparator(lines: List<T>, predicate: (T) -> Boolean): Map<T, List<T>> {
        val separatorIndices = lines.indices.filter { predicate(lines[it]) }
        return separatorIndices.mapIndexed { index, spe ->
            val next = if (index + 1 < separatorIndices.size) {
                separatorIndices[index + 1]
            } else {
                lines.size // 如果没有下一个分隔符，取行的总数
            }

            val subList = lines.subList(spe + 1, next)
            lines[spe] to subList // 使用 Pair 进行配对
        }.toMap()
    }


    fun join(split: String, vararg testStrs: CharSequence): String {
        if (testStrs.isEmpty()) {
            return ""
        }
        return testStrs.joinToString(split)
    }


}


fun String.withPkg(pkg: String): String {
    return "$this/${pkg.replace(".", "/")}"
}

fun String.withFileName(fileName: String): String {
    return "$this/$fileName"
}

fun String.withFileSuffix(suffix: String = ".kt"): String {
    return "$this$suffix"
}

fun CharSequence.removeAnyQuote(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    return removeAny(
        this,
        "\"",
        "\\",
        "\\n",
        " ",
        "\r\n",  // Windows 换行符
        "\n",    // Linux/macOS 换行符
        "\r"     // 旧版 Mac 换行符
    )
}

/**
 * 从字符序列中移除所有指定的字符串片段
 *
 * @param str 原字符序列
 * @param stringsToRemove 要移除的字符串片段
 * @return 移除指定字符串片段后的新字符串
 */
fun removeAny(str: CharSequence, vararg stringsToRemove: String): String {
    var result = str.toString()
    for (toRemove in stringsToRemove) {
        result = result.replace(toRemove, "")
    }
    return result
}

fun String.toUnderLineCase(): String {
    val sb = StringBuilder()
    for ((index, char) in this.withIndex()) {
        if (index > 0 && char.isUpperCase()) {
            sb.append('_')
        }
        sb.append(char)
    }
    return sb.toString()
}

/**
 * 简化的 KMP 兼容字符串格式化函数
 * 只支持 %.nf 格式的浮点数格式化
 */
fun String.kmpFormat(vararg args: Any?): String {
    var result = this
    var argIndex = 0

    // 简单的 %.1f 替换
    if (result.contains("%.1f") && argIndex < args.size) {
        val value = args[argIndex++]
        val formatted = when (value) {
            is Double -> formatDouble(value, 1)
            is Float -> formatDouble(value.toDouble(), 1)
            is Number -> formatDouble(value.toDouble(), 1)
            else -> value.toString()
        }
        result = result.replace("%.1f", formatted)
    }

    // 简单的 %.0f 替换
    if (result.contains("%.0f") && argIndex < args.size) {
        val value = args[argIndex++]
        val formatted = when (value) {
            is Double -> formatDouble(value, 0)
            is Float -> formatDouble(value.toDouble(), 0)
            is Number -> formatDouble(value.toDouble(), 0)
            else -> value.toString()
        }
        result = result.replace("%.0f", formatted)
    }

    return result
}

/**
 * 格式化双精度浮点数到指定小数位数
 */
private fun formatDouble(value: Double, decimals: Int): String {
    if (decimals == 0) {
        return kotlin.math.round(value).toLong().toString()
    }

    // 手动计算10的n次方，避免使用pow函数
    var multiplier = 1.0
    repeat(decimals) {
        multiplier *= 10.0
    }

    val rounded = kotlin.math.round(value * multiplier) / multiplier

    // 手动构建小数位数
    val intPart = rounded.toLong()
    val fracPart = kotlin.math.abs(rounded - intPart)

    if (fracPart == 0.0) {
        return "$intPart.${"0".repeat(decimals)}"
    }

    val fracStr = (fracPart * multiplier).toLong().toString().padStart(decimals, '0')
    return "$intPart.$fracStr"
}

/**
 * 便捷的数字格式化函数
 */
fun Double.formatDecimal(decimals: Int = 2): String {
    return formatDouble(this, decimals)
}

fun Float.formatDecimal(decimals: Int = 2): String {
    return formatDouble(this.toDouble(), decimals)
}

/**
 * 格式化为货币显示（保留指定小数位，默认0位）
 */
fun Double.formatCurrency(decimals: Int = 0): String {
    return formatDouble(this, decimals)
}

fun Float.formatCurrency(decimals: Int = 0): String {
    return formatDouble(this.toDouble(), decimals)
}


