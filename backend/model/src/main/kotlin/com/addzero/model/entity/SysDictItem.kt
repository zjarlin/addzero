package com.addzero.model.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 * <p>
 *  sys_dict_item

 * </p>
 *
 * @author zjarlin
 * @date 2024-09-16
 */
@Entity
@Table(name = "sys_dict_item")
interface SysDictItem : BaseEntity {


    /**
     *  字典项文本
     */
    @Column(name = "item_text")
    @Key
    @LabelProp
    val itemText: String

    /**
     *  字典项值
     */
    @Column(name = "item_value")
    @Key
    val itemValue: String

    /**
     *  描述
     */
    val description: String?

    /**
     *  排序
     */
    @Column(name = "sort_order")
    val sortOrder: Long?

    /**
     *  状态（1启用 0不启用）
     */
    @Default("1")
    val status: Long?

    @ManyToOne
    @JoinColumn(name = "dict_id")
    @Key
    @OnDissociate(DissociateAction.DELETE)
    val sysDict: SysDict?

//    @IdView("sysDict")
//    val dictId: Long?

}