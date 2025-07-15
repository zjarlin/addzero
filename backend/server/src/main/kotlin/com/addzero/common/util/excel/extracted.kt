package com.addzero.common.util.excel

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.core.map.MapUtil
import cn.idev.excel.EasyExcel
import cn.idev.excel.enums.WriteDirectionEnum
import cn.idev.excel.write.metadata.fill.FillConfig
import com.addzero.common.kt_util.toNotBlankStr
import com.addzero.web.infra.jackson.parseObject
import com.addzero.web.infra.spring.SprCtxUtil
import jakarta.servlet.ServletOutputStream
import org.apache.commons.collections4.map.LinkedMap
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*


private fun extracted() {


//        ExcelUtil.exportMapList(gen,"/Users/zjarlin/Desktop/execExcel/施工日志汇总.xlsx")
//        EasyExcel.write(res("施工日志.xlsx")).withTemplate(file).build()


    val map = FileUtil.loopFiles("/Users/zjarlin/Downloads/AddzeroKmp/backend/src/test/kotlin/com/addzero/addzero_common/excel/json").map {

        val gen = gen(it.absolutePath)

        gen

    }


    val alldata  = map.map { it.first }

        .map {
            it.remove("气象数据")
            it.remove("出勤人数")
            it.remove("机械设备")
            it.remove("施工内容")
            val removeNullValue = MapUtil.removeNullValue(it)
            removeNullValue
        }

    .toMutableList()

    val exportMap = ExcelWrightUtil.exportMap(alldata)
    val absolutePath = exportMap.absolutePath
    println(absolutePath)


    map.forEach {
        val path = it.second!!
        val sheetData = it.first
        val file = FileUtil.getInputStream("/Users/zjarlin/Documents/模板表格.xlsx")

        val name = FileUtil.getName(path)
        val outFile = "/Users/zjarlin/Desktop/execExcel/${name}" + ".xlsx"
        val outputStream1 = FileUtil.getOutputStream(outFile)
        templateFill(sheetData, null, file, outputStream1)

    }

}

private fun gen(path: String): Pair<MutableMap<String, Any>, String?> {
    val readUtf8String = FileUtil.readUtf8String(path)
    val name = FileUtil.getName(path)
    val sheet1Data = readUtf8String.parseObject<Map<String, Any>>().toMutableMap()
    val get = sheet1Data.get("气象数据") as LinkedHashMap<String, Any>


    val get4 = get.get("白天") as LinkedHashMap<String, Any>
    val get6 = get4.get("天气状况") ?: ""
    val get61 = get4.get("风力") ?: ""
    val get62 = get4.get("风向") ?: ""
    val get63 = get4.get("温度") ?: get4.get("temperature") ?: ""
    sheet1Data.put("白天天气状况", get6.toNotBlankStr())
    sheet1Data.put("白天风力", get61.toNotBlankStr())
    sheet1Data.put("白天风向", get62.toNotBlankStr())
    sheet1Data.put("白天温度", get63.toNotBlankStr())

    val get5 = get.get("夜间") as Map<String, Any>
    // 处理夜间天气数据
    val get51 = get5.get("天气状况") ?: ""

    val get52 = get5.get("风力") ?: ""
    val get53 = get5.get("风向") ?: ""
    val get54 = get5.get("温度") ?: get5.get("temperature") ?: ""

    sheet1Data.put("夜间天气状况", get51.toNotBlankStr())
    sheet1Data.put("夜间风力", get52.toNotBlankStr())
    sheet1Data.put("夜间风向", get53.toNotBlankStr())
    sheet1Data.put("夜间温度", get54.toNotBlankStr())

    // 安全处理出勤人数
    val get1 = sheet1Data["出勤人数"] as? Map<String, Any>
    val joinToString = get1?.map { "${it.key}${it.value.toNotBlankStr()}" }?.joinToString(",") ?: ""
    sheet1Data.put("出勤人数合", joinToString.toNotBlankStr())

    // 安全处理机械设备
    val get2 = sheet1Data["机械设备"] as? Map<String, Any>
    val joinToString1 = get2?.map { "${it.key}${it.value.toNotBlankStr()}" }?.joinToString(",") ?: ""
    sheet1Data.put("机械设备合", joinToString1.toNotBlankStr())

    // 安全处理施工内容
    @Suppress("UNCHECKED_CAST")
    val get3 = sheet1Data["施工内容"] as? List<String> ?: emptyList()
    val joinToString2 = get3
        .filter { it.isNotBlank() }
        .joinToString(System.lineSeparator())

    val shet2Data = get3.map { mapOf("施工内容" to it) }
    sheet1Data.put("施工内容合", joinToString2.toNotBlankStr())


//    val sheet1Data = readUtf8String.parseObject<Map<>>()


//    val sheet1Data = getSheet1Data()
//    val sheet2Data = getSheet2Data()
    val stream = ClassPathResource("模板表格.xlsx").stream
    val outFile = "/Users/zjarlin/Desktop/execExcel/${name}" + ".xlsx"
    val outputStream1 = FileUtil.getOutputStream(outFile)

    return sheet1Data to name

//    val file = FileUtil.getInputStream("/Users/zjarlin/Documents/模板表格.xlsx")
//    templateFill(sheet1Data, shet2Data, file, outputStream1)

//    ExcelUtil. templateFill<ConstructionLog,ConstructionLog>(
//        vo = sheet1Data,
//        dtos = null,
//        templateStream = file,
//        outputStream = outputStream1
//    )
//    println("模板表格生成成功，路径：$outFile")
}

