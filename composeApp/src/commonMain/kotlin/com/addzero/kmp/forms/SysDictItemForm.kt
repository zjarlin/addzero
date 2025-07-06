            package com.addzero.kmp.forms
            import androidx.compose.foundation.layout.*
            import androidx.compose.material3.*
            import androidx.compose.runtime.*
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.unit.dp
            import com.addzero.kmp.component.high_level.AddMultiColumnContainer
            import com.addzero.kmp.component.high_level.AddFormContainer
            import com.addzero.kmp.enums.RegexEnum
            import androidx.compose.material.icons.filled.*
            import com.addzero.kmp.component.form.*
            import com.addzero.kmp.core.ext.parseObjectByKtx
            import com.addzero.kmp.isomorphic.*
        class SysDictItemFormDsl(
            val state: MutableState<SysDictItemIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun itemText(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["itemText"] = {}
        render != null -> renderMap["itemText"] = {
               render(state)
        }
    }
}


fun itemValue(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["itemValue"] = {}
        render != null -> renderMap["itemValue"] = {
               render(state)
        }
    }
}


fun description(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["description"] = {}
        render != null -> renderMap["description"] = {
               render(state)
        }
    }
}


fun sortOrder(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["sortOrder"] = {}
        render != null -> renderMap["sortOrder"] = {
               render(state)
        }
    }
}


fun status(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["status"] = {}
        render != null -> renderMap["status"] = {
               render(state)
        }
    }
}


fun sysDict(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["sysDict"] = {}
        render != null -> renderMap["sysDict"] = {
               render(state)
        }
    }
}


fun dictId(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["dictId"] = {}
        render != null -> renderMap["dictId"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictItemIso>) -> Unit)? = null
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
object SysDictItemFormProps {
const val itemText = "itemText"
const val itemValue = "itemValue"
const val description = "description"
const val sortOrder = "sortOrder"
const val status = "status"
const val sysDict = "sysDict"
const val dictId = "dictId"
}
@Composable
fun rememberSysDictItemFormState(current:SysDictItemIso?=null): MutableState<SysDictItemIso> {
    return remember (current){ mutableStateOf(current?: SysDictItemIso ()) }
}
        @Composable
        fun SysDictItemForm(
        state: MutableState<SysDictItemIso>,
   visible: Boolean,
            title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
     dslConfig: SysDictItemFormDsl.() -> Unit = {}
        
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysDictItemFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysDictItemFormProps.itemText to {        AddTextField(
            value = state.value.itemText?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(itemText  =if (it.isBlank())   ""  else it .parseObjectByKtx())
            },
     label = "字典项文本" ,
)
 }
        ,
            SysDictItemFormProps.itemValue to {        AddTextField(
            value = state.value.itemValue?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(itemValue  =if (it.isBlank())   ""  else it .parseObjectByKtx())
            },
     label = "字典项值" ,
)
 }
        ,
            SysDictItemFormProps.description to {        AddTextField(
            value = state.value.description?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(description  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "描述" ,
)
 }
        ,
            SysDictItemFormProps.sortOrder to {        AddTextField(
            value = state.value.sortOrder?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(sortOrder  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "排序" ,
)
 }
        ,
            SysDictItemFormProps.status to {        AddTextField(
            value = state.value.status?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(status  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "状态（1启用0不启用）" ,
)
 }
        ,
            SysDictItemFormProps.sysDict to {        AddTextField(
            value = state.value.sysDict?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(sysDict  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "sysDict" ,
)
 }
        ,
            SysDictItemFormProps.dictId to {        AddTextField(
            value = state.value.dictId?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(dictId  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "dictId" ,
)
 }
         
 ) 
       
          val finalItems = remember(renderMap) {
        defaultRenderMap
            .filterKeys { it !in renderMap } // 未被DSL覆盖的字段
            .plus(renderMap.filterValues { it != {} }) // 添加非隐藏的自定义字段
    }.values.toList() 
       
       
    val items = finalItems
 
        
           AddFormContainer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,

        ) {
            AddMultiColumnContainer(
                howMuchColumn = 2,
                items =items
            )
        }
 
        
        
        
        }