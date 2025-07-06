package com.addzero.web.infra.jimmer.base.low

data class Mypage<T>(
    val pageNo: Int,
    val pageSize: Int,
    val data: List<T>,
)