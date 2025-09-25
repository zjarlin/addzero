package site.addzero.model.entity

import org.babyfish.jimmer.sql.*
import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity

/**
 * 产品实体类，基于产品分类创建
 */
@Entity
@Table(name = "product")
interface Product : BaseEntity {

    /**
     * 产品名称
     */
    @Key
    @LabelProp
    val name: String

    /**
     * 产品编码
     */
    @Key
    val code: String

    /** 产品分类 */
    @ManyToOne
    @Key
    val productCategory: ProductCategory?

    /**
     * 关联的物模型
     */
    @OneToOne(mappedBy = "product")
    @Key
    val thingModel: ThingModel?

    /**
     * 关联的设备列表
     */
    @OneToMany(mappedBy = "product")
    val devices: List<Device>

    /**
     * 产品描述
     */
    val description: String?

    /**
     * 设备接入方式，例如：MQTT
     */
    @Key
    val accessMethod: String

    /**
     * 认证方式
     */
    @Key
    val authMethod: String

    /**
     * 是否启用
     */
    @Key
    val enabled: Boolean
}
