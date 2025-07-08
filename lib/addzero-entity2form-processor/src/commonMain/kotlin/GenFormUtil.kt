
import com.addzero.kmp.util.JlStrUtil.makeSurroundWith
import com.addzero.kmp.util.defaultValue
import com.addzero.kmp.util.ktName
import com.addzero.kmp.util.removeAnyQuote
import com.addzero.kmp.util.toLowCamelCase
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 根据字段类型渲染不同模板
 * generateDifferentTypes
 */
fun KSPropertyDeclaration.generateDifferentTypes(): String = run {
    val prop = this
    val name = prop.simpleName.asString()
    val type = prop.type.resolve() // 属性的KSType
    val typeDecl = type.declaration // 属性类型的声明
//        val typeName = typeDecl.simpleName.asString() // 属性类型的简单名称
//        val qualifiedName = typeDecl.qualifiedName?.asString() // 属性类型的全限定名

    // 获取属性在同构体中的字符串表示（可能带Iso后缀）
//        val typeStr = getIsoTypeString(type, outputDir, generatedIsoClasses, packageName)

    // 生成默认值
//        val defaultValue = prop.defaultValue()
    // 判断是否需要导入对于form来说,递归可能这里会生成无用的iso

    val pdoc = (prop.docString ?: name).removeAnyQuote().makeSurroundWith("\"")
    when {
        //判断如果是数字类型

        prop.ktName().contains("phone", ignoreCase = true) -> {

            """
                  AddTextField(
          value = state.value.${name.toLowCamelCase()}?.toString() ?: "",
          onValueChange = {
                    state.value = state.value.copy(${name.toLowCamelCase()}  =if (it.isBlank())   ${prop.defaultValue()}  else it .parseObjectByKtx())
                    },
             label = $pdoc ,
            isRequired = false,
            regexValidator = RegexEnum.PHONE,
            leadingIcon = Icons.Default.Phone,
          //  remoteValidationConfig = RemoteValidationConfig(
           //     tableName = "sys_user",
           //     column = "phone",
           // )

//            , errorMessages = errorMessages
        )
 
            """.trimIndent()

        }


        prop.ktName().contains("email", ignoreCase = true) -> {
            """
                   AddEmailField(
          value = state.value.${name.toLowCamelCase()}?.toString() ?: "",
          onValueChange = {
                    state.value = state.value.copy(${name.toLowCamelCase()}  =if (it.isBlank())   ${prop.defaultValue()}  else it .parseObjectByKtx())
                    },
             label = $pdoc ,
          //  disable = false,
         //   showCheckEmail = true,
    //        remoteValidationConfig = RemoteValidationConfig(
       //         tableName = "sys_user",
       //         column = "email",
        //    )
        )
 
        """.trimIndent()


        }


        else -> {
            val trimIndent = """
               AddTextField(
                    value = state.value.${name.toLowCamelCase()}?.toString() ?: "",
          onValueChange = {
                    state.value = state.value.copy(${name.toLowCamelCase()}  =if (it.isBlank())   ${prop.defaultValue()}  else it .parseObjectByKtx())
                    },
             label = $pdoc ,
        )
 
    """.trimIndent()
            trimIndent

        }

    }


//    val trimIndent = """
//    { OutlinedTextField(
//                    value = state.value.${name.toLowCamelCase()}?.toString() ?: "",
//                    onValueChange = {
//                    state.value = state.value.copy(${name.toLowCamelCase()}  =if (it.isBlank())   ${prop.defaultValue()}  else it .parseObjectByKtx())
//                    },
//                    label = { Text($pdoc) },
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                ) },
//
//   """.trimIndent()
//    trimIndent
}
