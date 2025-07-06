//package com.addzero.web.modules.biz_dotfiles.constants
//import com.fasterxml.jackson.annotation.JsonCreator
//import com.fasterxml.jackson.annotation.JsonValue
//import org.babyfish.jimmer.sql.EnumItem
//
///**
// * /**
// *  定义类型
// *  alias=alias
// *  export=export
// * function=function
// * sh=sh
// * var=var
//*/
// *
// * @author AutoDDL
// * @date 2025-02-06 17:25:19
// */
//enum class EnumDefType(
//    val code: String?,
//    val desc: String
//) {
//    /**
//     * alias
//     */
//    @EnumItem(name = "1")
//    ALIAS("code_1", "alias"),
//
//    /**
//     * export
//     */
//    @EnumItem(name = "2")
//    EXPORT("code_2", "export"),
//
//    /**
//     * function
//     */
//    @EnumItem(name = "3")
//    FUNCTION("code_3", "function"),
//
//    /**
//     * sh
//     */
//    @EnumItem(name = "4")
//    SH("code_4", "sh"),
//
//    /**
//     * var
//     */
//    @EnumItem(name = "5")
//    VAR("code_5", "var");
//
//    @JsonValue
//    fun getValue(): String {
//        return desc
//    }
//
//    companion object {
//        @JsonCreator
//        fun fromCode(code: String?): EnumDefType? = entries.find { it.code == code }
//        fun fromDesc(desc: String?): EnumDefType? = entries.find { it.desc == desc }
//    }
//}
