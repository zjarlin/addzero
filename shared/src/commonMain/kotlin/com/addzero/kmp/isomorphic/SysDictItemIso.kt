@file:OptIn(ExperimentalTime::class)
package com.addzero.kmp.isomorphic

            
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
 @Serializable
data class SysDictItemIso(
                          /*
           * 
字典项文本

           */
            val itemText: String  = "" ,
                          /*
           * 
字典项值

           */
            val itemValue: String  = "" ,
                          /*
           * 
描述

           */
            val description: String?  = null ,
                          /*
           * 
排序

           */
            val sortOrder: Long?  = null ,
                          /*
           * 
状态（1启用 0不启用）

           */
            val status: Long?  = null ,
    
val sysDict: SysDictIso?  = null ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlin.time.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)