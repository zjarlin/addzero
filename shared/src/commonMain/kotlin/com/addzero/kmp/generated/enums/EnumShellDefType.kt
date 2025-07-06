        package com.addzero.kmp.generated.enums
        
        /**
         * 定义类型
         * 
         * 数据库字典编码: shell_def_type
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumShellDefType(
            val code: String,
            val desc: String
        ) {
            ALIAS("alias", "alias"),
EXPORT("export", "export"),
FUNCTION("function", "function"),
SH("sh", "sh"),
VAR("var", "var");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumShellDefType? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumShellDefType? {
                    return entries.find { it.desc == desc }
                }
            }
        }