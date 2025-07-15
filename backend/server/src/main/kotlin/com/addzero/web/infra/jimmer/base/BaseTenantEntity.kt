package com.addzero.web.infra.jimmer.base

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.MappedSuperclass


@MappedSuperclass
interface BaseTenantEntity {
    /**
     *  租户ID
     */
    @Column(name = "tenant_id")
    val tenantId: Int?

    /**
     *  低代码应用ID
     */
    @Column(name = "low_app_id")
    val lowAppId: String?
}