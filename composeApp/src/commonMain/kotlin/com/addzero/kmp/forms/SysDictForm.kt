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
     fun SysDictForm(
     state: MutableState<SysDictIso>,
visible: Boolean,
         title: String,
 onClose: () -> Unit,
 onSubmit: () -> Unit,
 confirmEnabled: Boolean = true,
  dslConfig: SysDictFormDsl.() -> Unit = {}
     
     ) {
     

     
        AddDrawer(
     visible = visible,
     title = title,
     onClose = onClose,
     onSubmit = onSubmit,
     confirmEnabled = confirmEnabled,

     ) {
           SysDictFormOriginal(
         state, dslConfig,
     ) 
     }
     }

              @Composable
        fun SysDictFormOriginal(
        state: MutableState<SysDictIso>,
     dslConfig: SysDictFormDsl.() -> Unit = {}
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysDictFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysDictFormProps.dictName to { AddTextField(
    value = state.value.dictName?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(dictName = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "字典名称",
    isRequired = true
) }
        ,
            SysDictFormProps.dictCode to { AddTextField(
    value = state.value.dictCode?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(dictCode = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "字典编码",
    isRequired = true
) }
        ,
            SysDictFormProps.description to { AddTextField(
    value = state.value.description?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(description = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "描述",
    isRequired = false
) }
        ,
            SysDictFormProps.sysDictItems to { AddTextField(
    value = state.value.sysDictItems?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(sysDictItems = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "sysDictItems",
    isRequired = true
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
 
        
        
 
            
        class SysDictFormDsl(
            val state: MutableState<SysDictIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun dictName(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["dictName"] = {}
        render != null -> renderMap["dictName"] = {
               render(state)
        }
    }
}


fun dictCode(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["dictCode"] = {}
        render != null -> renderMap["dictCode"] = {
               render(state)
        }
    }
}


fun description(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["description"] = {}
        render != null -> renderMap["description"] = {
               render(state)
        }
    }
}


fun sysDictItems(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["sysDictItems"] = {}
        render != null -> renderMap["sysDictItems"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["updateTime"] = {}
        render != null -> renderMap["updateTime"] = {
               render(state)
        }
    }
}


fun deleted(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDictIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["deleted"] = {}
        render != null -> renderMap["deleted"] = {
               render(state)
        }
    }
}


            
            fun hide(vararg fields: String) {
                fields.forEach { renderMap[it] = {} }
            }
        }
object SysDictFormProps {
const val dictName = "dictName"
const val dictCode = "dictCode"
const val description = "description"
const val sysDictItems = "sysDictItems"
}
@Composable
fun rememberSysDictFormState(current:SysDictIso?=null): MutableState<SysDictIso> {
    return remember (current){ mutableStateOf(current?: SysDictIso ()) }
}