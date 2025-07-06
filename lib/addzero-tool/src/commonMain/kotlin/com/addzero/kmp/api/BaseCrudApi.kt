package com.addzero.kmp.api

interface BaseCrudApi<T> {


    suspend fun edit(item: T): Boolean

    suspend fun delete(id: Any): Boolean
    suspend fun save(item: T): Boolean

}
