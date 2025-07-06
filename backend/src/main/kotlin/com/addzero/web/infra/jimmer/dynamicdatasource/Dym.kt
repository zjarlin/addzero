//package com.addzero.web.infra.jimmer.dynamicdatasource
//
//import cn.hutool.extra.spring.SpringUtil
//import com.addzero.web.infra.jimmer.dynamicdatasource.DynamicDataSourceConfig.Companion.registerTm
//import com.zaxxer.hikari.HikariDataSource
//import org.babyfish.jimmer.spring.SqlClients
//import org.babyfish.jimmer.spring.transaction.JimmerTransactionManager
//import org.springframework.beans.factory.support.BeanDefinitionRegistry
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
//import org.springframework.beans.factory.support.RootBeanDefinition
//import org.springframework.boot.context.properties.EnableConfigurationProperties
//import org.springframework.context.ApplicationContext
//import org.springframework.context.ApplicationContextAware
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.Ordered
//
//@Configuration
//@EnableConfigurationProperties(DynamicDatasourceProperties::class)
//open class KimmerDataSourceDefinition : BeanDefinitionRegistryPostProcessor, ApplicationContextAware, Ordered {
//
//    private lateinit var applicationContext: ApplicationContext
//
//    override fun setApplicationContext(applicationContext: ApplicationContext) {
//        this.applicationContext = applicationContext
//    }
//
//    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
//        val properties = applicationContext.getBean(DynamicDatasourceProperties::class.java)
//        registerTm(properties, registry)
//    }
//
//
//    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE
//}