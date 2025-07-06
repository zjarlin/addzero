package com.addzero.common.util.data_structure.spo.util

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil

object SpoFileReader {
    fun readTab(filePath: String?): List<Map<String, String>> {
        // 读取文件所有行
        val lines = FileUtil.readUtf8Lines(filePath)
        return readTabByLines(lines)
    }

    private fun readTabByLines(lines: List<String>): List<Map<String, String>> {
        if (lines.isEmpty()) {
            return emptyList()
        }
        // 提取表头字段
        val headers = lines[0].split("\t").map { it.trim() }

        // 解析文件内容，映射为对象列表
        return lines.asSequence().drop(1) // 跳过表头
            .filter { StrUtil.isNotBlank(it) } // 过滤空行
            .map { line -> line.split("\t").map { it.trim() } } // 分割数据行
            .map { fields -> mapToMap(fields, headers) } // 将每行数据映射为 Map
            .toList()
    }

    // 将字段映射为 Map
    private fun mapToMap(fields: List<String>, headers: List<String>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        for (i in fields.indices) {
            if (i < headers.size) {
                map[headers[i]] = fields[i]
            }
        }
        return map
    }
}