            package com.addzero.kmp.forms
            import androidx.compose.material.icons.Icons
            import androidx.compose.foundation.layout.*
            import androidx.compose.material3.*
            import androidx.compose.runtime.*
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.unit.dp
            import com.addzero.kmp.component.high_level.AddMultiColumnContainer
                       import com.addzero.kmp.component.drawer.AddDrawer
 
            import com.addzero.kmp.component.high_level.AddFormContainer
            import com.addzero.kmp.enums.RegexEnum
            import androidx.compose.material.icons.filled.*
            import com.addzero.kmp.component.form.*
           import com.addzero.kmp.component.form.number.*
import com.addzero.kmp.component.form.date.*
 
            import androidx.compose.ui.Alignment
            import com.addzero.kmp.core.ext.parseObjectByKtx
            import com.addzero.kmp.isomorphic.*
        class SysAreaFormDsl(
            val state: MutableState<SysAreaIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysAreaIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["id"] = {}
        render != null -> renderMap["id"] = {
               render(state)
        }
    }
}


fun parentId(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysAreaIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["parentId"] = {}
        render != null -> renderMap["parentId"] = {
               render(state)
        }
    }
}


fun nodeType(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysAreaIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["nodeType"] = {}
        render != null -> renderMap["nodeType"] = {
               render(state)
        }
    }
}


fun name(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysAreaIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["name"] = {}
        render != null -> renderMap["name"] = {
               render(state)
        }
    }
}


fun areaCode(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysAreaIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["areaCode"] = {}
        render != null -> renderMap["areaCode"] = {
               render(state)
        }
    }
}


            
            fun hide(vararg fields: String) {
                fields.forEach { renderMap[it] = {} }
            }
        }
object SysAreaFormProps {
const val parentId = "parentId"
const val nodeType = "nodeType"
const val name = "name"
const val areaCode = "areaCode"
}
@Composable
fun rememberSysAreaFormState(current:SysAreaIso?=null): MutableState<SysAreaIso> {
    return remember (current){ mutableStateOf(current?: SysAreaIso ()) }
}
     @Composable
     fun SysAreaForm(
     state: MutableState<SysAreaIso>,
visible: Boolean,
         title: String,
 onClose: () -> Unit,
 onSubmit: () -> Unit,
 confirmEnabled: Boolean = true,
  dslConfig: SysAreaFormDsl.() -> Unit = {}
     
     ) {
     

     
        AddDrawer(
     visible = visible,
     title = title,
     onClose = onClose,
     onSubmit = onSubmit,
     confirmEnabled = confirmEnabled,

     ) {
           SysAreaFormOriginal(
         state, dslConfig,
     ) 
     }
     }

              @Composable
        fun SysAreaFormOriginal(
        state: MutableState<SysAreaIso>,
     dslConfig: SysAreaFormDsl.() -> Unit = {}
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysAreaFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysAreaFormProps.parentId to {          AddIntegerField(
    value = state.value.parentId?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(parentId = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "上级数据库列名:parent_id数据类型:int8可空:是",
    isRequired = false
) }
        ,
            SysAreaFormProps.nodeType to { AddTextField(
    value = state.value.nodeType?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(nodeType = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "1省,2市,3区数据库列名:node_type数据类型:text可空:是默认值:NULL::charactervarying",
    isRequired = false
) }
        ,
            SysAreaFormProps.name to { AddTextField(
    value = state.value.name?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(name = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "name数据库列名:name数据类型:text可空:是默认值:NULL::charactervarying",
    isRequired = false
) }
        ,
            SysAreaFormProps.areaCode to { AddTextField(
    value = state.value.areaCode?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(areaCode = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "区域编码数据库列名:area_code数据类型:text可空:是默认值:NULL::charactervarying",
    isRequired = false
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
 
        
        