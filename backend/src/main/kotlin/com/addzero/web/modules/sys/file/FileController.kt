package com.addzero.web.modules.sys.file

import com.addzero.kmp.entity.FileUploadResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/sys/file")
class FileController {
    @PostMapping("/upload")
    fun upload(@RequestPart file: MultipartFile): String {

        return ""

    }

    @PostMapping("/download")
    fun download(fileId: String): String {

        return "redisKey"
    }

    @GetMapping("queryProgress")
    fun queryProgress(redisKey: String): FileUploadResponse {
        return FileUploadResponse("fileUrl", 1f)

    }

}