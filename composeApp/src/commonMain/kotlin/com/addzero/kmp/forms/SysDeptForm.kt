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
        class SysDeptFormDsl(
            val state: MutableState<SysDeptIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun name(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["name"] = {}
        render != null -> renderMap["name"] = {
               render(state)
        }
    }
}


fun parent(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["parent"] = {}
        render != null -> renderMap["parent"] = {
               render(state)
        }
    }
}


fun children(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["children"] = {}
        render != null -> renderMap["children"] = {
               render(state)
        }
    }
}


fun sysUsers(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["sysUsers"] = {}
        render != null -> renderMap["sysUsers"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysDeptIso>) -> Unit)? = null
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
object SysDeptFormProps {
const val name = "name"
const val parent = "parent"
const val children = "children"
const val sysUsers = "sysUsers"
}
@Composable
fun rememberSysDeptFormState(current:SysDeptIso?=null): MutableState<SysDeptIso> {
    return remember (current){ mutableStateOf(current?: SysDeptIso ()) }
}
        @Composable
        fun SysDeptForm(
        state: MutableState<SysDeptIso>,
   visible: Boolean,
            title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
     dslConfig: SysDeptFormDsl.() -> Unit = {}
        
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysDeptFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysDeptFormProps.name to { AddTextField(
    value = state.value.name?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(name = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "部门名称",
    isRequired = true
) }
        ,
            SysDeptFormProps.parent to { AddTextField(
    value = state.value.parent?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(parent = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "parent",
    isRequired = false
) }
        ,
            SysDeptFormProps.children to { AddTextField(
    value = state.value.children?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(children = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "children",
    isRequired = true
) }
        ,
            SysDeptFormProps.sysUsers to { AddTextField(
    value = state.value.sysUsers?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(sysUsers = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "部门用户",
    isRequired = true
) }
         
 ) 
       
          val finalItems = remember(renderMap) {
        defaultRenderMap
            .filterKeys { it !in renderMap } // 未被DSL覆盖的字段
            .plus(renderMap.filterValues { it != {} }) // 添加非隐藏的自定义字段
    }.values.toList() 
       
       
    val items = finalItems
 
        
           AddDrawer(
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