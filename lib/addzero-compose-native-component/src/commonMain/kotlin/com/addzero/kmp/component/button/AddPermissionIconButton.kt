package com.addzero.kmp.component.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.compose.icons.IconMap
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddPermissionIconButton(
    permissionCode: String,
    text: String,
    icon: String?,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {

        IconButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = IconMap[icon!!].vector, contentDescription = text
            )
        }


    }
) {
    val koinViewModel = koinViewModel<LoginViewModel>()
    val hasPermition = koinViewModel.hasPermission(permissionCode)
    if (!hasPermition) {
        return
    }
    AddIconButton(
        text = text, imageVector = IconMap[icon!!].vector, content = content, onClick = onClick
    )
    Spacer(modifier = Modifier.Companion.width(8.dp))

}
