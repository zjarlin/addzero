package com.addzero.kmp.api.mavenapi

import com.addzero.kmp.api.mavenapi.MavenCentralApi.getLatestVersion
import com.addzero.kmp.api.mavenapi.entity.MavenRes
import com.addzero.kmp.network.apiClient
import io.ktor.client.call.*
import io.ktor.client.request.*

object MavenCentralApi {

    suspend fun getLatestVersion(groupId: String, artifactId: String): String {
        val response: MavenRes = apiClient.get("https://search.maven" +
                ".org/solrsearch/select") {
            url {
                parameters.apply {
                    append("q", "g:$groupId AND a:$artifactId")
                    append("rows", "1")
                    append("wt", "json")
                }
            }
        }.body()

        val doc = response.response.docs.first()

        val trimIndent = """
    implementation("${doc.id}:${doc.latestVersion}")
            
        """.trimIndent()
        return trimIndent
    }
}
suspend fun main() {

    try {
        val latestVersion = getLatestVersion("org.jetbrains.kotlinx", "kotlinx-datetime")
        println("Latest version: $latestVersion") // 输出示例: 0.6.0
    } catch (e: Exception) {
        println("Failed to fetch version: ${e.message}")
    }
}
