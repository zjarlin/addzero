//package com.addzero.addzero_common
//
//import cn.hutool.core.io.FileUtil
//import addzero_common.versioncatlogutil.LibraryEntry
//import addzero_common.versioncatlogutil.PluginEntry
//import org.junit.jupiter.api.Test
//import org.tomlj.TomlParseResult
//import org.tomlj.TomlTable
//import java.nio.file.Path
//import java.nio.file.Paths
//
//class JvmToolTest() {
//
//
//    @Test
//    fun 测试解析tomlByOrg(): Unit {
//        val path = getToml()
//
//        val source: Path = Paths.get(path)
//        val result: TomlParseResult = org.tomlj.Toml.parse(source)
//
//
//        val versions = result.getTable("versions")?.toMap()
//        val toMap = result.getTable("plugins")?.toMap()
//        val plugins = recurMap(toMap)
//        val bundles = result.getTable("bundles")?.toMap()
//        val libraries = result.getTable("libraries")?.toMap()
////        VersionCatalogDTO(
////            versions = buildVersions(versions),
////            libraries = buildLibraries(libraries),
////            plugins = buildPlugins(plugins),
////            bundles = buildBundles(bundles),
////        )
////
////        result.getTable("a. dotted . key")
////        result.errors().forEach({ error -> System.err.println(error.toString()) })
//
//
//    }
//
//    private fun recurMap(map: Map<String?, Any?>?): Map<String?, Map.Entry<String?, Any?>>? {
//        val associateBy = map?.map {
//            val key = it.key
//            val columnValue = it.columnValue
//            if (columnValue is TomlTable) {
//                val toMap = columnValue.toMap()
//                val mapOf = mapOf(key to toMap)
//                recurMap(mapOf)
//            }
//            it
//        }?.associateBy { it.key }
//
//        return associateBy
//    }
//
//    private fun getToml(): String {
//        val exist = FileUtil.exist(path)
//        if (!exist) {
//            val writeUtf8String1 = FileUtil.writeUtf8String(
//                """
//           [versions]
//    #kotlin = "2.1.0"
//    [libraries]
//
//    [plugins]
//    [bundles]
//            """, path
//            )
//            val writeUtf8String = writeUtf8String1
//
//        }
//
//        val readUtf8String = FileUtil.readUtf8String(path)
//        return path
//    }
//
//    private fun buildBundles(map: MutableMap<String, Any>?): Map<String, List<String>> {
//        TODO("Not yet implemented")
//    }
//
//    private fun buildPlugins(map: MutableMap<String, Any>?): Map<String, PluginEntry> {
//        TODO("Not yet implemented")
//    }
//
//    private fun buildLibraries(map: MutableMap<String, Any>?): Map<String, LibraryEntry> {
//        TODO("Not yet implemented")
//    }
//
//    private fun buildVersions(map: MutableMap<String, Any>?): Map<String, String> {
//        TODO("Not yet implemented")
//    }
//
//
//}
