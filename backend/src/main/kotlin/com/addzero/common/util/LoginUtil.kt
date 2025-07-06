package com.addzero.common.util

import cn.dev33.satoken.stp.StpUtil

object LoginUtil {
    fun getLoginUserId(): Long {

        val loginId = try {
            StpUtil.getLoginIdAsLong()
        } catch (e: Exception) {
            //超管1
            return 1
        }
        return loginId
    }
}