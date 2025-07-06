        package com.addzero.kmp.generated.enums
        
        /**
         * 性别
         * 
         * 数据库字典编码: sys_gender
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumSysGender(
            val code: String,
            val desc: String
        ) {
            NAN("1", "男"),
NU("2", "女");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumSysGender? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumSysGender? {
                    return entries.find { it.desc == desc }
                }
            }
        }