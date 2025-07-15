package com.addzero.jlstarter.common.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


object IPUtils {
    @JvmStatic
    fun main(args: Array<String>) {
        val ipAddress = iPAddress
        println("IP Address: $ipAddress")
    }

    private val iPAddress: String?
        get() {
            val os = System.getProperty("os.name").lowercase(Locale.getDefault())

            return if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                unixMACAddress
            } else if (os.contains("win")) {
                windowsIPAddress
            } else {
                null
            }
        }

    private val unixMACAddress: String?
        get() {
            try {
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                while (networkInterfaces.hasMoreElements()) {
                    val networkInterface = networkInterfaces.nextElement()
                    val mac = networkInterface.hardwareAddress
                    if (mac != null) {
                        val sb = StringBuilder()
                        for (b in mac) {
                            sb.append(String.format("%02X:", b))
                        }
                        if (sb.length > 0) {
                            sb.deleteCharAt(sb.length - 1)
                        }
                        return sb.toString()
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            return null
        }

    private val windowsIPAddress: String?
        get() {
            try {
                val process = Runtime.getRuntime().exec("ipconfig")
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String
                while ((reader.readLine().also { line = it }) != null) {
                    if (line.contains("IPv4 Address")) {
                        val index = line.indexOf(":")
                        return line.substring(index + 1).trim { it <= ' ' }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    val localIp: String
        get() {
            var inetAddress: InetAddress? = null
            var isFind = false // 返回标识
            var networkInterfaceLists: Enumeration<NetworkInterface>? = null
            try {
                // 获取网络接口
                networkInterfaceLists = NetworkInterface.getNetworkInterfaces() as Enumeration<NetworkInterface>
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            while (networkInterfaceLists!!.hasMoreElements()) {
                val networkInterface = networkInterfaceLists.nextElement() as NetworkInterface
                val ips = networkInterface.inetAddresses
                // 遍历所有ip，获取本地地址中不是回环地址的ipv4地址
                while (ips.hasMoreElements()) {
                    inetAddress = ips.nextElement() as InetAddress
                    if (inetAddress is Inet4Address && inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()) {
                        isFind = true
                        break
                    }
                }
                if (isFind) {
                    break
                }
            }
            return if (inetAddress == null) "" else inetAddress.hostAddress
        }

}