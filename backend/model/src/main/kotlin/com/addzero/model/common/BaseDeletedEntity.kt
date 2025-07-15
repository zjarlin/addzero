package com.addzero.model.common

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.LogicalDeleted
import org.babyfish.jimmer.sql.MappedSuperclass

@MappedSuperclass
interface BaseDeletedEntity {
    @LogicalDeleted("1")
    @Column(name = "deleted")
    val deleted: Int
}