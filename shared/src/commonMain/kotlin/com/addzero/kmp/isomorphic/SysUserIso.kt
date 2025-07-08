package com.addzero.kmp.isomorphic

            
import com.addzero.kmp.generated.enums.EnumSysGender
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
 @Serializable
data class SysUserIso(
                            /*
             * 
价格

             */
            val price: String  = "" ,
                            /*
             * 
整数

             */
            val testInt: Int  = 0 ,
                                      /*
            * 
主键

            */
            val id: Long?  = null,
                           /*
            * 
手机号

            */
            val phone: String?  = null ,
                           /*
            * 
电子邮箱

            */
            val email: String  = "" ,
                           /*
            * 
用户名

            */
            val username: String  = "" ,
                           /*
            * 
密码

            */
            val password: String  = "" ,
                           /*
            * 
头像

            */
            val avatar: String?  = null ,
                           /*
            * 
昵称

            */
            val nickname: String?  = null ,
                           /*
            * 
性别

            */
            val gender: EnumSysGender?  = null ,
                           /*
            * 
所属部门

            */
            val depts: List<SysDeptIso>  = emptyList() ,
                           /*
            * 
角色列表

            */
            val roles: List<SysRoleIso>  = emptyList() ,
    
val createTime: LocalDateTime  = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)