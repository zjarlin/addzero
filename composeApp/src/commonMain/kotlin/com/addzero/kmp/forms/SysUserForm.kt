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
        class SysUserFormDsl(
            val state: MutableState<SysUserIso>,
            private val renderMap: MutableMap<String, @Composable () -> Unit>
        ) {
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
        
           val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    SysUserFormDsl(state, renderMap).apply(dslConfig) 
        
        
                     val defaultRenderMap = mutableMapOf<String, @Composable () -> Unit>(
            SysUserFormProps.phone to {                        AddTextField(
                  AddEmailField(
          value = state.value.phone?.toString() ?: "",
          onValueChange = {
                    state.value = state.value.copy(phone  =if (it.isBlank())   null  else it .parseObjectByKtx())
                    },
             label = "手机号" ,
            isRequired = false,
            regexValidator = RegexEnum.PHONE,
            leadingIcon = Icons.Default.Phone,
          //  remoteValidationConfig = RemoteValidationConfig(
           //     tableName = "sys_user",
           //     column = "phone",
           // )

//            , errorMessages = errorMessages
        )
  }
        ,
            SysUserFormProps.email to {                AddEmailField(
      value = state.value.email?.toString() ?: "",
      onValueChange = {
                state.value = state.value.copy(email  =if (it.isBlank())   ""  else it .parseObjectByKtx())
                },
         label = "电子邮箱" ,
      //  disable = false,
     //   showCheckEmail = true,
//        remoteValidationConfig = RemoteValidationConfig(
   //         tableName = "sys_user",
   //         column = "email",
    //    )
    )
 }
        ,
            SysUserFormProps.username to {        AddTextField(
            value = state.value.username?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(username  =if (it.isBlank())   ""  else it .parseObjectByKtx())
            },
     label = "用户名" ,
)
 }
        ,
            SysUserFormProps.password to {        AddTextField(
            value = state.value.password?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(password  =if (it.isBlank())   ""  else it .parseObjectByKtx())
            },
     label = "密码" ,
)
 }
        ,
            SysUserFormProps.avatar to {        AddTextField(
            value = state.value.avatar?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(avatar  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "头像" ,
)
 }
        ,
            SysUserFormProps.nickname to {        AddTextField(
            value = state.value.nickname?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(nickname  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "昵称" ,
)
 }
        ,
            SysUserFormProps.gender to {        AddTextField(
            value = state.value.gender?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(gender  =if (it.isBlank())   null  else it .parseObjectByKtx())
            },
     label = "性别0：男1：女2：未知" ,
)
 }
        ,
            SysUserFormProps.depts to {        AddTextField(
            value = state.value.depts?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(depts  =if (it.isBlank())   emptyList()  else it .parseObjectByKtx())
            },
     label = "所属部门列表" ,
)
 }
        ,
            SysUserFormProps.roles to {        AddTextField(
            value = state.value.roles?.toString() ?: "",
  onValueChange = {
            state.value = state.value.copy(roles  =if (it.isBlank())   emptyList()  else it .parseObjectByKtx())
            },
     label = "角色列表" ,
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