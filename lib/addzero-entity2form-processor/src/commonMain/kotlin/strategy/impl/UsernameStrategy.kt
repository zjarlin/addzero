package strategy.impl

import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.isRequired
import com.addzero.kmp.util.label
import com.addzero.kmp.util.name
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import strategy.FormStrategy

/**
 * URL策略
 */
object UsernameStrategy : FormStrategy {

    override val priority: Int = 12

    override fun support(prop: KSPropertyDeclaration): Boolean {
        val ktName = prop.name
        return ktName.contains("username", ignoreCase = true)
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue

//               AddTextField(
//            value = userRegFormState.username,
//            onValueChange = { viewModel.userRegFormState = userRegFormState.copy(username = it) },
//            label = "用户名",
//            regexEnum = RegexEnum.USERNAME,
//            leadingIcon = Icons.Default.PeopleAlt,
//            disable = checkSignInput == USERNAME,
//            onErrMsgChange = { input, msg -> errorMsgs.add(msg) },
//
//
//            remoteValidationConfig = RemoteValidationConfig(
//                tableName = "sys_user",
//                column = "username",
//            )
//
//
////            , errorMessages = errorMessages
//        )


        return """
            AddTextField(
                value = state.value.$name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy($name = if (it.isBlank()) $defaultValue else it.parseObjectByKtx())
                },
                label = $label,
                isRequired = $isRequired,
                regexEnum = RegexEnum.USERNAME,
                  leadingIcon = Icons.Default.PeopleAlt, 
                  //            disable = checkSignInput == USERNAME,

           remoteValidationConfig = RemoteValidationConfig(
      tableName = "sys_user",
      column = "username",
            ) 
            )
        """.trimIndent()
    }
}
