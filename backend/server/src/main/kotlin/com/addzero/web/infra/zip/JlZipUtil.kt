package com.addzero.web.infra.zip

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.ZipUtil
import com.addzero.web.infra.upload.DownloadUtil
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.nio.charset.Charset
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object JlZipUtil {
    fun handlerZipInputStream(file: MultipartFile, unzippedHandler: BiFunction<String, File, Array<File>>) {
        // 获取上传的文件流

        val inputStream: InputStream = file.inputStream
        // 解压缩文件到临时目录
        val charset = Charset.defaultCharset()
        //获取系统路径分隔符
        val separator = File.separator
        //创建临时目录
        val tempDirPath = FileUtil.getTmpDirPath() + separator + IdUtil.getSnowflakeNextIdStr()
        val tempDir = FileUtil.file(tempDirPath)

        //        File tempDir = FileUtil.mkdir("/Users/zjarlin/Desktop/out");
        val unzip = ZipUtil.unzip(inputStream, tempDir, charset)


        val handedFiles = unzippedHandler.apply(tempDirPath, unzip)
        // 创建 zip 文件并发送给 HTTP 响应流
        val zipFile = createZipFile(handedFiles)
        val inputStream1: BufferedInputStream = FileUtil.getInputStream(zipFile)
        DownloadUtil.downloadZip(
            "output.zip",
            Consumer<OutputStream> { outputStream1: OutputStream -> IoUtil.copy(inputStream1, outputStream1) })
        //        删除临时资源
        FileUtil.del(zipFile)
        FileUtil.del(unzip)
    }


    fun saveFilesToDirectory(files: List<File>, directory: File) {
        if (CollUtil.isEmpty(files)) {
            return
        }
        for (file in files) {
            val newFile = File(directory, file.name)
            FileUtil.copy(file, newFile, true) // 使用Hutool的文件复制方法
        }
    }

    private fun createZipFile(files: Array<File>): File {
        val zipFile = File.createTempFile("output", ".zip")
        val fileOutputStream = FileOutputStream(zipFile)
        val zipOutputStream = ZipOutputStream(fileOutputStream)
        files.forEach { addToZipFile(it, zipOutputStream) }
        return zipFile
    }

    private fun addToZipFile(dir: File, zipOut: ZipOutputStream) {
        val files = dir.listFiles()
        if (files != null) {
            for (file in files) {
                FileInputStream(file).use { fis ->
                    val zipEntry = ZipEntry(dir.name + "/" + file.name)
                    zipOut.putNextEntry(zipEntry)
                    val bytes = ByteArray(1024)
                    var length: Int
                    while ((fis.read(bytes).also { length = it }) >= 0) {
                        zipOut.write(bytes, 0, length)
                    }
                }
            }
        }
    }
}