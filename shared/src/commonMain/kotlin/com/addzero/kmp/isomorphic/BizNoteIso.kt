@file:OptIn(ExperimentalTime::class)
package com.addzero.kmp.isomorphic

            
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
 @Serializable
data class BizNoteIso(
    
val leafFlag: Boolean  = false ,
                           /*
            * 
笔记的子节点列表，表示当前笔记的子笔记。
通过 {@link OneToMany} 注解与父笔记关联。

@return 子笔记列表

            */
            val children: List<BizNoteIso>  = emptyList() ,
                           /*
            * 
笔记的父节点，表示当前笔记的父笔记。
通过 {@link ManyToOne} 注解与子笔记关联。

@return 父笔记，如果没有父笔记则返回 null

            */
            val parent: BizNoteIso?  = null ,
                           /*
            * 
标题


            */
            val title: String  = "" ,
                           /*
            * 
内容


            */
            val content: String  = "" ,
                            /*
             * 
 类型
 1=markdown
2=pdf
3=word
4=excel

 @return 笔记类型

             */
            val type: String?  = null ,
                           /*
            * 
笔记的标签列表，用于分类和检索。
通过中间表实现与标签的多对多关系

@return 标签列表

            */
            val tags: List<BizTagIso>  = emptyList() ,
                           /*
            * 
笔记的路径

@return 笔记路径

            */
            val path: String?  = null ,
                           /*
            * 
笔记关联的文件链接（可选）。


            */
            val fileUrl: String?  = null ,
               
val id: Long?  = null,
    
val updateBy: SysUserIso?  = null ,
    
val createBy: SysUserIso?  = null ,
    
val createTime: LocalDateTime  = kotlin.time.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) ,
    
val updateTime: LocalDateTime?  = null 
)