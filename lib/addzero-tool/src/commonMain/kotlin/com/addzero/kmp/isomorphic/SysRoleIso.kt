package com.addzero.kmp.isomorphic

            
import com.addzero.kmp.generated.enums.EnumSysToggle
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
 @Serializable
data class SysRoleIso(
                           /*
            * 
角色编码

            */
            val roleCode: String  = "" ,
                           /*
            * 
角色名称

            */
            val roleName: String  = "" ,
                           /*
            * 
是否为系统角色

            */
            val systemFlag: Boolean  = false ,
                           /*
            * 
角色状态

            */
            val status: EnumSysToggle  = com.addzero.kmp.generated.enums.EnumSysToggle.entries.first() ,
    
val sysUsers: List<SysUserIso>  = emptyList() ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)