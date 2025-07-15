package com.addzero.model.common

import cn.hutool.core.util.IdUtil
import org.babyfish.jimmer.sql.meta.UserIdGenerator

class SnowflakeIdGenerator : UserIdGenerator<Long> {
    override fun generate(entityType: Class<*>?): Long {
//        val snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr()
        val snowflakeNextId = IdUtil.getSnowflakeNextId()
        return snowflakeNextId
    }
}