        package com.addzero.kmp.generated.enums
        
        /**
         * 系统主题
         * 
         * 数据库字典编码: sys_theme
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumSysTheme(
            val code: String,
            val desc: String
        ) {
            LAN_SE_ANSE("DARK_BLUE", "蓝色暗色"),
MO_REN_ANSE("DARK_DEFAULT", "默认暗色"),
LU_SE_ANSE("DARK_GREEN", "绿色暗色"),
ZI_SE_ANSE("DARK_PURPLE", "紫色暗色"),
LAN_SE_LIANGSE("LIGHT_BLUE", "蓝色亮色"),
MO_REN_LIANGSE("LIGHT_DEFAULT", "默认亮色"),
LU_SE_LIANGSE("LIGHT_GREEN", "绿色亮色"),
ZI_SE_LIANGSE("LIGHT_PURPLE", "紫色亮色");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumSysTheme? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumSysTheme? {
                    return entries.find { it.desc == desc }
                }
            }
        }