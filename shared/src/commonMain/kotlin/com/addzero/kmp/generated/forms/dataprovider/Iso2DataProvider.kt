package com.addzero.kmp.generated.forms.dataprovider

import com.addzero.kmp.generated.api.ApiProvider
import com.addzero.kmp.generated.isomorphic.SysDictIso
import com.addzero.kmp.generated.isomorphic.SysDictItemIso
import com.addzero.kmp.generated.isomorphic.SysDeptIso
import com.addzero.kmp.generated.isomorphic.SysUserIso
import com.addzero.kmp.generated.isomorphic.BizDotfilesIso
import com.addzero.kmp.generated.isomorphic.SysRoleIso


object Iso2DataProvider {
    val isoToDataProvider = mapOf(
        SysDictIso::class to ApiProvider.sysDictApi::tree,
        SysDictItemIso::class to ApiProvider.sysDictItemApi::tree,
        SysDeptIso::class to ApiProvider.sysDeptApi::tree,
        SysUserIso::class to ApiProvider.sysUserApi::tree,
        BizDotfilesIso::class to ApiProvider.bizDotfilesApi::tree,
        SysRoleIso::class to ApiProvider.sysRoleApi::tree
    )
}