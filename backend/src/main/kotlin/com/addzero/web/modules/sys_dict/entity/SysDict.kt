package com.addzero.web.modules.sys_dict.entity

import com.addzero.kmp.entity2form.annotation.LabelProp
import com.addzero.web.infra.jimmer.base.BaseDeletedEntity
import com.addzero.web.infra.jimmer.base.baseentity.BaseEntity
import com.addzero.web.modules.sys_dict_item.entity.SysDictItem
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table

/**
 * <p>
 * sys_dict
 * </p>
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
//    @OnDissociate()

    val sysDictItems: List<SysDictItem>


}
