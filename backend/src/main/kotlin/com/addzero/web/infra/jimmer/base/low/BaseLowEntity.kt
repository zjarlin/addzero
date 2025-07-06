//package com.addzero.web.infra.jimmer.base.low
//
//import com.addzero.web.infra.jimmer.SnowflakeIdGenerator
//import com.addzero.web.infra.jimmer.base.BaseDeletedEntity
//import org.babyfish.jimmer.sql.*
//import java.time.LocalDateTime
//
//
//@MappedSuperclass
//interface BaseLowEntity :BaseDeletedEntity{
//    @Id
//    @Column(name = "SID")
//    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
//    val sid: String
//
//    /**
//     * 创建人
//     */
//    @Column(name = "CREATEUSER")
//    val createUser: String?
//
//    /**
//     * 创建日期
//     */
//    @Column(name = "CREATE_DATE")
//    val createDate: LocalDateTime?
//
//    /**
//     * 更新人
//     */
//    @Column(name = "UPDATEUSER")
//    val updateUser: String?
//
//    /**
//     * 更新日期
//     */
//    val updateDate: LocalDateTime?
//
//    /**
//     * 排序
//     */
//    @Column(name = "ORDERID")
//    val orderId: Int?
//
//    /**
//    数据来源
//     */
//    @Column(name = "DATA_SOURCE")
//    val dataSource: String?
//
//}