//package com.addzero.web.modules.biz_dotfiles.constants
//
//import com.fasterxml.jackson.annotation.JsonCreator
//import com.fasterxml.jackson.annotation.JsonValue
//import org.babyfish.jimmer.sql.EnumItem
//
///**
// * /**
// *  状态
// *  1= 启用
// *  0= 未启用
//*/
// *
// * @author AutoDDL
// * @date 2025-02-06 12:15:11
// */
//enum class EnumStatus(
//    val code: String?,
//    val desc: String
//) {
//    /**
//     * 启用
//     */
//    @EnumItem(name = "1")
//    QIYONG("1", "启用"),
//
//    /**
//     * 未启用
//     */
//    @EnumItem(name = "0")
//    WEI_QIYONG("0", "未启用");
//
//    @JsonValue
//    fun getValue(): String {
//        return desc
//    }
//
//    companion object {
//        @JsonCreator
//        fun fromCode(code: String?): EnumStatus? = values().find { it.code == code }
//    }
//}
