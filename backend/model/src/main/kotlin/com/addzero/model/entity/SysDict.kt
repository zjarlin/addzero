package com.addzero.model.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.model.common.BaseDeletedEntity
import com.addzero.model.common.BaseEntity
import org.babyfish.jimmer.sql.*

/**
 *字典
 * @author zjarlin
 * @date 2024/11/27
 * @constructor 创建[SysDict]
 */
@Entity
@Table(name = "sys_dict")
interface SysDict : BaseEntity, BaseDeletedEntity {

    /**
     *  字典名称
     */
    @LabelProp
    @Column(name = "dict_name")
    val dictName: String

    /**
     *  字典编码
     */
    @Column(name = "dict_code")
    @Key(group = "dictCode")
    val dictCode: String

    /**
     *  描述
     */
    @Key(group = "description")
    @LabelProp
    val description: String?


    @OneToMany(mappedBy = "sysDict")
    @OnDissociate(DissociateAction.DELETE)
    val sysDictItems: List<SysDictItem>


}
