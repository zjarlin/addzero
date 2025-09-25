package site.addzero.model.entity.biz_device

import org.babyfish.jimmer.sql.*
import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity

/**
 * 物模型实体类，与产品一对一关联
 */
@Entity
@Table(name = "thing_model")
interface ThingModel : BaseEntity {

    /**
     * 所属产品
     * 与产品一对一关联
     */
    @OneToOne
    val product: Product?

    /**
     * 模型名称
     */
    @Key
    @LabelProp
    val name: String

    /**
     * 模型描述
     */
    val description: String?


    /**
     * 模型标识
     */
    val identifier: String?

    /**
     * 状态
     */
    @Default("1")
    val status: Int

    @OneToMany(mappedBy = "thingModel")
    val properties: List<ThingModelProperty>

}
