package com.addzero.addzero_common.versioncatlogutil

import cn.hutool.core.io.FileUtil
import org.tomlj.Toml
import org.tomlj.TomlArray
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val toTomlDTO = TomlUtils.toTomlDTO("/Users/zjarlin/Downloads/addzero_common/gradle/otherlibs/libs.versions.toml")
    val toTomlDTO1 = TomlUtils.toTomlDTO("/Users/zjarlin/Downloads/addzero_common/gradle/libs.versions.toml")
    val merge = TomlUtils.merge(toTomlDTO, toTomlDTO1)
    val toToml = merge.toToml()
    FileUtil.writeUtf8String(
        toToml,
        "/Users/zjarlin/Downloads/addzero_common/gradle/mergeedtoml/libs.versions.toml"
    )

}

fun VersionCatalogDTO.toToml(): String {
    val sections = mutableListOf<String>()

    if (!versions.isNullOrEmpty()) {
        sections.add(
            """
                [versions]
                ${versions.joinToString("\n") { "${it.versionRef} = \"${it.version}\"" }}
            """.trimIndent()
        )
    }

    if (!libraries.isNullOrEmpty()) {
        sections.add(
            """
                [libraries]
                ${
                libraries.joinToString("\n") { library ->
                    val versionPart = when {
                        library.version != null -> ", version = \"${library.version}\""
                        library.versionRef != null -> ", version.ref = \"${library.versionRef}\""
                        else -> ""
                    }
                    "${library.key} = { group = \"${library.group}\", name = \"${library.name}\"$versionPart }"
                }
            }
            """.trimIndent()
        )
    }

    if (!plugins.isNullOrEmpty()) {
        sections.add(
            """
                [plugins]
                ${
                plugins.joinToString("\n") { plugin ->
                    val versionPart = when {
                        plugin.version != null -> ", version = \"${plugin.version}\""
                        plugin.versionRef != null -> ", version.ref = \"${plugin.versionRef}\""
                        else -> ""
                    }
                    "${plugin.key} = { id = \"${plugin.id}\"$versionPart }"
                }
            }
            """.trimIndent()
        )
    }

    if (!bundles.isNullOrEmpty()) {
        sections.add(
            """
                [bundles]
                ${
                bundles.joinToString("\n") { bundle ->
                    val librariesStr = bundle.libraries.joinToString(", ") { "\"$it\"" }
                    "${bundle.key} = [$librariesStr]"
                }
            }
            """.trimIndent()
        )
    }

    return sections.joinToString("\n\n")
}

object TomlUtils {
    private fun buildVersions(map: Map<String, Any?>?): List<VersionEntry>? {
        val toList = map?.map {
            val toString = it.value.toString()
            VersionEntry(it.key, toString)
        }?.toList()
        return toList
    }

    private fun buildLibraries(map: Map<String, Any?>?): List<LibraryEntry>? {
        val toList = map?.map {
            val value = it.value
            val table = value as TomlTable
            val version = try {
                table.getString("version")
            } catch (e: Exception) {
                null
            }
            LibraryEntry(
                key = it.key,
                group = table.getString("group") ?: "",
                name = table.getString("name") ?: "",
                version = version,
                versionRef = table.getString("version.ref"),
            )

        }?.toList()
        return toList
    }

    private fun buildPlugins(map: Map<String, Any?>?): List<PluginEntry>? {
        val toList = map?.map {
            val value = it.value
            val table = value as TomlTable

            val version = try {
                table.getString("version")
            } catch (e: Exception) {
                null
            }

            val pluginEntry = PluginEntry(
                key = it.key,
                id = table.getString("id") ?: "",
                version = version,
                versionRef = table.getString("version.ref")
            )
            pluginEntry

        }?.toList()

        return toList

    }

    private fun buildBundles(map: Map<String, Any?>?): List<BundleEntry>? {
        val map2 = map?.map { (key, value) ->
            val array = value as TomlArray
            val toList = array.toList()
            val map1 = toList.map { it.toString() }
            BundleEntry(
                key, map1
            )
        }?.toList()

        return map2
    }

    private fun getToml(path: String): String {
        if (!FileUtil.exist(path)) {
            FileUtil.writeUtf8String(
                """
                [versions]
                kotlin = "2.1.0"

                [libraries]
                hutool = { group = "cn.hutool", name = "hutool-all", version.ref = "kotlin" }

                [plugins]
                kotlin = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }

                [bundles]
                spring = ["spring-boot", "spring-core"]
                """.trimIndent(), path
            )
        }
        return path
    }

    fun merge(vararg allDtos: VersionCatalogDTO): VersionCatalogDTO {
//        val allDtos = listOf(this) + dtos

        val mergedVersions = allDtos.flatMap { it.versions ?: emptyList() }.distinctBy { it.versionRef }

        val mergedLibraries =
            allDtos.flatMap { it.libraries ?: emptyList() }.distinctBy { it.key to it.group to it.name }

        val mergedPlugins = allDtos.flatMap { it.plugins ?: emptyList() }.distinctBy { it.id }

        val mergedBundles = allDtos.flatMap { it.bundles ?: emptyList() }.distinctBy { it.key }

        return VersionCatalogDTO(
            versions = mergedVersions, libraries = mergedLibraries, plugins = mergedPlugins, bundles = mergedBundles
        )
    }


    fun toTomlDTO(path1: String): VersionCatalogDTO {
        val path = getToml(path1)
        val source: Path = Paths.get(path)
        val result: TomlParseResult = Toml.parse(source)

        //        val toToml = table?.toToml()
        val versions = buildVersions(result.getTable("versions")?.toMap())
        val libraries = buildLibraries(result.getTable("libraries")?.toMap())
        val plugins = buildPlugins(result.getTable("plugins")?.toMap())
        val bundles = buildBundles(result.getTable("bundles")?.toMap())

        val catalogDTO = VersionCatalogDTO(
            versions = versions, libraries = libraries, plugins = plugins, bundles = bundles
        )
        return catalogDTO
    }

    /**
     * 在TOML文件指定标记后追加内容
     * @param content TOML文件内容
     * @param tag 标记（如 [plugins]）
     * @param appendText 要追加的内容
     * @return 处理后的字符串
     */
    fun appendAfterTag(content: String, tag: String, appendText: String): String {
        val normalizedTag = if (!tag.startsWith("[")) "[$tag" else tag
        val index = content.indexOf(normalizedTag)
        if (index == -1) return content

        // 找到标记后的换行符位置
        val nextLineIndex = content.indexOf('\n', index)
        if (nextLineIndex == -1) return content

        // 在标记后的换行处插入新内容
        return StringBuilder(content).insert(
            nextLineIndex + 1, appendText + "\n"
        ).toString()
    }


    fun merge(path: Array<String>): String {
        val reduce = path
//            .map { FileUtil.readUtf8String(it) }
            .map { toTomlDTO(it) }
            .reduce { acc, dto -> merge(acc, dto) }
        return reduce.toToml()
    }

}
