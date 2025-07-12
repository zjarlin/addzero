package com.addzero.kmp.form_mapping

import com.addzero.kmp.api.ApiProvider.sysDeptApi
import com.addzero.kmp.api.ApiProvider.sysDictApi
import com.addzero.kmp.api.ApiProvider.sysDictItemApi
import com.addzero.kmp.isomorphic.SysDeptIso
import com.addzero.kmp.isomorphic.SysDictIso
import com.addzero.kmp.isomorphic.SysDictItemIso


object Iso2DataProvider {
    val isoToDataProvider
//    : Map<KClass<*>, suspend (String) ->  List<*>>
            =
        mapOf(
            SysDeptIso::class to { sysDeptApi::tree },
            SysDictIso::class to { sysDictApi::querydict },
//            SysDictItemIso::class to { sysDictItemApi::tree }
//        SysUserIso::class to { sysUserApi::page }
        )
}
