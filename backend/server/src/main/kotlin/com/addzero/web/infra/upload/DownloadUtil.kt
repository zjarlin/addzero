package com.addzero.web.infra.upload

import cn.hutool.core.codec.Base64Encoder
import cn.hutool.core.util.StrUtil
import com.addzero.web.infra.constant.ContentTypeEnum
import com.addzero.web.infra.spring.SprCtxUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.OutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.function.Consumer

/**
 * @author zjarlin
 * @since 2023/11/28 10:25
 */
object DownloadUtil {

    val httpServletRequest: HttpServletRequest
        get() {
            return SprCtxUtil.httpServletRequest
        }
    val httpServletResponse: HttpServletResponse
        get() {
            return SprCtxUtil.httpServletResponse
        }


    /**
     * 调用浏览器文件下载
     */
    fun downloadExcel(fileName: String, consumer: Consumer<OutputStream>) {
        download(fileName, consumer, ContentTypeEnum.XLSX)
    }

    fun download(fileName: String, consumer: Consumer<OutputStream>, tab: ContentTypeEnum) {
        download(fileName, consumer, tab, true)
    }

    fun download(fileName: String, consumer: Consumer<OutputStream>, tab: ContentTypeEnum, addPostfix: Boolean) {
        val application: String = tab.application
        val postfix: String = tab.postfix
        httpServletResponse.characterEncoding = "UTF-8"
        //得请求头中的User-Agent
        val agent: String = httpServletRequest.getHeader("User-Agent")

        // 根据不同的客户端进行不同的编码
        var filenameEncoder = ""
        if (agent.contains("MSIE")) {
            // IE浏览器
            filenameEncoder = URLEncoder.encode(fileName, "utf-8")
            filenameEncoder = filenameEncoder.replace("+", " ")
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            val encode = Base64Encoder.encode(fileName.toByteArray(StandardCharsets.UTF_8))
            filenameEncoder = "=utf-8B$encode="
        } else {
            // 其它浏览器
            filenameEncoder = URLEncoder.encode(fileName, "utf-8")
        }
        if (addPostfix) {
            filenameEncoder = StrUtil.addSuffixIfNot(filenameEncoder, postfix)
        }
        //        filenameEncoder = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        httpServletResponse.setHeader(
            "Content-disposition", "attachment;filename=$filenameEncoder"
        )
        httpServletResponse.contentType = application
        val outputStream: OutputStream = httpServletResponse.outputStream
        consumer.accept(outputStream)
    }


    fun downloadZip(fileName: String, consumer: Consumer<OutputStream>) {
        download(fileName, consumer, ContentTypeEnum.ZIP)
    }
}