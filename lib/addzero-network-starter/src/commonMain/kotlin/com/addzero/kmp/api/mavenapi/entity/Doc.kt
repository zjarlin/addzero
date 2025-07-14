package com.addzero.kmp.generated.api.mavenapi.entity

import kotlinx.serialization.Serializable

@Serializable

data class Doc(
    val a: String,
    val ec: List<String>,
    val g: String,
    val id: String,
    val latestVersion: String,
    val p: String,
    val repositoryId: String,
    val text: List<String>,
    val timestamp: Long,
    val versionCount: Int
)
