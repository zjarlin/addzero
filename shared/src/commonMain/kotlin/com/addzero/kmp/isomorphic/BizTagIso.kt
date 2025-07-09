@file:OptIn(ExperimentalTime::class)
package com.addzero.kmp.isomorphic

            
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
 @Serializable
data class BizTagIso(
                           /*
            * 
标签名称

            */
            val name: String  = "" ,
                           /*
            * 
标签描述

            */
            val description: String?  = null ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlin.time.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)