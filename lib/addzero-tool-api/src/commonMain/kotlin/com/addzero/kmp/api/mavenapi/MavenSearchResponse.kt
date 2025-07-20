package com.addzero.kmp.api.mavenapi

import com.addzero.kmp.api.mavenapi.entity.MavenRes
import com.addzero.kmp.core.network.apiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

@Serializable
data class MavenSearchResponse(
    val responseHeader: ResponseHeader,
    val response: SearchResponse
) {
    @Serializable
    data class ResponseHeader(
        val status: Int,
        val QTime: Int,
        val params: Map<String, String>
    )

    @Serializable
    data class SearchResponse(
        val numFound: Int,
        val start: Int,
        val docs: List<ArtifactDoc>
    )

    @Serializable
    data class ArtifactDoc(
        val id: String,
        val g: String, // groupId
        val a: String, // artifactId
        val v: String?, // version
        val p: String?, // packaging
        val timestamp: Long?,
        val ec: List<String>? // extensions
    )
}


suspend fun main() {
    var likeDependy = likeDependy("datetime")

    println(likeDependy)
}

// 使用示例
suspend fun likeDependy(keyword: String): MavenRes {
    val client = apiClient
    var body = client.get("https://search.maven.org/solrsearch/select") {
        parameter("q", keyword)
        parameter("rows", 5)
        parameter("wt", "json")
    }.body<MavenRes>()
    return body
}