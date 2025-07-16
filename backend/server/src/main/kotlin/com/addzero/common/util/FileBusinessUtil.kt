package com.addzero.common.util

import cn.hutool.core.codec.Base64
import cn.hutool.core.io.FileUtil
import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.ZipUtil
import com.addzero.common.consts.Vars
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Consumer
import java.util.regex.Pattern

/**
 * 文件业务
 *
 * @author zjarlin
 * @since 2023/02/23
 */
interface FileBusinessUtil {
    companion object {
        fun zipFiles(paths: Collection<String?>): String {
            return zipFiles(null, paths)
        }

        /**
         * 默认paths的文件都在一个文件夹可以省略dirname
         *
         * @param zipFileName zip文件名字
         * @param paths       路径 入参
         * @return [String]
         * @author zjarlin
         * @since 2023/02/24
         */
        fun zipFiles(zipFileName: String?, paths: Collection<String?>): String {
            val s = paths.stream().findAny().orElse(null)
            val dirname = FileUtil.getParent(s, 1)
            return zipFiles(dirname, zipFileName, paths)
        }

        /**
         * 批量压缩zip文件
         *
         * @param dirname
         * @param zipFileName zip文件名字
         * @param paths       路径 入参
         * @return [String]
         * @author zjarlin
         * @since 2023/02/24
         */
        fun zipFiles(dirname: String, zipFileName: String?, paths: Collection<String?>): String {
            //List<File> collect = paths.stream().map(e -> FileUtil.file(e)).collect(Collectors.toList());
            val s = paths.stream().findAny().orElse(null)

            val tmpZipName = "tmp"
            val file = FileUtil.mkdir(dirname + File.separator + tmpZipName)
            val tmpAbsolutePath = file.absolutePath

            paths.forEach(Consumer { p: String? -> FileUtil.copy(p, tmpAbsolutePath, true) })
            val zip = ZipUtil.zip(tmpAbsolutePath)
            val newName = if (CharSequenceUtil.isBlank(zipFileName)) "${Vars.timePrefix}.zip"
            else
                "$zipFileName.zip"
            val rename = FileUtil.rename(zip, newName, true)
            val del = FileUtil.del(tmpAbsolutePath)

            return rename.absolutePath
        }

        /**
         * 入参base64字符串,将其保存到savePath目录下,并返回文件保存的路径
         *
         * @param base64Str       入参
         * @param saveAbsRootPath
         * @return [String]
         * @author zjarlin
         * @since 2023/02/10
         */
        fun base64ToUrlWithSavePhoto(base64Str: String, saveAbsRootPath: String): String {
            var base64Str = base64Str
            if (StrUtil.isBlank(base64Str)) {
                return ""
            }
            //前缀做处理
            base64Str = base64Str.replaceFirst("data:(.+?);base64,".toRegex(), "")

            // boolean isNotBase64 = !cn.hutool.core.codec.Base64.isBase64(base64Str);
            val isNotBase64 = !checkForEncode(base64Str)
            //如果不是base64格式就不转换,还按照原来的返回
            if (isNotBase64) {
                return base64Str
            }
            // DateTimeFormatter dfDateTime = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS_");
            val dfDateTime = DateTimeFormatter.ofPattern("HH_mm_ss_SSS_")
            val format = dfDateTime.format(LocalDateTime.now())
            val path = saveAbsRootPath + File.separator + format + RandomUtil.randomString(2) + ".png"
            val touch = FileUtil.touch(path)

            val fileOutputStream: FileOutputStream = FileOutputStream(touch)
            val bytes = Base64.decode(base64Str)
            // byte[] bytes = java.util.Base64.getMimeDecoder().decode(base64Str);
            fileOutputStream.write(bytes)

            // BufferedImage read = ImageIO.read(touch);
            // boolean isNotImg = read == null;
            //如果生成的不是图片,还按原字符串返回
            // if (isNotImg) {
            //     boolean del = FileUtil.del(touch);
            //     return base64Str;
            // }
            return path
        }

        fun checkForEncode(string: String): Boolean {
            var string = string
            string = string.replaceFirst("data:(.+?);base64,".toRegex(), "")

            val pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"
            val base64ImageRegex = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$"

            // "[\\/]?([\\da-zA-Z]+[\\/+]+)*[\\da-zA-Z]+([+=]{1,2}|[\\/])?"
            val r = Pattern.compile(pattern)
            val m = r.matcher(string)
            val b = m.find()
            return b
            // && isValidImage(string)
        }
    }
}