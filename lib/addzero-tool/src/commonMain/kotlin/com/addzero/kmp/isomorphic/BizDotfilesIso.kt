package com.addzero.kmp.isomorphic

            
import com.addzero.kmp.generated.enums.EnumOsType
import com.addzero.kmp.generated.enums.EnumShellDefType
import com.addzero.kmp.generated.enums.EnumShellPlatforms
import com.addzero.kmp.generated.enums.EnumSysToggle
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
 @Serializable
data class BizDotfilesIso(
                            /*
             * 
  操作系统
  win=win
 linux=linux
 mac=mac
null=不限

             */
            val osType: List<EnumOsType>  = emptyList() ,
                          /*
           * 
系统架构
arm64=arm64
x86=x86
不限=不限

           */
            val osStructure: EnumShellPlatforms?  = null ,
                           /*
            * 
 定义类型
 alias=alias
 export=export
function=function
sh=sh
var=var

            */
            val defType: EnumShellDefType  = com.addzero.kmp.generated.enums.EnumShellDefType.entries.first() ,
                          /*
           * 
名称

           */
            val name: String  = "" ,
                          /*
           * 
值

           */
            val value: String  = "" ,
                          /*
           * 
注释

           */
            val describtion: String?  = null ,
                          /*
           * 
状态
1= 启用
0= 未启用

           */
            val status: EnumSysToggle  = com.addzero.kmp.generated.enums.EnumSysToggle.entries.first() ,
               /*
* 文件地址 
*/
           val fileUrl: String?  = null ,
               /*
* 文件位置 
*/
           val location: String?  = null ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)