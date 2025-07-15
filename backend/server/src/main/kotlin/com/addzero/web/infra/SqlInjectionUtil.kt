package com.addzero.web.infra

import cn.hutool.core.util.StrUtil
import cn.hutool.crypto.SecureUtil
import com.addzero.web.infra.config.log
import com.addzero.kmp.exp.BizException
import jakarta.servlet.http.HttpServletRequest
import java.util.*
import java.util.regex.Pattern

/**
 * sql注入处理工具类
 *
 * @author zhoujf
 */
object SqlInjectionUtil {
    /**
     * sign 用于表字典加签的盐值【SQL漏洞】
     * （上线修改值 20200501，同步修改前端的盐值）
     */
    private const val TABLE_DICT_SIGN_SALT = "20200501"
    private const val XSS_STR =
        "and |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|user()"

    /**
     * 正则 user() 匹配更严谨
     */
    private const val REGULAR_EXPRE_USER = "user[\\s]*\\([\\s]*\\)"

    /**正则 show tables */
    private const val SHOW_TABLES = "show\\s+tables"

    /**
     * sql注释的正则
     */
    private val SQL_ANNOTATION: Pattern = Pattern.compile("/\\*[\\s\\S]*\\*/")

    /**
     * 针对表字典进行额外的sign签名校验（增加安全机制）
     * @param dictCode:
     * @param sign:
     * @param request:
     * @Return: void
     */
    fun checkDictTableSign(dictCode: String, sign: String, request: HttpServletRequest) {
        //表字典SQL注入漏洞,签名校验
        val accessToken = request.getHeader("X-Access-Token")
        val signStr = dictCode + TABLE_DICT_SIGN_SALT + accessToken
        val javaSign = SecureUtil.md5(signStr)
        if (javaSign != sign) {
            log.error("表字典，SQL注入漏洞签名校验失败 ：$sign!=$javaSign,dictCode=$dictCode")
            throw BizException("无权限访问！")
        }
        log.info(" 表字典，SQL注入漏洞签名校验成功！sign=$sign,dictCode=$dictCode")
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     * @param value
     * @return
     */
    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     * @param value
     */
    @JvmOverloads
    fun filterContent(value: String?, customXssString: String? = null) {
        var value = value
        if (value == null || "" == value) {
            return
        }
        // 校验sql注释 不允许有sql注释
        checkSqlAnnotation(value)
        // 统一转为小写
        value = value.lowercase(Locale.getDefault())

        //SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
        //columnValue = columnValue.replaceAll("/\\*.*\\*/","");
        val xssArr = XSS_STR.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in xssArr.indices) {
            if (value.indexOf(xssArr[i]) > -1) {
                log.error("请注意，存在SQL注入关键词---> {}", xssArr[i])
                log.error("请注意，值可能存在SQL注入风险!---> {}", value)
                throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
            }
        }
        //update-begin-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
        if (customXssString != null) {
            val xssArr2 = customXssString.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in xssArr2.indices) {
                if (value.indexOf(xssArr2[i]) > -1) {
                    log.error("请注意，存在SQL注入关键词---> {}", xssArr2[i])
                    log.error("请注意，值可能存在SQL注入风险!---> {}", value)
                    throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
                }
            }
        }
        //update-end-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
        if (Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)) {
            throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
        }
        return
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     * @param values
     * @return
     */
    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     * @param values
     */
    @JvmOverloads
    fun filterContent(values: Array<String?>, customXssString: String? = null) {
        val xssArr = XSS_STR.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (value in values) {
            var value = value
            if (value == null || "" == value) {
                return
            }
            // 校验sql注释 不允许有sql注释
            checkSqlAnnotation(value)
            // 统一转为小写
            value = value.lowercase(Locale.getDefault())

            //SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
            //columnValue = columnValue.replaceAll("/\\*.*\\*/","");
            for (i in xssArr.indices) {
                if (value.indexOf(xssArr[i]) > -1) {
                    log.error("请注意，存在SQL注入关键词---> {}", xssArr[i])
                    log.error("请注意，值可能存在SQL注入风险!---> {}", value)
                    throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
                }
            }
            //update-begin-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
            if (customXssString != null) {
                val xssArr2 = customXssString.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in xssArr2.indices) {
                    if (value.indexOf(xssArr2[i]) > -1) {
                        log.error("请注意，存在SQL注入关键词---> {}", xssArr2[i])
                        log.error("请注意，值可能存在SQL注入风险!---> {}", value)
                        throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
                    }
                }
            }
            //update-end-author:taoyan date:2022-7-13 for: 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
            if (Pattern.matches(SHOW_TABLES, value) || Pattern.matches(
                    REGULAR_EXPRE_USER, value
                )
            ) {
                throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
            }
        }
        return
    }

    /**
     * 【提醒：不通用】
     * 仅用于字典条件SQL参数，注入过滤
     *
     * @param value
     * @return
     */
    //@Deprecated
    fun specialFilterContentForDictSql(value: String?) {
        var value = value
        val specialXssStr =
            " exec |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract| insert | select | delete | update | drop | count | chr | mid | master | truncate | char | declare |;|+|user()"
        val xssArr = specialXssStr.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (value == null || "" == value) {
            return
        }
        // 校验sql注释 不允许有sql注释
        checkSqlAnnotation(value)
        // 统一转为小写
        value = value.lowercase(Locale.getDefault())

        //SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
        //columnValue = columnValue.replaceAll("/\\*.*\\*/","");
        for (i in xssArr.indices) {
            if (value.indexOf(xssArr[i]) > -1 || value.startsWith(xssArr[i].trim { it <= ' ' })) {
                log.error("请注意，存在SQL注入关键词---> {}", xssArr[i])
                log.error("请注意，值可能存在SQL注入风险!---> {}", value)
                throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
            }
        }
        if (Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)) {
            throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
        }
        return
    }


    /**
     * 【提醒：不通用】
     * 仅用于Online报表SQL解析，注入过滤
     * @param value
     * @return
     */
    //@Deprecated
    fun specialFilterContentForOnlineReport(value: String?) {
        var value = value
        val specialXssStr =
            " exec |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract| insert | delete | update | drop | chr | mid | master | truncate | char | declare |user()"
        val xssArr = specialXssStr.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (value == null || "" == value) {
            return
        }
        // 校验sql注释 不允许有sql注释
        checkSqlAnnotation(value)
        // 统一转为小写
        value = value.lowercase(Locale.getDefault())

        //SQL注入检测存在绕过风险 https://gitee.com/jeecg/jeecg-boot/issues/I4NZGE
        //columnValue = columnValue.replaceAll("/\\*.*\\*/"," ");
        for (i in xssArr.indices) {
            if (value.indexOf(xssArr[i]) > -1 || value.startsWith(xssArr[i].trim { it <= ' ' })) {
                log.error("请注意，存在SQL注入关键词---> {}", xssArr[i])
                log.error("请注意，值可能存在SQL注入风险!---> {}", value)
                throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
            }
        }

        if (Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)) {
            throw RuntimeException("请注意，值可能存在SQL注入风险!--->$value")
        }
        return
    }


    /**
     * 判断给定的字段是不是类中的属性
     * @param field 字段名
     * @param clazz 类对象
     * @return
     */
    fun isClassField(field: String?, clazz: Class<*>): Boolean {
        val fields = clazz.declaredFields
        for (i in fields.indices) {
            val fieldName = fields[i].name
            val tableColumnName = StrUtil.toUnderlineCase(fieldName)
            if (fieldName.equals(field, ignoreCase = true) || tableColumnName.equals(field, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断给定的多个字段是不是类中的属性
     * @param fieldSet 字段名set
     * @param clazz 类对象
     * @return
     */
    fun isClassField(fieldSet: Set<String?>, clazz: Class<*>): Boolean {
        val fields = clazz.declaredFields
        for (field in fieldSet) {
            var exist = false
            for (i in fields.indices) {
                val fieldName = fields[i].name
                val tableColumnName = StrUtil.toUnderlineCase(fieldName)
                if (fieldName.equals(field, ignoreCase = true) || tableColumnName.equals(field, ignoreCase = true)) {
                    exist = true
                    break
                }
            }
            if (!exist) {
                return false
            }
        }
        return true
    }

    /**
     * 校验是否有sql注释
     * @return
     */
    fun checkSqlAnnotation(str: String) {
        val matcher = SQL_ANNOTATION.matcher(str)
        if (matcher.find()) {
            val error = "请注意，值可能存在SQL注入风险---> \\*.*\\"
            log.error(error)
            throw RuntimeException(error)
        }
    }
}
