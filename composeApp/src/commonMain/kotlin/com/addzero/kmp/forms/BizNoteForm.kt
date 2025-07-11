            package com.addzero.kmp.forms
            import androidx.compose.material.icons.Icons
            import androidx.compose.foundation.layout.*
            import androidx.compose.material3.*
            import androidx.compose.runtime.*
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.unit.dp
            import com.addzero.kmp.component.high_level.AddMultiColumnContainer
                       import com.addzero.kmp.component.drawer.AddDrawer
            //import com.addzero.kmp.component.high_level.AddFormContainer
 
            import com.addzero.kmp.enums.RegexEnum
            import androidx.compose.material.icons.filled.*
            import com.addzero.kmp.component.form.*
           import com.addzero.kmp.component.form.number.*
import com.addzero.kmp.component.form.date.*
 
            import androidx.compose.ui.Alignment
            import com.addzero.kmp.core.ext.parseObjectByKtx
            import com.addzero.kmp.isomorphic.*
                @Composable
     fun BizNoteForm(
     state: MutableState<BizNoteIso>,
visible: Boolean,
         title: String,
 onClose: () -> Unit,
 onSubmit: () -> Unit,
 confirmEnabled: Boolean = true,
  dslConfig: BizNoteFormDsl.() -> Unit = {}
     
     ) {
     

     
        AddDrawer(
     visible = visible,
     title = title,
     onClose = onClose,
     onSubmit = onSubmit,
     confirmEnabled = confirmEnabled,

     ) {
           BizNoteFormOriginal(
         state, dslConfig,
     ) 
     }
     }

              @Composable
        fun BizNoteFormOriginal(
        state: MutableState<BizNoteIso>,
     dslConfig: BizNoteFormDsl.() -> Unit = {}
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    BizNoteFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            BizNoteFormProps.leafFlag to {     
Row(verticalAlignment = Alignment.CenterVertically) {
Text("leafFlag")
    Switch(
        checked = state.value.leafFlag ?: false,
        onCheckedChange = {
            state.value = state.value.copy(leafFlag = it)
        },
    )
    
    Text(
        text = if (  state.value as? Boolean == true) "是" else "否",
        modifier = Modifier.width(40.dp)
    )

}
     }
        ,
            BizNoteFormProps.children to { AddTextField(
    value = state.value.children?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(children = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "笔记的子节点列表，表示当前笔记的子笔记。通过{@linkOneToMany}注解与父笔记关联。@return子笔记列表",
    isRequired = true
) }
        ,
            BizNoteFormProps.parent to { AddTextField(
    value = state.value.parent?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(parent = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "笔记的父节点，表示当前笔记的父笔记。通过{@linkManyToOne}注解与子笔记关联。@return父笔记，如果没有父笔记则返回null",
    isRequired = false
) }
        ,
            BizNoteFormProps.title to { AddTextField(
    value = state.value.title?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(title = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "标题",
    isRequired = true
) }
        ,
            BizNoteFormProps.content to { AddTextField(
    value = state.value.content?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(content = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "内容",
    isRequired = true
) }
        ,
            BizNoteFormProps.type to { AddTextField(
    value = state.value.type?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(type = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "类型1=markdown2=pdf3=word4=excel@return笔记类型",
    isRequired = false
) }
        ,
            BizNoteFormProps.tags to { AddTextField(
    value = state.value.tags?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(tags = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "笔记的标签列表，用于分类和检索。通过中间表实现与标签的多对多关系@return标签列表",
    isRequired = true
) }
        ,
            BizNoteFormProps.path to { AddTextField(
    value = state.value.path?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(path = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "笔记的路径@return笔记路径",
    isRequired = false
) }
        ,
            BizNoteFormProps.fileUrl to { AddTextField(
    value = state.value.fileUrl?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(fileUrl = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "笔记关联的文件链接（可选）。",
    isRequired = false,
    regexEnum = RegexEnum.URL
) }
         
 ) 
       
          val finalItems = remember(renderMap) {
        defaultRenderMap
            .filterKeys { it !in renderMap } // 未被DSL覆盖的字段
            .plus(renderMap.filterValues { it != {} }) // 添加非隐藏的自定义字段
    }.values.toList() 
       
       
    val items = finalItems
 
            AddMultiColumnContainer(
                howMuchColumn = 2,
                items =items
            )
        
 
        
        
        
        }
 
        
        
 
            
        class BizNoteFormDsl(
            val state: MutableState<BizNoteIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun leafFlag(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["leafFlag"] = {}
        render != null -> renderMap["leafFlag"] = {
               render(state)
        }
    }
}


fun children(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["children"] = {}
        render != null -> renderMap["children"] = {
               render(state)
        }
    }
}


fun parent(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["parent"] = {}
        render != null -> renderMap["parent"] = {
               render(state)
        }
    }
}


fun title(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["title"] = {}
        render != null -> renderMap["title"] = {
               render(state)
        }
    }
}


fun content(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["content"] = {}
        render != null -> renderMap["content"] = {
               render(state)
        }
    }
}


fun type(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["type"] = {}
        render != null -> renderMap["type"] = {
               render(state)
        }
    }
}


fun tags(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["tags"] = {}
        render != null -> renderMap["tags"] = {
               render(state)
        }
    }
}


fun path(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["path"] = {}
        render != null -> renderMap["path"] = {
               render(state)
        }
    }
}


fun fileUrl(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["fileUrl"] = {}
        render != null -> renderMap["fileUrl"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["id"] = {}
        render != null -> renderMap["id"] = {
               render(state)
        }
    }
}


fun updateBy(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["updateBy"] = {}
        render != null -> renderMap["updateBy"] = {
               render(state)
        }
    }
}


fun createBy(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["createBy"] = {}
        render != null -> renderMap["createBy"] = {
               render(state)
        }
    }
}


fun createTime(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["createTime"] = {}
        render != null -> renderMap["createTime"] = {
               render(state)
        }
    }
}


fun updateTime(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizNoteIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["updateTime"] = {}
        render != null -> renderMap["updateTime"] = {
               render(state)
        }
    }
}


            
            fun hide(vararg fields: String) {
                fields.forEach { renderMap[it] = {} }
            }
        }
object BizNoteFormProps {
const val leafFlag = "leafFlag"
const val children = "children"
const val parent = "parent"
const val title = "title"
const val content = "content"
const val type = "type"
const val tags = "tags"
const val path = "path"
const val fileUrl = "fileUrl"
}
@Composable
fun rememberBizNoteFormState(current:BizNoteIso?=null): MutableState<BizNoteIso> {
    return remember (current){ mutableStateOf(current?: BizNoteIso ()) }
}