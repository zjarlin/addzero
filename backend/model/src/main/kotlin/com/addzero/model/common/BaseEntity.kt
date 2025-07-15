package com.addzero.model.common

import com.addzero.model.entity.SysUser
import org.babyfish.jimmer.sql.DissociateAction
import org.babyfish.jimmer.sql.ForeignKeyType
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.JoinColumn
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.MappedSuperclass
import org.babyfish.jimmer.sql.OnDissociate

@MappedSuperclass
interface BaseEntity : BaseDateTime {
    @Id
    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
    val id: Long

    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    @JoinColumn(foreignKeyType = ForeignKeyType.FAKE, name = "update_by")
    val updateBy: SysUser?

    @ManyToOne
    @JoinColumn(foreignKeyType = ForeignKeyType.FAKE, name = "create_by")
    @OnDissociate(DissociateAction.DELETE)
    val createBy: SysUser?
}