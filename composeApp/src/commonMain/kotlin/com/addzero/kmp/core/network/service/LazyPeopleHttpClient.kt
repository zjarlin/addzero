package com.addzero.kmp.core.network.service

import com.addzero.kmp.core.network.apiClient
import com.lt.lazy_people_http.config.LazyPeopleHttpConfig

private val config = LazyPeopleHttpConfig(apiClient)
//创建请求接口的实现类
private val hf = HttpFunctions::class.createService(config)
