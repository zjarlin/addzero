package com.addzero.web.infra.jimmer.base.exhandler.autoaddcol

interface AutoAddColStrategy {
    fun canHandle(message: String?, causeMessage: String?): Boolean?
    fun handle(message: String?, causeMessage: String?): Any?
}