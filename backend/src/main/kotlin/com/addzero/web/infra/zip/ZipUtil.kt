package com.addzero.web.infra.zip

import com.addzero.kmp.kt_util.add
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object ZipUtil {
    fun unzipInputStream(zipInputStream: InputStream): List<File> {
        val fileList: List
<File> = ArrayList()
        try {
            ZipInputStream(zipInputStream).use { zip ->
                var zipEntry: ZipEntry? = null
                while ((zip.nextEntry.also { zipEntry = it }) != null) {
                    val fileName_zip = zipEntry!!.name
                    val file = File(fileName_zip)
                    if (fileName_zip.endsWith("/")) {
                        file.mkdir()
                        continue
                    } else {
                        val outputStream = BufferedOutputStream(FileOutputStream(file))
                        val byte_s = ByteArray(1024)
                        var num: Int
                        while ((zip.read(byte_s, 0, byte_s.size).also { num = it }) > 0) {
                            outputStream.write(byte_s, 0, num)
                        }
                        outputStream.close()
                    }
                    fileList.add(file)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fileList
    }
}
