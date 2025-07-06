package com.addzero.web.infra.curllog

import cn.hutool.core.util.StrUtil
import com.alibaba.fastjson2.JSON
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.JoinPoint
import org.springframework.http.HttpMethod
import org.springframework.web.multipart.MultipartFile
import java.util.*
import java.util.stream.Collectors

/**
 * @author zjarlin
 * @since 2023/8/2 15:51
 */
object CurlUtil {
    fun formatCurlCommand(curlCommand: String): String {
        // Ensure each '-H', '-d', or '-' followed by a letter is in a new line
        var curlCommand = curlCommand
        curlCommand = curlCommand.replace("(?<=(-[Hd]|- )[^\\s])".toRegex(), "\\\n$1")
        return curlCommand
    }

    fun generateCurlCommand(request: HttpServletRequest, joinPoint: JoinPoint): String? {
        val curlCommand = StringBuilder()

        // Append curl command with HTTP method and URL
        val method: String = request.method
        val requestURL: String = request.requestURL.toString()
        curlCommand.append("curl -X ").append(" ").append(method).append(" ")

        // Append request headers
        val headerNames: Enumeration<String> = request.headerNames

        val headers = Collections.list(headerNames)
            .filter(this::needHeader).associateWith { headerName -> request.getHeader(headerName) }

        headers.forEach { (headerName: String, headerValue: String?) ->
            curlCommand.append("-H \"").append(
                escapeQuotes(
                    headerName!!
                )
            ).append(": ").append(escapeQuotes(headerValue!!)).append("\" ")
        }

        // Append request body or query parameters
        if (HttpMethod.GET.matches(method)) {
            val queryParams = getQueryParams(request)
            val queryString = queryParams.entries
                .stream()
                .map { entry: Map.Entry<String, String> -> entry.key + "=" + entry.value }
                .collect(Collectors.joining("&"))
            curlCommand
                .append(" \"").append(requestURL) //                    .append("\" ")

                .append("?").append(queryString).append("\" ")
        } else {
            val args: Array<Any> = joinPoint.args
            for (arg in args) {
                if (arg is HttpServletRequest || arg is HttpServletResponse) {
                    continue
                }
                if (arg is Map<*, *>) {
                    val queryParams = getQueryParamsFromMap(arg)
                    val queryString = queryParams.entries
                        .stream()
                        .map { entry: Map.Entry<String, String> -> entry.key + "=" + entry.value }
                        .collect(Collectors.joining("&"))
                    curlCommand.append("\"?").append(queryString).append("\" ")
                }
                if (MultipartFile::class.java.isAssignableFrom(arg.javaClass)) {
                    // 如果参数是 MultipartFile 类型，则将其转换为文件上传的形式
                    val file: MultipartFile = arg as MultipartFile
                    val filename = file.originalFilename
                    val requestBody = "-F \"$filename=@/path/to/your/file/$filename\""
                    curlCommand.append(requestBody).append(" ")
                    //                    String requestBody =  "{}" : JSON.toJSONString(arg);
                } else if (Array<MultipartFile>::class.java.isAssignableFrom(arg.javaClass)) {
                    val file: Array<MultipartFile> = arg as Array<MultipartFile>
                    val collect: String = Arrays.stream<MultipartFile>(file).map<String> { e: MultipartFile ->
                        val filename = e.originalFilename
                        val requestBody = "-F \"$filename=@/path/to/your/file/$filename\""
                        requestBody
                    }.collect(Collectors.joining(","))
                    curlCommand.append(collect).append(" ")
                } else {
//                    Class<?> aClass = arg.getClass();
//                    boolean assignableFrom = MultipartFile.class.isAssignableFrom(aClass);
                    val requestBody: String = JSON.toJSONString(arg)
                    if (requestBody != null && !requestBody.isEmpty()) {
                        val str = escapeQuotes(requestBody)
                        //                        String str = escapeQuotes(requestBody);
                        curlCommand.append("-d \"").append(str).append("\" ")
                            .append(" \"").append(requestURL).append("\" ")
                    }
                }
            }
        }

        val s = curlCommand.toString().replace("\\s+".toRegex(), " ")
        val fix = System.lineSeparator()
        val s11 = StrUtil.addPrefixIfNot(s, fix)
        val s2 = StrUtil.addSuffixIfNot(s11, fix)
        return s2 // Remove extra whitespaces and make it single-line
    }

    private fun escapeQuotes(input: String): String {
        return input.replace("\"", "\\\"")
    }

    private fun needHeader(headerName: String): Boolean {
        val list: List<String> = mutableListOf("X-Access-Token", "Content-Type")
        // Add common header names here
        val b = list.stream().anyMatch { e: String? -> StrUtil.containsIgnoreCase(e, headerName) }
        return b
    }

    private fun getQueryParams(request: HttpServletRequest): Map<String, String> {
        val queryParams: MutableMap<String, String> = HashMap()
        val parameterNames: Enumeration<String> = request.parameterNames
        while (parameterNames.hasMoreElements()) {
            val paramName: String = parameterNames.nextElement()
            val paramValue: String = request.getParameter(paramName)
            queryParams[paramName] = paramValue
        }
        return queryParams
    }

    private fun getQueryParamsFromMap(paramMap: Map<*, *>): Map<String, String> {
        val queryParams: MutableMap<String, String> = HashMap()
        for ((key1, value1) in paramMap) {
            val key = key1.toString()
            val value = value1.toString()
            queryParams[key] = value
        }
        return queryParams
    }
}