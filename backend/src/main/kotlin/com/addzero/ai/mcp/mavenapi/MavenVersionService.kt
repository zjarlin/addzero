package com.addzero.ai.mcp.mavenapi

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Service
class MavenVersionService(
    private val restTemplate: RestTemplate = RestTemplate()
) {
    suspend fun getLatestVersion(groupId: String, artifactId: String): String? =
        withContext(Dispatchers.IO) {
            val url = "https://search.maven.org/solrsearch/select?" +
                    "q=g:${groupId}+AND+a:${artifactId}&rows=1&wt=json"

            try {
                val response = restTemplate.getForObject<MavenCentralResponse>(url)
                response?.response?.docs?.firstOrNull()?.version
            } catch (e: Exception) {
                null // 处理网络或解析错误
            }
        }
}
