package com.addzero.kmp.isomorphic

            
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
 @Serializable
data class SysDictIso(
                          /*
           * 
字典名称

           */
            val dictName: String  = "" ,
                          /*
           * 
字典编码

           */
            val dictCode: String  = "" ,
                          /*
           * 
描述

           */
            val description: String?  = null ,
    
val sysDictItems: List<SysDictItemIso>  = emptyList() ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null ,
    
val deleted: Int  = 0 
)