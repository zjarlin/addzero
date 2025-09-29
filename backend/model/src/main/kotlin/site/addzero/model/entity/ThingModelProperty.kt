package site.addzero.model.entity.biz_device

import org.babyfish.jimmer.sql.*
import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity
import site.addzero.model.entity.ThingModel

/**
 * 物模型属性实体类，属于物模型的一部分
 */
@Entity
@Table(name = "thing_model_property")
interface ThingModelProperty : BaseEntity {

    /**
     * 所属物模型
     */
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)

    val thingModel: ThingModel?

    /**
     * 属性标识
     */
    @Key
    val identifier: String

    /**
     * 属性名称
     */
    @Key
    val name: String


    /**
     * 属性描述
     */
    @Key
    @LabelProp
    val description: String

    /**
     * 数据类型，例如：int32, float, double, string, bool, enum 等
     */
    @Key
    val dataType: String

    /**
     * 是否必填
     */
    @Default("true")
    val required: Boolean


    /**
     * 正常范围最小值
     */
    val minNormalValue: Double?

    /**
     * 正常范围最大值
     */
    val maxNormalValue: Double?


    /**
     * 正常范围最小值
     */
    val minWarningValue: Double?

    /**
     * 正常范围最大值
     */
    val maxWarningValue: Double?


    /**
     * 精度值设置，对数值类型有效
     */
    val dataPrecision: Int?

    /**
     * 读取方式：读、写、上报
     */
    @Key
    val accessMode: String

    /**
     * 排序
     */
    @Default("0")
    val sort: Int
}
