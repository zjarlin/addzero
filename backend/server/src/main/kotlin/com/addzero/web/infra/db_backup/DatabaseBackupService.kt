package com.addzero.demo.service

import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Service
class DatabaseBackupService {
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    fun backupDatabase() {
        try {
            // 获取当前类所在的包路径
            val packagePath = javaClass.getPackage().name.replace('.', '/')

            // 使用 PathMatchingResourcePatternResolver 获取脚本资源
            val resolver: PathMatchingResourcePatternResolver = PathMatchingResourcePatternResolver()
            val resource: Resource =
                resolver.getResource("classpath:" + packagePath + "/scripts/" + BACKUP_DM_DB + ".sh")

            // 创建临时文件并将脚本内容复制到临时文件
            val tempScript = java.nio.file.Files.createTempFile(BACKUP_DM_DB, ".sh")
            java.nio.file.Files.copy(
                resource.inputStream,
                tempScript,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            )
            tempScript.toFile().setExecutable(true) // 设置脚本文件为可执行

            // 使用 ProcessBuilder 调用脚本
            val processBuilder = ProcessBuilder(tempScript.toString())
            val process = processBuilder.start()

            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    println(line)
                }
            }
            BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    System.err.println(line)
                }
            }
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                println("Database backup script executed successfully")
            } else {
                System.err.println("Database backup script execution failed. Exit code: $exitCode")
            }

            // 删除临时脚本文件
            java.nio.file.Files.delete(tempScript)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val BACKUP_DM_DB: String = "backup_dm_db"
    }
}