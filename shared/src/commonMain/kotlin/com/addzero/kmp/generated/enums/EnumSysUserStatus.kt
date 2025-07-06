        package com.addzero.kmp.generated.enums
        
        /**
         * 用户状态
         * 
         * 数据库字典编码: sys_user_status
         * 自动生成的枚举类，不要手动修改
         */
        enum class EnumSysUserStatus(
            val code: String,
            val desc: String
        ) {
            TINGYONG("0", "停用"),
ZHENGCHANG("1", "正常");
            
            companion object {
                /**
                 * 根据编码获取枚举值
                 * 
                 * @param code 编码
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromCode(code: String): EnumSysUserStatus? {
                    return entries.find { it.code == code }
                }
                
                /**
                 * 根据描述获取枚举值
                 * 
                 * @param desc 描述
                 * @return 对应的枚举值，如果不存在则返回null
                 */
                fun fromDesc(desc: String): EnumSysUserStatus? {
                    return entries.find { it.desc == desc }
                }
            }
        }