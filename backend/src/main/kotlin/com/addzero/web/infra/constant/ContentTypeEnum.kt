package com.addzero.web.infra.constant

enum class ContentTypeEnum(val application: String) {
    DOC("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    DOT("application/msword"),
    DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template"),
    XLS("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT("application/vnd.ms-powerpoint"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PDF("application/pdf"),
    TXT("text/plain"),
    GIF("image/gif"),
    JPEG("image/jpeg"),
    JPG("image/jpeg"),
    PNG("image/png"),
    CSS("text/css"),
    HTML("text/html"),
    HTM("text/html"),
    XSL("text/xml"),
    XML("text/xml"),
    MPEG("video/mpeg"),
    MPG("video/mpeg"),
    AVI("video/x-msvideo"),
    MOVIE("video/x-sgi-movie"),
    BIN("application/octet-stream"),
    EXE("application/octet-stream"),
    SO("application/octet-stream"),
    DLL("application/octet-stream"),
    AI("application/postscript"),
    DIR("application/x-director"),
    JS("application/x-javascript"),
    SWF("application/x-shockwave-flash"),
    XHTML("application/xhtml+xml"),
    XHT("application/xhtml+xml"),
    ZIP("application/zip"),
    MID("audio/midi"),
    MIDI("audio/midi"),
    MP3("audio/mpeg"),
    RM("audio/x-pn-realaudio"),
    RPM("audio/x-pn-realaudio-plugin"),
    WAV("audio/x-wav"),
    BMP("image/bmp");

    // 枚举的名称转为小写，作为 jdbcType
    val type: String
        get() = name.lowercase()

    // 枚举名称转换为小写，并自动生成后缀（在前面加点）
    val postfix: String
        get() = ".$type"
}
