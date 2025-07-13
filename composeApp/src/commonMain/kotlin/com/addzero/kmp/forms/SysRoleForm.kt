            package com.addzero.kmp.forms
            import com.addzero.kmp.form_mapping.Iso2DataProvider.isoToDataProvider
 
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
     fun SysRoleForm(
     state: MutableState<SysRoleIso>,
visible: Boolean,
         title: String,
 onClose: () -> Unit,
 onSubmit: () -> Unit,
 confirmEnabled: Boolean = true,
  dslConfig: SysRoleFormDsl.() -> Unit = {}
     
     ) {
     

     
        AddDrawer(
     visible = visible,
     title = title,
     onClose = onClose,
     onSubmit = onSubmit,
     confirmEnabled = confirmEnabled,

     ) {
           SysRoleFormOriginal(
         state, dslConfig,
     ) 
     }
     }

              @Composable
        fun SysRoleFormOriginal(
        state: MutableState<SysRoleIso>,
     dslConfig: SysRoleFormDsl.() -> Unit = {}
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysRoleFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysRoleFormProps.roleCode to { AddTextField(
    value = state.value.roleCode?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(roleCode = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "角色编码",
    isRequired = true
) }
        ,
            SysRoleFormProps.roleName to { AddTextField(
    value = state.value.roleName?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(roleName = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "角色名称",
    isRequired = true
) }
        ,
            SysRoleFormProps.systemFlag to {     
Row(verticalAlignment = Alignment.CenterVertically) {
Text("是否为系统角色")
    Switch(
        checked = state.value.systemFlag ?: false,
        onCheckedChange = {
            state.value = state.value.copy(systemFlag = it)
        },
    )
    
    Text(
        text = if (  state.value as? Boolean == true) "是" else "否",
        modifier = Modifier.width(40.dp)
    )

}
     }
        ,
            SysRoleFormProps.status to { AddTextField(
    value = state.value.status?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(status = if (it.isBlank()) com.addzero.kmp.generated.enums.EnumSysToggle.entries.first() else it.parseObjectByKtx())
    },
    label = "角色状态",
    isRequired = true
) }
        ,
            SysRoleFormProps.sysUsers to { val sysUsersDataProvider = remember {
              val dataProviderFunction = isoToDataProvider[SysUserIso::class] ?: throw IllegalStateException("未找到 val sysUsers: List<SysUser> 的数据提供者，请在Iso2DataProvider注册")
             dataProviderFunction 
}

AddGenericSelector(
    value = state.value.sysUsers,
    onValueChange = { 
        state.value = state.value.copy(sysUsers = it as List<SysUserIso>)
    },
    dataProvider = {
        
           if (  sysUsersDataProvider == null) {
    emptyList()
} else {
      sysUsersDataProvider().invoke("") as List<SysUserIso>
} 
    },
    getId = {it.id!! },
    getLabel = { it.   email   },
    
    getChildren = { 
emptyList()
    },
       placeholder = "请选择"+"sysUsers", 
    allowClear = false,
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
 
        
        
 
            
        class SysRoleFormDsl(
            val state: MutableState<SysRoleIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun roleCode(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["roleCode"] = {}
        render != null -> renderMap["roleCode"] = {
               render(state)
        }
    }
}


fun roleName(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["roleName"] = {}
        render != null -> renderMap["roleName"] = {
               render(state)
        }
    }
}


fun systemFlag(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["systemFlag"] = {}
        render != null -> renderMap["systemFlag"] = {
               render(state)
        }
    }
}


fun status(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["status"] = {}
        render != null -> renderMap["status"] = {
               render(state)
        }
    }
}


fun sysUsers(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysRoleIso>) -> Unit)? = null
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
object SysRoleFormProps {
const val roleCode = "roleCode"
const val roleName = "roleName"
const val systemFlag = "systemFlag"
const val status = "status"
const val sysUsers = "sysUsers"
}
@Composable
fun rememberSysRoleFormState(current:SysRoleIso?=null): MutableState<SysRoleIso> {
    return remember (current){ mutableStateOf(current?: SysRoleIso ()) }
}