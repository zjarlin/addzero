package site.addzero.enums

enum class ErrorEnum(
    val code: Int,

    val msg: String
) {

    /**
     * 用户没有找到
     */
    USER_NOT_FIND(40010, "用户没有找到"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(40011, "用户名或密码错误"),

    /**
     * 用户名或邮箱已存在
     */
    USERNAME_OR_EMAIL_ALREADY_EXISTS(40012, "用户名或邮箱已存在"),

    /**
     * 验证码不存在
     */
    EMAIL_CODE_IS_NOT_EXIST(40013, "邮箱验证码不存在"),

    /**
     * 验证码不正确
     */
    EMAIL_CODE_IS_NOT_TRUE(40014, "邮箱验证码不正确"),

    /**
     * 旧密码不正确
     */
    OLD_PASSWORD_IS_NOT_TRUE(40015, "旧密码不正确"),


    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功！"),

    /**
     * 用户未登录
     */
    USER_NOT_LOGGED_IN(401, "用户未登录！"),

    /**
     * 无权限
     */
    UNAUTHORIZED(403, "无权限访问！"),

    /**
     * 参数不合法
     */
    INVALID_PARAMETER(400, "参数不合法！"),

    /**
     * 数据未找到
     */
    DATA_NOT_FOUND(404, "数据未找到！"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(409, "数据已存在！"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误！"),

    /**
     * 业务逻辑错误
     */
    BUSINESS_LOGIC_ERROR(1001, "业务逻辑错误！"),

    /**
     * 验证码无效
     */
    INVALID_CAPTCHA(1002, "验证码无效！"),

    /**
     * 操作失败
     */
    OPERATION_FAILED(1003, "操作失败，请重试！"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(1004, "请求超时，请稍后再试！"),

    /**
     * 账号封禁
     */
    ACCOUNT_BANNED(1005, "账号已被封禁，请联系客服！")
}
