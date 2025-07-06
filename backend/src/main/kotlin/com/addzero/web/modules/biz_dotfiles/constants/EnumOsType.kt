//package com.addzero.web.modules.biz_dotfiles.constants
//
//import com.addzero.web.infra.jimmer.enum.BaseEnum
//import org.babyfish.jimmer.sql.EnumItem
//
///**
// * /**
// *  操作系统
// *  win=win
// * linux-linux
// * mac mac
//*/
// *
// * @author AutoDDL
// * @date 2025-02-11 10:14:36
// */
//enum class EnumOsType(
//    val code: String?, override val desc: String,
//) : BaseEnum<EnumOsType, String> {
//
//
//    /**
//     * win
//     */
//    @EnumItem(name = "1")
//    WIN("code_1", "win系统") {
//        override val value: String
//            get() = "1"
//    },
//
//    /**
//     * linux
//     */
//    @EnumItem(name = "2")
//    LINUX("code_2", "linux系统") {
//        override val value: String
//            get() = "2"
//    },
//
//    /**
//     * mac
//     */
//    @EnumItem(name = "3")
//    MAC("code_3", "mac系统") {
//        override val value: String
//            get() = "3"
//    },
//
//    /**
//     * 不限
//     */
//    @EnumItem(name = "0")
//    BUXIAN("code_0", "不限") {
//        override val value: String
//            get() = "0"
//    };
//
//
//}
