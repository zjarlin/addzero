package com.addzero.ai.mcp.mavenapi

import com.fasterxml.jackson.annotation.JsonProperty

data class MavenCentralResponse(
    @JsonProperty("response") val response: Response
) {
    data class Response(
        @JsonProperty("docs") val docs: List<Doc>
    ) {
        data class Doc(
            @JsonProperty("g") val groupId: String,
            @JsonProperty("a") val artifactId: String,
            @JsonProperty("v") val version: String
        )
    }
}
