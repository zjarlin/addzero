package com.addzero.model.common

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface BaseDateTime {
    //    @JoinColumn(foreignKeyType =ForeignKeyType.FAKE)
    val createTime: LocalDateTime

    //    @JoinColumn(foreignKeyType =ForeignKeyType.FAKE)
    val updateTime: LocalDateTime?

}