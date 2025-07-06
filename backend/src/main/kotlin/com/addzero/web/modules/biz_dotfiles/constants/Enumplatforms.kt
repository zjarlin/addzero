//package com.addzero.web.modules.biz_dotfiles.constants
//
//import com.fasterxml.jackson.annotation.JsonCreator
//import com.fasterxml.jackson.annotation.JsonValue
//import org.babyfish.jimmer.sql.EnumItem
//
//fun main() {
//
//    val listOf = listOf(Enumplatforms.ARM64, Enumplatforms.X86)
//    val toString = listOf.toString()
//    println(toString)
//}
//
///**
// * /**
// *
// *  系统架构
// *  arm64=arm64
// *  x86=x86
//*/
// *
// * @author AutoDDL
// * @date 2025-02-06 17:23:17
// */
//enum class Enumplatforms(
//    val code: String?,
//    val desc: String
//) {
//    /**
//     * arm64
//     */
//    @EnumItem(name = "1")
//    ARM64("code_arm64", "arm64"),
//
//    /**
//     * x86
//     */
//    @EnumItem(name = "2")
//    X86("code_x86", "x86"),
//
//    /**
//     * 不限
//     */
//    @EnumItem(name = "0")
//    BUXIAN("code_0", "不限");
//
//    @JsonValue
//    fun getValue(): String {
//        return desc
//    }
//
//    companion object {
//        @JsonCreator
//        fun fromCode(code: String?): Enumplatforms? = values().find { it.code == code }
//    }
//}
