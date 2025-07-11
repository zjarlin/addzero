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
        class BizTagFormDsl(
            val state: MutableState<BizTagIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun name(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["name"] = {}
        render != null -> renderMap["name"] = {
               render(state)
        }
    }
}


fun description(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["description"] = {}
        render != null -> renderMap["description"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizTagIso>) -> Unit)? = null
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
object BizTagFormProps {
const val name = "name"
const val description = "description"
}
@Composable
fun rememberBizTagFormState(current:BizTagIso?=null): MutableState<BizTagIso> {
    return remember (current){ mutableStateOf(current?: BizTagIso ()) }
}
     @Composable
     fun BizTagForm(
     state: MutableState<BizTagIso>,
visible: Boolean,
         title: String,
 onClose: () -> Unit,
 onSubmit: () -> Unit,
 confirmEnabled: Boolean = true,
  dslConfig: BizTagFormDsl.() -> Unit = {}
     
     ) {
     

     
        AddDrawer(
     visible = visible,
     title = title,
     onClose = onClose,
     onSubmit = onSubmit,
     confirmEnabled = confirmEnabled,

     ) {
           BizTagFormOriginal(
         state, dslConfig,
     ) 
     }
     }

              @Composable
        fun BizTagFormOriginal(
        state: MutableState<BizTagIso>,
     dslConfig: BizTagFormDsl.() -> Unit = {}
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    BizTagFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            BizTagFormProps.name to { AddTextField(
    value = state.value.name?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(name = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "标签名称",
    isRequired = true
) }
        ,
            BizTagFormProps.description to { AddTextField(
    value = state.value.description?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(description = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "标签描述",
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
 
        
        