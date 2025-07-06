package com.addzero.kmp.isomorphic

            
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
 @Serializable
data class SysDeptIso(
               /*
* 部门名称  
*/
           val name: String  = "" ,
    
val parent: SysDeptIso?  = null ,
    
val children: List<SysDeptIso>  = emptyList() ,
               /*
* 部门用户 
*/
           val sysUsers: List<SysUserIso>  = emptyList() ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)