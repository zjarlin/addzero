package com.addzero.addzero_common

import com.addzero.kmp.generated.enums.EnumOsType


fun main() {
    //二进制码表
    val associate = EnumOsType.entries.associate {
        it.toString() to (1 shl it.ordinal)
    }


}


//object EnumBitmaskConverter {
//    private val enumValues = EnumOsType.values()
//
//    private val nameToBitValue = enumValues.associateBy { it.name to it.bitValue }
//
//    // 字符串列表 → 二进制掩码
//    fun stringsToBitmask(names: Collection<String>): Int {
//        return names.fold(0) { mask, name ->
//            mask or (nameToBitValue[name] ?: 0) // 忽略无效名称
//        }
//    }
//
//    // 二进制掩码 → 字符串列表
//    fun bitmaskToStrings(bitmask: Int): List<String> {
//        return enumValues
//            .filter { (bitmask and it.bitValue) != 0 }
//            .map { it.name }
//    }
//}
