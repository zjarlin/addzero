package com.addzero.web.modules.sys_dict_item.entity

import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.modules.sys_dict.entity.SysDict
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Default
import org.babyfish.jimmer.sql.DissociateAction
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.IdView
import org.babyfish.jimmer.sql.JoinColumn
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.OnDissociate
import org.babyfish.jimmer.sql.Table

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
