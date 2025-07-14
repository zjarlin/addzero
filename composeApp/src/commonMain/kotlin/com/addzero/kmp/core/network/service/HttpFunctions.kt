//package com.addzero.kmp.core.network.service
//
//import com.addzero.kmp.generated.api.BaseCrudApi
//import com.addzero.kmp.generated.isomorphic.SysUserIso
//import com.lt.lazy_people_http.annotations.LazyPeopleHttpService
//
//@LazyPeopleHttpService
//interface HttpFunctions : BaseCrudApi<SysUserIso> {
//    //标准post请求声明
//    @com.lt.lazy_people_http.annotations.POST("post/postB")
//    suspend fun postB(@com.lt.lazy_people_http.annotations.Field("name") t: String)
//
//    //suspend post请求声明
//    suspend fun post_postA(t: String): String
//
//}
