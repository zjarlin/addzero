//package com.addzero.web.infra.jimmer.cache
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.babyfish.jimmer.meta.ImmutableProp
//import org.babyfish.jimmer.meta.ImmutableType
//import org.babyfish.jimmer.spring.cache.RedisValueBinder
//import org.babyfish.jimmer.sql.cache.Cache
//import org.babyfish.jimmer.sql.cache.RemoteKeyPrefixProvider
//import org.babyfish.jimmer.sql.cache.chain.ChainCacheBuilder
//import org.babyfish.jimmer.sql.kt.cache.KCacheFactory
//import org.springframework.context.annotation.Bean
//import java.time.Duration
//
//@Bean
//fun cacheFactory(
//    connectionFactory: RedisConnectionFactory, // Redis连接工厂
//    objectMapper: ObjectMapper                // JSON序列化工具
//): KCacheFactory =                           // 返回缓存工厂实例
//    object : KCacheFactory {                 // 匿名实现KCacheFactory接口
//        override fun createObjectCache(type: ImmutableType): Cache<*, *>? {
//            // 创建对象缓存（存储实体对象）
//            return ChainCacheBuilder<Any, Any>().add(
//                RedisValueBinder.forObject<Any, Any>(type)          // 绑定对象类型
//                    .objectMapper(objectMapper)         // 配置JSON序列化
//                    .duration(Duration.ofMinutes(10))   // 设置过期时间10分钟
//                    .redis(connectionFactory)           // 绑定Redis连接
//                    .keyPrefixProvider(A())              // 使用自定义键前缀
//                    .build()
//            ).build()
//        }
//
//        override fun createAssociatedIdListCache(prop: ImmutableProp): Cache<*, List<*>>? {
//            // 创建关联ID列表缓存（存储一对多关联的ID列表）
//            val add = ChainCacheBuilder<Any, List<*>>().add(
//                RedisValueBinder.forProp<Any, List<*>>(prop)        // 绑定属性类型
//                    .objectMapper(objectMapper).duration(Duration.ofMinutes(10))
//                    .redis(connectionFactory)
////                    .keyPrefixProvider(A()).build()
//            )
//            return add.build()
//        }
//
//        override fun createAssociatedIdCache(prop: ImmutableProp): Cache<*, *>? {
//            // 创建关联ID缓存（存储一对一关联的ID）
//            return ChainCacheBuilder<Any, Any>().add(
//                RedisValueBinder.forProp<Any, Any>(prop)            // 绑定属性类型
//                    .objectMapper(objectMapper).duration(Duration.ofMinutes(10)).redis(connectionFactory).keyPrefixProvider(A()).build()
//            ).build()
//        }
//    }
//
//class A : RemoteKeyPrefixProvider {    // 自定义Redis键前缀提供器
//    override fun propKeyPrefix(prop: ImmutableProp?): String? {
//        // 属性键前缀：添加前缀"1"
//        return "1" + super.propKeyPrefix(prop)
//    }
//
//    override fun typeKeyPrefix(type: ImmutableType?): String? {
//        // 类型键前缀：添加前缀"2"
//        return "2" + super.typeKeyPrefix(type)
//    }
//}
