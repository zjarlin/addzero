package com.addzero.kmp.generated.forms.dataprovider

import com.addzero.kmp.generated.api.ApiProvider
import com.addzero.kmp.generated.isomorphic.SysDeptIso
import com.addzero.kmp.generated.isomorphic.SysUserIso
import com.addzero.kmp.generated.isomorphic.SysRoleIso
import com.addzero.kmp.generated.isomorphic.SysDictIso
import com.addzero.kmp.generated.isomorphic.BizDotfilesIso
import com.addzero.kmp.generated.isomorphic.SysDictItemIso


object Iso2DataProvider {
    val isoToDataProvider = mapOf(
        SysDeptIso::class to ApiProvider.sysDeptApi::tree,
        SysUserIso::class to ApiProvider.sysUserApi::tree,
        SysRoleIso::class to ApiProvider.sysRoleApi::tree,
        SysDictIso::class to ApiProvider.sysDictApi::tree,
        BizDotfilesIso::class to ApiProvider.bizDotfilesApi::tree,
        SysDictItemIso::class to ApiProvider.sysDictItemApi::tree
    )
}