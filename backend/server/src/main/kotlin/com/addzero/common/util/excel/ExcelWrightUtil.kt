package com.addzero.common.util.excel

import cn.hutool.core.io.file.PathUtil
import cn.hutool.core.util.IdUtil
import cn.idev.excel.EasyExcel
import java.io.File
import java.util.stream.Collectors
import java.util.stream.Stream

object ExcelWrightUtil {
    fun exportMap(data: MutableList<MutableMap<String, Any>>): File {
        val header = data.stream()
            .flatMap<String?> { e -> e!!.keys.stream() }
            .distinct()
            .collect(Collectors.toList())

        return exportMap(data, header)
    }

    fun exportMap(data: MutableList<MutableMap<String, Any>>, header: MutableList<String?>): File {
        return exportMap(data, header, null)
    }

    fun exportMap(data: MutableList<MutableMap<String, Any>>, header: MutableList<String?>, dir: File?): File {
        val excelHeader = header.stream()
            .map<MutableList<String?>> { e: String? -> Stream.of<String?>(e).collect(Collectors.toList()) }
            .collect(Collectors.toList())

        val excelData = data.stream()
            .map<MutableList<Any?>> { e ->
                header.stream()
                    .map<Any?> { col: String? ->
                        val value = e!!.getOrDefault(col, "")
                        when (value) {
                            is List<*> -> value.joinToString(",") { it?.toString() ?: "" }
                            else -> value
                        }
                    }
                    .collect(Collectors.toList())
            }
            .collect(Collectors.toList())

        val tempFile =
            PathUtil.createTempFile(IdUtil.getSnowflakeNextIdStr(), ".xlsx", if (dir != null) dir.toPath() else null)
                .toFile().getCanonicalFile()

        EasyExcel.write(tempFile)
            .head(excelHeader)
            .sheet("Sheet1")
//            .registerWriteHandler(ExcelWidthStyleStrategy())
//            .registerWriteHandler(CustomCellStyleStrategy())
            .doWrite(excelData)
        return tempFile
    }

}

