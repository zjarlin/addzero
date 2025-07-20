package com.addzero.common.util.bash

import cn.hutool.core.io.FileUtil
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader


object CommandExecutor {
    fun runAndResult(cmd: String): String {

        val sb = java.lang.StringBuilder()
        var br: BufferedReader? = null
        var execFlag = true
        val uuid = java.util.UUID.randomUUID().toString().replace("-", "")
        val tempFileName = "./temp$uuid.sh"
        try {
            val osName = System.getProperty("os.name").uppercase()
            if (osName.matches("^(?i)LINUX.*$".toRegex()) || osName.contains("MAC")) {
                val execute_fw = java.io.FileWriter(tempFileName)
                val execute_bw: BufferedWriter = BufferedWriter(execute_fw)
                execute_bw.write(cmd + "\n")
                execute_bw.close()
                execute_fw.close()
                val command = "bash $tempFileName"
                val p = Runtime.getRuntime().exec(command)
                p.waitFor()
                br = BufferedReader(InputStreamReader(p.inputStream))
                var line: String
                while ((br.readLine().also { line = it }) != null) {
                    sb.append(System.lineSeparator())
                    sb.append(line)
                }
                br.close()
                br = BufferedReader(InputStreamReader(p.errorStream))
                while ((br.readLine().also { line = it }) != null) {
                    sb.append(System.lineSeparator())
                    sb.append(line)
                    if (line.length > 0) {
                        execFlag = false
                    }
                }
                br.close()
                if (execFlag) {
                } else {
                    throw java.lang.RuntimeException(sb.toString())
                }
            } else {
                throw java.lang.RuntimeException("不支持的操作系统类型")
            }
        } catch (e: java.lang.Exception) {
            println("执行失败$e")
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    println("io异常$e")
                }
            }
            FileUtil.del(tempFileName)
        }
        val string = sb.toString()
        return cn.hutool.core.util.StrUtil.trim(string)
    }
}