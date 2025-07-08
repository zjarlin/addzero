            package com.addzero.kmp.forms
            import androidx.compose.material.icons.Icons
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
           import com.addzero.kmp.component.form.number.*
import com.addzero.kmp.component.form.date.*
 
            import androidx.compose.ui.Alignment
            import com.addzero.kmp.core.ext.parseObjectByKtx
            import com.addzero.kmp.isomorphic.*
        class BizDotfilesFormDsl(
            val state: MutableState<BizDotfilesIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun osType(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["osType"] = {}
        render != null -> renderMap["osType"] = {
               render(state)
        }
    }
}


fun osStructure(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["osStructure"] = {}
        render != null -> renderMap["osStructure"] = {
               render(state)
        }
    }
}


fun defType(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["defType"] = {}
        render != null -> renderMap["defType"] = {
               render(state)
        }
    }
}


fun name(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["name"] = {}
        render != null -> renderMap["name"] = {
               render(state)
        }
    }
}


fun value(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["value"] = {}
        render != null -> renderMap["value"] = {
               render(state)
        }
    }
}


fun describtion(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["describtion"] = {}
        render != null -> renderMap["describtion"] = {
               render(state)
        }
    }
}


fun status(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["status"] = {}
        render != null -> renderMap["status"] = {
               render(state)
        }
    }
}


fun fileUrl(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["fileUrl"] = {}
        render != null -> renderMap["fileUrl"] = {
               render(state)
        }
    }
}


fun location(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["location"] = {}
        render != null -> renderMap["location"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<BizDotfilesIso>) -> Unit)? = null
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
object BizDotfilesFormProps {
const val osType = "osType"
const val osStructure = "osStructure"
const val defType = "defType"
const val name = "name"
const val value = "value"
const val describtion = "describtion"
const val status = "status"
const val fileUrl = "fileUrl"
const val location = "location"
}
@Composable
fun rememberBizDotfilesFormState(current:BizDotfilesIso?=null): MutableState<BizDotfilesIso> {
    return remember (current){ mutableStateOf(current?: BizDotfilesIso ()) }
}
        @Composable
        fun BizDotfilesForm(
        state: MutableState<BizDotfilesIso>,
   visible: Boolean,
            title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
     dslConfig: BizDotfilesFormDsl.() -> Unit = {}
        
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    BizDotfilesFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            BizDotfilesFormProps.osType to { AddTextField(
    value = state.value.osType?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(osType = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "操作系统win=winlinux=linuxmac=macnull=不限",
    isRequired = true
) }
        ,
            BizDotfilesFormProps.osStructure to { AddTextField(
    value = state.value.osStructure?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(osStructure = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "系统架构arm64=arm64x86=x86不限=不限",
    isRequired = false
) }
        ,
            BizDotfilesFormProps.defType to { AddTextField(
    value = state.value.defType?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(defType = if (it.isBlank()) com.addzero.kmp.generated.enums.EnumShellDefType.entries.first() else it.parseObjectByKtx())
    },
    label = "定义类型alias=aliasexport=exportfunction=functionsh=shvar=var",
    isRequired = true
) }
        ,
            BizDotfilesFormProps.name to { AddTextField(
    value = state.value.name?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(name = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "名称",
    isRequired = true
) }
        ,
            BizDotfilesFormProps.value to { AddTextField(
    value = state.value.value?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(value = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "值",
    isRequired = true
) }
        ,
            BizDotfilesFormProps.describtion to { AddTextField(
    value = state.value.describtion?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(describtion = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "注释",
    isRequired = false
) }
        ,
            BizDotfilesFormProps.status to { AddTextField(
    value = state.value.status?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(status = if (it.isBlank()) com.addzero.kmp.generated.enums.EnumSysToggle.entries.first() else it.parseObjectByKtx())
    },
    label = "状态1=启用0=未启用",
    isRequired = true
) }
        ,
            BizDotfilesFormProps.fileUrl to { AddTextField(
    value = state.value.fileUrl?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(fileUrl = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "文件地址",
    isRequired = false,
    regexEnum = RegexEnum.URL
) }
        ,
            BizDotfilesFormProps.location to { AddTextField(
    value = state.value.location?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(location = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "文件位置",
    isRequired = false
) }
         
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