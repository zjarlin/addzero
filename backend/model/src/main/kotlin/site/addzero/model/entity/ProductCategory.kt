package site.addzero.model.entity.biz_device

import site.addzero.entity2form.annotation.LabelProp
import site.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 * 产品分类实体类，用于对产品进行归类，例如：机床
 */
@Entity
@Table(name = "product_category")
interface ProductCategory : BaseEntity {

    /**
     * 分类名称
     */
    @Key
    @LabelProp
    val name: String

    /**
     * 分类描述
     */
    val description: String?

    /**
     * 父分类
     */
    @ManyToOne
    val parent: ProductCategory?


    @OneToMany(mappedBy = "productCategory")
    val products: List<Product>

    /**
     * 排序
     */
    val sort: Int?

    /**
     * 是否启用
     */
    @Key
    val enabled: Boolean
}
