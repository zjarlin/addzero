        package com.addzero.kmp.generated.enums
        
        /**
         * 系统架构
         * 
         * 数据库字典编码: shell_platforms
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumShellPlatforms(
            val code: String,
            val desc: String
        ) {
            ARM64("arm64", "arm64"),
X86("x86", "x86");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumShellPlatforms? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumShellPlatforms? {
                    return entries.find { it.desc == desc }
                }
            }
        }