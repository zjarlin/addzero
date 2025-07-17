package com.addzero.ai.mcp

import com.addzero.kmp.api.mavenapi.MavenCentralApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class MavenService {
    @Tool(description = "获取依赖最新版本")
    fun getLatestVersions(groupId: String, artifactId: String): String {
        // 使用 runBlocking 将挂起函数转换为阻塞调用
        return runBlocking {
            // 在 IO 调度器上执行网络请求
            MavenCentralApi.getLatestVersion(groupId, artifactId)
        }
    }
}