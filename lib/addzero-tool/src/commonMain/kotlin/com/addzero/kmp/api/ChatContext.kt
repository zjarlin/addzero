package com.addzero.kmp.api

import com.addzero.kmp.isomorphic.SysUserIso
import kotlinx.serialization.Serializable

@Serializable
sealed class ChatContext

@Serializable
data class ChatRequest(val promt: String) : ChatContext()

@Serializable
data class 视觉Request(val promt: String, val images: List<String>) : ChatContext()


data class FileUploadResponse(
    val fileUrl: String,
    val progress: Float,
)


@Serializable
enum class CheckSignInput {
    PHONE, EMAIL, USERNAME
}

@Serializable
sealed class SignInStatus {
    @Serializable
    object None : SignInStatus()

    @Serializable
    data class Alredyregister(val loginContextAct: CheckSignInput, val sysUserIso: SysUserIso) : SignInStatus()

    @Serializable
    data class Notregister(val loginContextAct: CheckSignInput) : SignInStatus()
}

@Serializable
data class SecondLoginDTO(val userRegFormState: SysUserIso)

@Serializable
data class SecondLoginResponse(val sysUserIso: SysUserIso, val token: String)

