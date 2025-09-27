package site.addzero.enums

/**
 * 枚举工具类
 * 提供 KMP 兼容的枚举操作方法
 */
object EnumUtils {

    /**
     * 获取枚举的所有值（KMP 兼容）
     * @param enumClass 枚举类
     * @return 枚举值列表
     */
    inline fun <reified T : Enum<T>> getEnumValues(): List<T> {
        val enumValues = enumValues<T>()
        return enumValues.toList()
    }

}
