//package com.addzero.kmp.demo
//
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import com.addzero.kmp.annotation.Route
//import com.addzero.kmp.core.ext.toJsonByKtx
//import com.addzero.kmp.isomorphic.SysUserIso
//import com.addzero.kmp.viewmodel.CrudViewModel
//import org.koin.compose.viewmodel.koinViewModel
//
//@Composable
//@Route
//fun balabala() {
//
//    val koinViewModel = koinViewModel<CrudViewModel<SysUserIso>>()
//    koinViewModel.set(SysUserIso(id = 1111))
//    val get = koinViewModel.get()
//    Text(get.toJsonByKtx())
//}
