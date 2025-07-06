        package com.addzero.kmp.generated.enums
        
        /**
         * 操作系统
         * 
         * 数据库字典编码: os_type
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumOsType(
            val code: String,
            val desc: String
        ) {
            LINUX("linux", "linux"),
MAC("mac", "mac"),
WIN("win", "win");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumOsType? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumOsType? {
                    return entries.find { it.desc == desc }
                }
            }
        }