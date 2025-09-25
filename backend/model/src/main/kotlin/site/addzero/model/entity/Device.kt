package site.addzero.model.entity.biz_device

import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 * 设备实体类，基于产品创建
 */
@Entity
@Table(name = "device")
interface Device : BaseEntity {

    /**
     * 设备名称
     */
    @Key
    @LabelProp
    val name: String

    /**
     * 设备编码
     */
    @Key
    val code: String

    /**
     * 所属产品
     */
    @ManyToOne
    val product: Product?

    /**
     * 设备认证信息（设备可以单独设置）
     */
    val authInfo: String?

    /**
     * 是否启用
     */
    @Key
    val enabled: Boolean

    /**
     * 设备状态
     */
    val status: String?
}
