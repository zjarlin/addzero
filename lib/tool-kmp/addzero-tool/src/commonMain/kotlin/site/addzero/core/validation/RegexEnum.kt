package site.addzero.core.validation

enum class RegexEnum(val pattern: String, val message: String) {
    PHONE("^1[3-9]\\d{9}$", "请输入正确的手机号"),
    ID_CARD("^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$", "请输入正确的身份证号"),
    EMAIL("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", "请输入正确的邮箱地址"),

    // 密码相关
    PASSWORD("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", "密码必须包含字母和数字，长度8-16位"),
    STRONG_PASSWORD(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        "密码必须包含大小写字母、数字和特殊字符"
    ),

    // 用户名相关
    USERNAME("^[a-zA-Z0-9_-]{4,16}$", "用户名只能包含字母、数字、下划线和横线，长度4-16位"),
    CHINESE_NAME("^[\\u4e00-\\u9fa5]{2,4}$", "请输入2-4个汉字的中文姓名"),

    // 数字相关
    INTEGER("^-?\\d+$", "请输入整数"),
    POSITIVE_INTEGER("^[1-9]\\d*$", "请输入正整数"),
    DECIMAL("^-?\\d+\\.?\\d*$", "请输入数字"),
    MONEY("^\\d+\\.?\\d{0,2}$", "请输入正确的金额格式"),

    // 日期时间相关
    DATE("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", "请输入正确的日期格式：yyyy-MM-dd"),
    TIME("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", "请输入正确的时间格式：HH:mm:ss"),
    TIME_MINUTE("^([01]\\d|2[0-3]):[0-5]\\d$", "请输入正确的时间格式：HH:mm"),
    DATETIME(
        "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\\s([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
        "请输入正确的日期时间格式：yyyy-MM-dd HH:mm:ss"
    ),
    DATETIME_MINUTE(
        "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\\s([01]\\d|2[0-3]):[0-5]\\d$",
        "请输入正确的日期时间格式：yyyy-MM-dd HH:mm"
    ),

    // 网络相关
    URL("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", "请输入正确的URL地址"),
    IP_V4("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", "请输入正确的IPv4地址"),
    MAC("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", "请输入正确的MAC地址"),

    // 文件相关
    IMAGE("\\.(jpg|jpeg|png|gif|bmp|webp)$", "只支持jpg、jpeg、png、gif、bmp、webp格式的图片"),
    VIDEO("\\.(mp4|avi|rmvb|rm|flv|mkv|mov)$", "只支持mp4、avi、rmvb、rm、flv、mkv、mov格式的视频"),
    AUDIO("\\.(mp3|wav|wma|ogg|ape|acc)$", "只支持mp3、wav、wma、ogg、ape、acc格式的音频"),
    DOCUMENT("\\.(doc|docx|xls|xlsx|ppt|pptx|pdf|txt)$", "只支持常用的文档格式"),

    // 其他常用
    CHINESE("^[\\u4e00-\\u9fa5]+$", "只能输入中文"),
    ENGLISH("^[a-zA-Z]+$", "只能输入英文字母"),
    ZIP_CODE("^\\d{6}$", "请输入正确的邮政编码"),
    BANK_CARD("^\\d{16,19}$", "请输入正确的银行卡号"),
    QQ("^[1-9][0-9]{4,}$", "请输入正确的QQ号码"),
    WECHAT("^[a-zA-Z][a-zA-Z\\d_-]{5,19}$", "请输入正确的微信号");

    fun matches(input: String?): Boolean {
        if (input == null) return false
        return input.matches(Regex(pattern))
    }

    companion object Companion {
        fun validate(input: String?, pattern: RegexEnum): Pair<Boolean, String> {
            val matches = pattern.matches(input)
            return Pair(matches, if (matches) "" else pattern.message)
        }
    }

}
