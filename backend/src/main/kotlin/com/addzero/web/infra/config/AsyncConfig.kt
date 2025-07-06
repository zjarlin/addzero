//package com.addzero.web.infra.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.core.task.AsyncTaskExecutor
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
//
//@Configuration
////@EnableConfigurationProperties(SecurityProperties::class)
//open class AsyncConfig : WebMvcConfigurer {
//
//    @Primary
//    @Bean(name = ["asyncTaskExecutor"])
//    open fun asyncTaskExecutor(): AsyncTaskExecutor {
//        val executor = ThreadPoolTaskExecutor()
//        executor.corePoolSize = 5 // 根据需求调整
//        executor.maxPoolSize = 10
//        executor.queueCapacity = 25
////        executor.threadNamePrefix = "MyApp-Async-"
//        executor.initialize()
//        return executor
//    }
//
//    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
//        configurer.setTaskExecutor(asyncTaskExecutor())
//    }
//
//}