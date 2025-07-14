//package com.addzero.kmp.form_mapping
//
//import com.addzero.kmp.generated.api.ApiProvider
//import com.addzero.kmp.generated.isomorphic.SysDeptIso
//import com.addzero.kmp.generated.isomorphic.SysDictIso
//import com.addzero.kmp.generated.isomorphic.SysDictItemIso
//import com.addzero.kmp.generated.isomorphic.SysRoleIso
//import com.addzero.kmp.generated.isomorphic.SysUserIso
//
//
//object Iso2DataProvider {
//    val isoToDataProvider = mapOf(
//        SysDeptIso::class to { ApiProvider.sysDeptApi::tree },
//        SysDictIso::class to { ApiProvider.sysDictApi::tree },
//        SysDictItemIso::class to { ApiProvider.sysDictItemApi::tree },
//        SysRoleIso::class to { ApiProvider.sysRoleApi::tree },
//        SysUserIso::class to { ApiProvider.sysUserApi::tree }
//    )
//}
