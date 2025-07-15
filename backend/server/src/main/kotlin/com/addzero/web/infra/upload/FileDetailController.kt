//package com.addzero.web.infra.upload
//
//import jakarta.servlet.http.HttpServletRequest
//import net.coobird.thumbnailator.Thumbnails
//import org.dromara.x.file.storage.core.FileInfo
//import org.dromara.x.file.storage.core.FileStorageService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RestController
//import org.springframework.web.multipart.MultipartFile
//import java.io.InputStream
//
///**
// *其它操作
// * //手动构造文件信息，可用于其它操作
// * FileInfo fileInfo = new FileInfo()
// *         .setPlatform("huawei-obs-1")
// *         .setBasePath("test/")
// *         .setPath("aa/")
// *         .setFilename("image.png")
// *         .setThFilename("image.png.min.jpg");
// *
// * //文件是否存在
// * boolean exists = fileStorageService.exists(fileInfo);
// * //下载
// * byte[] bytes = fileStorageService.download(fileInfo).bytes();
// * //删除
// * fileStorageService.delete(fileInfo);
// * //其它更多操作
// * 点击复制错误复制成功
// * 如果将文件记录保存到数据库中，还可以更方便的根据 URL 进行操作了，详情请阅读 保存上传记录 章节
// *
// * //直接从数据库中获取 FileInfo 对象，更加方便执行其它操作
// * FileInfo fileInfo = fileStorageService.getFileInfoByUrl("https://abc.def.com/test/aa/image.png");
// *
// * //文件是否存在
// * boolean exists = fileStorageService.exists("https://abc.def.com/test/aa/image.png");
// * //下载
// * byte[] bytes = fileStorageService.download("https://abc.def.com/test/aa/image.png").bytes();
// * //删除
// * fileStorageService.delete("https://abc.def.com/test/aa/image.png");
// * //其它更多操作
// * 点击复制错误复制成功
// *
// */
//@RestController
//class FileDetailController {
//    @Autowired
//    private val fileStorageService: FileStorageService? = null //注入实列
//
//    /**
//     * 上传文件
//     */
//    @PostMapping("/upload")
//    fun upload(file: MultipartFile?): FileInfo {
//        val upload = fileStorageService!!.of(file).upload()
//        return upload
//    }
//
//    /**
//     * 上传文件，成功返回文件 url
//     */
//    @PostMapping("/upload2")
//    fun upload2(file: MultipartFile?): String {
//        val fileInfo = fileStorageService!!.of(file)
//            .setPath("upload/") //保存到相对路径下，为了方便管理，不需要可以不写
//            .setSaveFilename("image.jpg") //设置保存的文件名，不需要可以不写，会随机生成
//            .setObjectId("0") //关联对象id，为了方便管理，不需要可以不写
//            .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
//            .putAttr("role", "admin") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
//            .upload() //将文件上传到对应地方
//        return if (fileInfo == null) "上传失败！" else fileInfo.url
//    }
//
//    /**
//     * 上传图片，成功返回文件信息
//     * 图片处理使用的是 https://github.com/coobird/thumbnailator
//     */
//    @PostMapping("/upload-image")
//    fun uploadImage(file: MultipartFile?): FileInfo {
//        return fileStorageService!!.of(file)
//            .image { img: Thumbnails.Builder<out InputStream?> -> img.size(1000, 1000) } //将图片大小调整到 1000*1000
//            .thumbnail { th: Thumbnails.Builder<out InputStream?> -> th.size(200, 200) } //再生成一张 200*200 的缩略图
//            .upload()
//    }
//
//    /**
//     * 上传文件到指定存储平台，成功返回文件信息
//     */
//    @PostMapping("/upload-platform")
//    fun uploadPlatform(file: MultipartFile?): FileInfo {
//        return fileStorageService!!.of(file)
//            .setPlatform("aliyun-oss-1") //使用指定的存储平台
//            .upload()
//    }
//
//    /**
//     * 直接读取 HttpServletRequest 中的文件进行上传，成功返回文件信息
//     * 使用这种方式有些注意事项，请查看文档 基础功能-上传 章节
//     */
//    @PostMapping("/upload-request")
//    fun uploadPlatform(request: HttpServletRequest?): FileInfo {
//        val upload = fileStorageService!!.of(request).upload()
//        return upload
//    }
//}