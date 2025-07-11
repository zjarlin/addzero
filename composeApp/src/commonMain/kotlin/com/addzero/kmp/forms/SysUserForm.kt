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
     fun SysUserForm(
     state: MutableState<SysUserIso>,
visible: Boolean,
         title: String,
 onClose: () -> Unit,
 onSubmit: () -> Unit,
 confirmEnabled: Boolean = true,
  dslConfig: SysUserFormDsl.() -> Unit = {}
     
     ) {
     

     
        AddDrawer(
     visible = visible,
     title = title,
     onClose = onClose,
     onSubmit = onSubmit,
     confirmEnabled = confirmEnabled,

     ) {
           SysUserFormOriginal(
         state, dslConfig,
     ) 
     }
     }

              @Composable
        fun SysUserFormOriginal(
        state: MutableState<SysUserIso>,
     dslConfig: SysUserFormDsl.() -> Unit = {}
        ) {
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysUserFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysUserFormProps.price to { AddMoneyField(
    value = state.value.price?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(price = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "价格",
    isRequired = true,
    currency = "CNY"
) }
        ,
            SysUserFormProps.testInt to {          AddIntegerField(
    value = state.value.testInt?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(testInt = if (it.isBlank()) 0 else it.parseObjectByKtx())
    },
    label = "整数",
    isRequired = true
) }
        ,
            SysUserFormProps.phone to { AddTextField(
    value = state.value.phone?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(phone = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "手机号",
   leadingIcon = Icons.Default.Phone,
    isRequired = false,
    regexEnum = RegexEnum.PHONE
) }
        ,
        SysUserFormProps.email to { AddEmailField(
    value = state.value.email?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(email = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    showCheckEmail=false,
    label = "电子邮箱",
    isRequired = true,
) }
    ,
            SysUserFormProps.username to {       AddTextField(
          value = state.value.username?.toString() ?: "",
          onValueChange = {
              state.value = state.value.copy(username = if (it.isBlank()) "" else it.parseObjectByKtx())
          },
          label = "用户名",
          isRequired = true,
          regexEnum = RegexEnum.USERNAME,
            leadingIcon = Icons.Default.PeopleAlt, 
            //            disable = checkSignInput == USERNAME,

     remoteValidationConfig = RemoteValidationConfig(
tableName = "sys_user",
column = "username",
      ) 
      ) }
        ,
            SysUserFormProps.password to {       AddPasswordField(
    value = state.value.password?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(password = if (it.isBlank()) "" else it.parseObjectByKtx())
    },
    label = "密码",
    isRequired = true,
) }
        ,
            SysUserFormProps.avatar to { AddTextField(
    value = state.value.avatar?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(avatar = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "头像",
    isRequired = false
) }
        ,
            SysUserFormProps.nickname to { AddTextField(
    value = state.value.nickname?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(nickname = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "昵称",
    isRequired = false
) }
        ,
            SysUserFormProps.gender to { AddTextField(
    value = state.value.gender?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(gender = if (it.isBlank()) null else it.parseObjectByKtx())
    },
    label = "性别",
    isRequired = false
) }
        ,
            SysUserFormProps.depts to { AddDeptSelector(
    selectedDepts = state.value.depts ?: emptyList(),
    onValueChange = { selectedDepts ->
        state.value = state.value.copy(depts = selectedDepts)
    },
) }
        ,
            SysUserFormProps.roles to { AddTextField(
    value = state.value.roles?.toString() ?: "",
    onValueChange = {
        state.value = state.value.copy(roles = if (it.isBlank()) emptyList() else it.parseObjectByKtx())
    },
    label = "角色列表",
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
 
        
        
 
            
        class SysUserFormDsl(
            val state: MutableState<SysUserIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
            fun price(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["price"] = {}
        render != null -> renderMap["price"] = {
               render(state)
        }
    }
}


fun testInt(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["testInt"] = {}
        render != null -> renderMap["testInt"] = {
               render(state)
        }
    }
}


fun id(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["id"] = {}
        render != null -> renderMap["id"] = {
               render(state)
        }
    }
}


fun phone(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["phone"] = {}
        render != null -> renderMap["phone"] = {
               render(state)
        }
    }
}


fun email(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["email"] = {}
        render != null -> renderMap["email"] = {
               render(state)
        }
    }
}


fun username(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["username"] = {}
        render != null -> renderMap["username"] = {
               render(state)
        }
    }
}


fun password(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["password"] = {}
        render != null -> renderMap["password"] = {
               render(state)
        }
    }
}


fun avatar(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["avatar"] = {}
        render != null -> renderMap["avatar"] = {
               render(state)
        }
    }
}


fun nickname(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["nickname"] = {}
        render != null -> renderMap["nickname"] = {
               render(state)
        }
    }
}


fun gender(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["gender"] = {}
        render != null -> renderMap["gender"] = {
               render(state)
        }
    }
}


fun depts(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["depts"] = {}
        render != null -> renderMap["depts"] = {
               render(state)
        }
    }
}


fun roles(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
) {
    when {
        hidden -> renderMap["roles"] = {}
        render != null -> renderMap["roles"] = {
               render(state)
        }
    }
}


fun createTime(
    hidden: Boolean = false,
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
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
    render:  (@Composable ( MutableState<SysUserIso>) -> Unit)? = null
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
object SysUserFormProps {
const val price = "price"
const val testInt = "testInt"
const val phone = "phone"
const val email = "email"
const val username = "username"
const val password = "password"
const val avatar = "avatar"
const val nickname = "nickname"
const val gender = "gender"
const val depts = "depts"
const val roles = "roles"
}
@Composable
fun rememberSysUserFormState(current:SysUserIso?=null): MutableState<SysUserIso> {
    return remember (current){ mutableStateOf(current?: SysUserIso ()) }
}