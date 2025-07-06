        package com.addzero.kmp.generated.enums
        
        /**
         * 系统开关
         * 
         * 数据库字典编码: sys_toggle
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumSysToggle(
            val code: String,
            val desc: String
        ) {
            GUANBI("0", "关闭"),
KAIQI("1", "开启");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumSysToggle? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumSysToggle? {
                    return entries.find { it.desc == desc }
                }
            }
        }