fun main() {
    extracted()
}

fun extracted2() {
    val string = "/Users/zjarlin/Downloads/AddzeroKmp/backend/src/test/kotlin/com/addzero/addzero_common/excel/json"
    (12..31).map {
        val string1 = "2024-11-$it.json"
        val touch = FileUtil.touch(string, string1)
        touch
    }
}


private fun getSheet1Data(): Map<String, String> {
    val mapOf = mapOf("name" to "zhangsan", "age" to "17", "userName" to "自来也", "sex" to "男")
    return mapOf
}

private fun getSheet2Data(): List<Map<String, String>>? {
    return listOf(
        mapOf("deptName" to "研发一部", "groupName" to "一部小组1", "userName" to "自来也", "sex" to "男"), mapOf("deptName" to "研发二部", "groupName" to "二部小组1", "userName" to "雏田", "sex" to "女"), mapOf("deptName" to "研发二部", "groupName" to "二部小组1", "userName" to "小樱", "sex" to "女"), mapOf("deptName" to "研发一部", "groupName" to "一部小组2", "userName" to "鸣人", "sex" to "男"), mapOf("deptName" to "研发一部", "groupName" to "一部小组2", "userName" to "佐助", "sex" to "男"), mapOf("deptName" to "研发三部", "groupName" to "三部小组", "userName" to "拓海", "sex" to "男")
    )
}

fun templateFill(vo: Map<String, Any>, dtos: List<Map<String, Any>>?, templateStream: InputStream, outputStream: OutputStream) {
    val writer = EasyExcel.write(outputStream).withTemplate(templateStream).build()
    val workbook = writer.writeContext().writeWorkbookHolder().workbook
    workbook.forceFormulaRecalculation = true

    val sheet = EasyExcel.writerSheet(0).build()
    val fillConfig = FillConfig.builder().forceNewRow(true).direction(WriteDirectionEnum.VERTICAL).build()

    writer.fill(vo, sheet)
    if (CollUtil.isNotEmpty(dtos)) {
        writer.fill(dtos, fillConfig, sheet)
    }
    writer.finish()
    outputStream.flush()
}

private fun res(fileName: String): ServletOutputStream {
    val response = SprCtxUtil.httpServletResponse
    response.contentType = "multipart/form-data"
    response.characterEncoding = "UTF-8"
    response.setHeader("Content-disposition", "attachment;filename=$fileName")
    return response.outputStream
}

fun mergeExcel(dataList: List<Map<String, String?>>, outFilePath: String) {
    val deptMap = LinkedMap<String, Int>()
    val groupMap = LinkedMap<String, Int>()
    var lastValue = ""
    var groupIndex = 0
    var groupCount = 1

    dataList.forEach { map ->
        val deptName = map["deptName"]
        val groupName = map["groupName"] ?: ""
        deptMap[deptName] = deptMap.getOrDefault(deptName, 0) + 1

        if (lastValue.isNotEmpty() && groupName == lastValue) {
            groupCount++
            groupMap["$groupIndex$groupName"] = groupCount
        } else {
            groupIndex++
            groupCount = 1
            groupMap["$groupIndex$groupName"] = groupCount
            lastValue = groupName
        }
    }

    val deptNameCellRangeList = mutableListOf<String>()
    val groupNameCellRangeList = mutableListOf<String>()
    var initDeptNameRowIndex = 1
    var initGroupNameRowIndex = 1

    deptMap.forEach { (_, count) ->
        val cellRangeRow = initDeptNameRowIndex + count - 1
        deptNameCellRangeList.add("$initDeptNameRowIndex-$cellRangeRow")
        initDeptNameRowIndex = cellRangeRow + 1
    }

    groupMap.forEach { (_, count) ->
        val cellRangeRow = initGroupNameRowIndex + count - 1
        groupNameCellRangeList.add("$initGroupNameRowIndex-$cellRangeRow")
        initGroupNameRowIndex = cellRangeRow + 1
    }

    try {
        val inputStream = FileInputStream(outFilePath)
        val workbook: Workbook = XSSFWorkbook(inputStream)
        val outputStream = FileOutputStream(outFilePath)
        val sheet = workbook.getSheetAt(1)

        deptNameCellRangeList.forEach { range ->
            val (firstRow, lastRow) = range.split("-").map { it.toInt() }
            if (firstRow != lastRow) {
                sheet.addMergedRegion(CellRangeAddress(firstRow, lastRow, 0, 0))
            }
        }

        groupNameCellRangeList.forEach { range ->
            val (firstRow, lastRow) = range.split("-").map { it.toInt() }
            if (firstRow != lastRow) {
                sheet.addMergedRegion(CellRangeAddress(firstRow, lastRow, 1, 1))
            }
        }

        workbook.write(outputStream)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}