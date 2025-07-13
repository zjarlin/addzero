package com.addzero.kmp.form_mapping

import com.addzero.kmp.api.ApiProvider
import com.addzero.kmp.api.ApiProvider.sysDeptApi
import com.addzero.kmp.api.ApiProvider.sysDictApi
import com.addzero.kmp.api.SysDictItemApi
// import com.addzero.kmp.api.ApiProvider.sysDictItemApi  // 暂时注释掉，等待重新生成
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
            SysDictItemIso::class to { ApiProvider.sysDictItemApi::tree }  // 暂时注释掉，等待重新生成
//        SysUserIso::class to { sysUserApi::page }
        )
}
