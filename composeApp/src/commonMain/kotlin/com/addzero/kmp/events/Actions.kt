package com.addzero.kmp.events

sealed class Actions {
    data class Login(val username: String, val password: String) : Actions()
    data class LogOut(val username: String) : Actions()
}