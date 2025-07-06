package com.addzero.web.infra.jimmer.base.low

import com.addzero.web.modules.sys_user.entity.SysUser
import org.babyfish.jimmer.sql.MappedSuperclass
import org.babyfish.jimmer.sql.OneToOne


@MappedSuperclass
interface BaseShenHeEntity {

    val approvalState: String?


    @OneToOne
    val sysUser: SysUser?

    val dataSids: String?
    val formSids: String?
    val formSidName: String?

}
