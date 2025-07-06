package com.addzero.addzero_common.versioncatlogutil

data class VersionCatalogDTO(
    val versions: List<VersionEntry>?,
    val libraries: List<LibraryEntry>?,
    val plugins: List<PluginEntry>?,
    val bundles: List<BundleEntry>?,
) {
}

data class LibraryEntry(
    val key: String,
    val group: String,
    val name: String,
    val version: String? = null,
    val versionRef: String? = null
)

data class PluginEntry(
    val key: String,
    val id: String,
    val version: String? = null,
    val versionRef: String? = null
)

data class VersionEntry(
    val versionRef: String,
    val version: String
)

data class BundleEntry(
    val key: String,
    val libraries: List<String>
)