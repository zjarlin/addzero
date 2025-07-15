package com.addzero.common.util.shell

import java.io.BufferedReader
import java.io.InputStreamReader

object CommandExecutor {

    /**
     * 执行命令并返回输出结果
     *
     * @param command 要执行的命令
     * @return 命令执行结果（标准输出 + 标准错误）
     */
    fun execute(command: String): String {
        val process = if (isWindows()) {
            // Windows 使用 PowerShell
            Runtime.getRuntime().exec(arrayOf("powershell.exe", "-Command", command))
        } else {
            // macOS/Linux 使用 Bash
            Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", command))
        }

        val output = StringBuilder()
        val error = StringBuilder()

        // 读取标准输出
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
        }

        // 读取标准错误
        BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                error.append(line).append("\n")
            }
        }

        // 等待命令执行完成
        process.waitFor()

        // 返回标准输出和标准错误
        return if (error.isEmpty()) output.toString() else error.toString()
    }

    /**
     * 执行命令并通过回调函数实时返回输出结果
     *
     * @param command 要执行的命令
     * @param onProgress 进度回调函数，接收命令的实时输出
     * @param onError 错误回调函数，接收命令的错误输出
     * @return 命令执行结果（标准输出 + 标准错误）
     */
    fun execute(command: String, onProgress: (String) -> Unit, onError: (String) -> Unit = {}): String {
        val process = if (isWindows()) {
            Runtime.getRuntime().exec(arrayOf("powershell.exe", "-Command", command))
        } else {
            Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", command))
        }

        val output = StringBuilder()
        val error = StringBuilder()

        // 创建标准输出读取线程
        val outputThread = Thread {
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let {
                        output.append(it).append("\n")
                        onProgress(it)
                    }
                }
            }
        }.apply { start() }

        // 创建标准错误读取线程
        val errorThread = Thread {
            BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    line?.let {
                        error.append(it).append("\n")
                        onError(it)
                    }
                }
            }
        }.apply { start() }

        // 等待两个线程完成
        outputThread.join()
        errorThread.join()

        // 等待命令执行完成
        process.waitFor()

        // 返回标准输出和标准错误
        return if (error.isEmpty()) output.toString() else error.toString()
    }

    /**
     * 判断当前操作系统是否为 Windows
     */
    fun isWindows(): Boolean {
        return System.getProperty("os.name").startsWith("Windows", ignoreCase = true)
    }
}

fun main() {
    // 示例命令
    val command = if (CommandExecutor.isWindows()) {
        "Get-Process" // Windows 的 PowerShell 命令
    } else {
        "ls -l" // macOS/Linux 的 Bash 命令
    }

    // 使用带进度回调的方法执行命令
    val result = CommandExecutor.execute(
        command = command,
        onProgress = { line -> println("进度: $line") },
        onError = { error -> println("错误: $error") }
    )
    println("\n最终结果:\n$result")
}