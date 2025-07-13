package com.addzero.kmp.form_mapping

import com.addzero.kmp.api.ApiProvider
import com.addzero.kmp.api.SysDictItemApi
import com.addzero.kmp.isomorphic.SysDeptIso
import com.addzero.kmp.isomorphic.SysDictIso
import com.addzero.kmp.isomorphic.SysDictItemIso
import com.addzero.kmp.isomorphic.SysRoleIso
import com.addzero.kmp.isomorphic.SysUserIso


object Iso2DataProvider {
    val isoToDataProvider = mapOf(
        SysDeptIso::class to { ApiProvider.sysDeptApi::tree },
        SysDictIso::class to { ApiProvider.sysDictApi::tree },
        SysDictItemIso::class to { ApiProvider.sysDictItemApi::tree },
        SysRoleIso::class to { ApiProvider.sysRoleApi::tree },
        SysUserIso::class to { ApiProvider.sysUserApi::tree }
    )
}
