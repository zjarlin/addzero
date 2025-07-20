package com.addzero.common.util.excel

import cn.hutool.core.collection.CollUtil
import cn.hutool.poi.excel.ExcelReader
import cn.hutool.poi.excel.ExcelUtil
import cn.idev.excel.EasyExcel
import cn.idev.excel.ExcelWriter
import cn.idev.excel.enums.WriteDirectionEnum
import cn.idev.excel.write.metadata.WriteSheet
import cn.idev.excel.write.metadata.fill.FillConfig
import org.apache.commons.collections4.map.LinkedMap
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*
import java.util.function.Consumer

object ExcelUtil {
    // 扩展函数：统一处理不同类型的 Excel 读取
    inline fun <reified T : Any> T.toExcelReader(): ExcelReader = when (this) {
        is String -> ExcelUtil.getReader(this)
        is File -> ExcelUtil.getReader(this)
        is InputStream -> ExcelUtil.getReader(this)
        else -> throw IllegalArgumentException("Unsupported jdbcType: ${this.javaClass}")
    }

    fun <VO, DTO> templateFill(
        vo: VO?,
        dtos: MutableList<DTO?>?,
        templateStream: InputStream,
        outputStream: OutputStream
    ) {
        //文件模板输入流
        val writer: ExcelWriter =
            EasyExcel.write(outputStream).withTemplate(templateStream) //.registerConverter(new LocalDateConverter())
                //.registerConverter(new LocalDateTimeConverter())
                .build()
        //3.4 设置强制计算公式：不然公式会以字符串的形式显示在excel中
        val workbook: Workbook = writer.writeContext().writeWorkbookHolder().workbook
        workbook.forceFormulaRecalculation = true

        val sheet: WriteSheet? = EasyExcel.writerSheet(0).build()
        //填充列表开启自动换行,自动换行表示每次写入一条list数据是都会重新生成一行空行,此选项默认是关闭的,需要提前设置为true
        val fillConfig = FillConfig.builder().forceNewRow(true).direction(WriteDirectionEnum.VERTICAL).build()
        //填充一维数据
        writer.fill(vo, sheet)
        //填充二维数据
        if (CollUtil.isNotEmpty(dtos)) {
            writer.fill(dtos, fillConfig, sheet)
        }
        //填充完成
        writer.finish()
        outputStream.flush()
    }

    fun mergeExcel(dataList: MutableList<MutableMap<String?, String?>>, outFilePath: String) {
        val deptMap = LinkedMap<String?, Int?>()
        val groupMap = LinkedMap<String?, Int?>()
        var lastValue: String? = ""
        var groupIndex = 0
        var groupCount = 1
        for (map in dataList) {
            val deptName = map.get("deptName")
            val groupName: String = map.get("groupName")!!
            deptMap.put(deptName, deptMap.getOrDefault(deptName, 0)!! + 1)

            if ("" != lastValue && groupName == lastValue) {
                groupCount++
                groupMap.put(groupIndex.toString() + groupName, groupCount)
            } else {
                groupIndex++
                groupCount = 1
                groupMap.put(groupIndex.toString() + groupName, groupCount)
                lastValue = groupName
            }
        }

        val deptNameCellRangeList: MutableList<String> = ArrayList<String>()
        val groupNameCellRangeList: MutableList<String?> = ArrayList<String?>()
        var initDeptNameRowINdex = 1 // 刨去标题行
        var initGroupNameRowINdex = 1 // 刨去标题行
        for (entry in deptMap.entries) {
            val cellRangeRow = initDeptNameRowINdex + entry.value!! - 1
            deptNameCellRangeList.add(initDeptNameRowINdex.toString() + "-" + cellRangeRow)
            initDeptNameRowINdex = cellRangeRow + 1
        }
        for (entry in groupMap.entries) {
            val cellRangeRow = initGroupNameRowINdex + entry.value!! - 1
            groupNameCellRangeList.add(initGroupNameRowINdex.toString() + "-" + cellRangeRow)
            initGroupNameRowINdex = cellRangeRow + 1
        }

        try {
            // 使用Apache POI合并单元格
            val inputStream = FileInputStream(outFilePath)
            val workbook: Workbook = XSSFWorkbook(inputStream)
            val outputStream = FileOutputStream(outFilePath)
            val sheet: Sheet = workbook.getSheetAt(1) // 合并第二个sheet
            // 合并
            for (deptNameCellRange in deptNameCellRangeList) {
                val firstRow =
                    deptNameCellRange.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
                val lastRow =
                    deptNameCellRange.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                if (firstRow != lastRow) {
                    // 入参： firstRow 合并开始行； lastRow 合并结尾行； firstCol 合并开始列； lastCol 合并结束列。
                    val cellAddresses = CellRangeAddress(firstRow, lastRow, 0, 0)
                    sheet.addMergedRegion(cellAddresses)
                }
            }
            groupNameCellRangeList.forEach(Consumer { groupNameCellRange: String? ->  // forEach 写法
                val firstRow =
                    groupNameCellRange!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
                val lastRow =
                    groupNameCellRange.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()
                if (firstRow != lastRow) {
                    // 入参： firstRow 合并开始行； lastRow 合并结尾行； firstCol 合并开始列； lastCol 合并结束列。
                    val cellAddresses = CellRangeAddress(firstRow, lastRow, 1, 1)
                    sheet.addMergedRegion(cellAddresses)
                }
            })
            // 保存合并后的Excel文件
            workbook.write(outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    // 主方法：读取 Excel 并返回 Map 列表
    inline fun <reified T : Any> readMap(
        source: T,
        headerRowIndex: Int = 0,
        startRowIndex: Int = 1,
        sheetIndex: Int = 0,
        ignoreEmptyRow: Boolean = true,
        ignoreEmptyData: Boolean = true,
    ): MutableList<MutableMap<String, Any>> = source.toExcelReader().readAll()

    /**
     * 导出Map数据到Excel
     * @param map 要导出的Map数据
     * @param savePath 保存路径（包含文件名）
     */
    fun exportMap(map: Map<String, Any>, savePath: String) {
        val writer = EasyExcel.write(savePath).build()
        val sheet = EasyExcel.writerSheet("Sheet1").build()

        // 处理表头
        val headers = map.keys.toList()
        val headerRow = mutableListOf<List<String>>()
        headerRow.add(headers)
        writer.write(headerRow, sheet)

        // 处理数据行
        val dataRow = mutableListOf<List<Any>>()
        dataRow.add(map.values.toList())
        writer.write(dataRow, sheet)

        writer.finish()
    }

    /**
     * 导出多行Map数据到Excel
     * @param mapList 要导出的Map数据列表
     * @param savePath 保存路径（包含文件名）
     */
    fun exportMapList(mapList: List<Map<String, Any>>, savePath: String) {
        if (mapList.isEmpty()) return

        val writer = EasyExcel.write(savePath).build()
        val sheet = EasyExcel.writerSheet("Sheet1").build()

        // 处理表头
        val headers = mapList.first().keys.toList()
        val headerRow = mutableListOf<List<String>>()
        headerRow.add(headers)
        writer.write(headerRow, sheet)

        // 处理数据行
        val dataRows = mapList.map { map -> map.values.toList() }
        writer.write(dataRows, sheet)

        writer.finish()
    }
}
