package com.addzero.common.util

import com.addzero.common.kt_util.containsAny
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.print.DocFlavor
import javax.print.PrintException
import javax.print.PrintServiceLookup
import javax.print.SimpleDoc
import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.standard.*

/**
 * 打印工具类
 * @author zjarlin
 * @date 2025/01/22
 */
object PrinterUtil {
    // 1. 获取默认打印机


    /**
     * 打印单个文件
     *
     * @param filePath 文件路径
     * @param copies   打印份数
     */
    fun printFile(filePath: String, copies: Int) {
        try {
            // 1. 获取默认打印机
            val defaultPrinter = PrintServiceLookup.lookupDefaultPrintService()
            if (defaultPrinter == null) {
                println("未找到默认打印机！")
                return
            }

            // 2. 创建打印任务
            val printJob = defaultPrinter.createPrintJob()

            // 3. 设置打印属性
            val attributes = HashPrintRequestAttributeSet().apply {
                add(Copies(copies)) // 打印份数
                add(MediaSizeName.ISO_A4) // 纸张大小
                add(OrientationRequested.PORTRAIT) // 打印方向（纵向）
                add(SheetCollate.COLLATED) // 逐份打印
                add(Chromaticity.COLOR) // 彩色打印
//                add(Chromaticity.MONOCHROME) //黑白
                add(PrintQuality.HIGH) // 高质量打印
            }

            // 4. 加载文件
            val fileInputStream = FileInputStream(filePath)
            val doc = SimpleDoc(fileInputStream, DocFlavor.INPUT_STREAM.AUTOSENSE, null)

            // 5. 发送打印任务
            printJob.print(doc, attributes)

            println("文件已发送到打印机: $filePath")
        } catch (e: FileNotFoundException) {
            println("文件未找到: ${e.message}")
        } catch (e: PrintException) {
            println("打印失败: ${e.message}")
        }
    }

    /**
     * 遍历目录并打印所有可打印文件
     *
     * @param directoryPath 目录路径
     * @param copies        打印份数
     */
    fun printFilesInDirectory(directoryPath: String, copies: Int) {
        val directory = File(directoryPath)
        if (!directory.isDirectory) {
            println("路径不是目录: $directoryPath")
            return
        }

        // 获取目录下的所有文件
        val files = directory.listFiles()
        if (files.isNullOrEmpty()) {
            println("目录为空: $directoryPath")
            return
        }

        // 遍历文件并打印
        files.forEach { file ->
            if (file.isFile && isPrintable(file.name)) {
                printFile(file.absolutePath, copies)
            }
        }
    }

    /**
     * 判断文件是否可打印
     *
     * @param fileName 文件名
     * @return 是否可打印
     */
    private fun isPrintable(fileName: String): Boolean {
        val printableExtensions = arrayOf(
            ".pdf", ".txt", ".jpg", ".jpeg", ""
                    + ".png", ".doc", ".xlsx", ".xls"
        )
        fileName.containsAny()
        return printableExtensions.any { fileName.lowercase().endsWith(it) }
    }
}

fun main() {
    val directoryPath = "/Users/zjarlin/Desktop/test" // 替换为实际目录路径
    val copies = 1 // 打印份数
    PrinterUtil.printFilesInDirectory(directoryPath, copies)

}