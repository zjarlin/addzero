//package com.addzero.kmp.viewmodel
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import org.koin.android.annotation.KoinViewModel
//import org.koin.compose.viewmodel.koinViewModel
//import org.koin.core.parameter.parametersOf
//import kotlin.reflect.KClass
//
//
//@KoinViewModel
//class CrudViewModel<T>(clazz: KClass<*>) : ViewModel() {
//    var t by mutableStateOf(null as T?)
//
//    fun set(t: T): Unit {
//        this.t = t
//    }
//
//    /**
//     * 获取表单状态
//     */
//    fun get(): T? {
//        return t
//    }
//
//}
//
