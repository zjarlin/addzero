package com.addzero.ai.mcp.mavenapi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController(
    private val mavenVersionService: MavenVersionService
) {
    @GetMapping("/latest-version")
    suspend fun getLatestVersion(
        @RequestParam groupId: String,
        @RequestParam artifactId: String
    ): Map<String, String?> {
        val version = mavenVersionService.getLatestVersion(groupId, artifactId)
        return mapOf("latestVersion" to version)
    }
}
