package com.addzero.model.common

import cn.hutool.core.util.IdUtil
import org.babyfish.jimmer.sql.meta.UserIdGenerator

class SnowflakeIdStringGenerator : UserIdGenerator<String> {
    override fun generate(entityType: Class<*>?): String {
//        val snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr()
        val snowflakeNextId = IdUtil.getSnowflakeNextIdStr()
        return snowflakeNextId
    }
}