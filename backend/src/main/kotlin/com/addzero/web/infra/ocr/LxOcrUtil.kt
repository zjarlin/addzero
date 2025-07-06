package com.addzero.web.infra.ocr

import cn.hutool.extra.spring.SpringUtil
import cn.hutool.http.HttpRequest
import com.alibaba.fastjson2.JSON
import java.io.File


fun getip(): String {
    try {
        return SpringUtil.getProperty("ocr.ip")
    } catch (e: Exception) {
        return "192.168.0.1:8089"
    }
}


object LxOcrUtil {

    /**
     * docker run -itd --rm -p 8089:8089 --name trwebocr mmmz/trwebocr:latest
     * @param [ip]
     * @param [bytes]
     * @param [fileName]
     * @return [Pair<String, List<String>>?]
     *///@Throws(Exception::class)
    fun ocr(
        ip: String = getip(),
        requestCustomizer: (HttpRequest) -> Unit = {},  //
        // Consumer<HttpRequest> 参数
    ): String {
        val post = HttpRequest.post("$ip/api/tr-run/")
        requestCustomizer(post)
        val header = post
            .form("is_draw", "0").form("img", "").form("compress", "0")
            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
        val response = header.execute()
        val body = response?.body()
        // 解析返回的 JSON 数据
        val ocrOutVO: OcrOutVO = JSON.parseObject(body, OcrOutVO::class.java)
        val rawOut: List<List<Any>> = ocrOutVO.data?.raw_out ?: emptyList()
        // 提取第二列的文本信息
        val collect: List<String> = rawOut.map { e -> e[1] as String }.filter { it.isNotBlank() }
        val collectJoined: String = collect.filter { it.isNotBlank() }
            .joinToString(System.lineSeparator())

        return collectJoined
    }

    fun ocr(ip: String = getip(), bytes: ByteArray, fileName: String): String {
//        val post = HttpRequest.post("$ip/api/tr-run/")
        val ocr = ocr { run { it.form("file", bytes, fileName) } }
        return ocr
    }

    fun ocr(ip: String = getip(), file: File): String {
        val post = HttpRequest.post("$ip/api/tr-run/")
        val ocr = ocr { run { it.form("file", file) } }
        return ocr
    }

    fun ocr(ip: String = getip(), resource: cn.hutool.core.io.resource.Resource):
            String {
        val post = HttpRequest.post("$ip/api/tr-run/")
        val ocr = ocr { run { it.form("file", resource) } }
        return ocr
    }


}

/**
 * @author zjarlin
 * @since 2023/11/25 14:22
 */
// OcrOutVO 数据类
data class OcrOutVO(val code: Int?, val msg: String?, val data: DataDTO?)
data class DataDTO(val raw_out: List<List<Any>>?, val speed_time: Double?)
