package com.addzero.web.infra.jimmer.base.exhandler.exception_translate

import org.babyfish.jimmer.sql.exception.SaveException
import org.babyfish.jimmer.sql.runtime.ExceptionTranslator
import org.springframework.stereotype.Component

@Component
class NotUniqueExceptionTranslator :
    ExceptionTranslator<SaveException.NotUnique> {

    override fun translate(
        exception: SaveException.NotUnique,
        args: ExceptionTranslator.Args
    ): Exception? =
        when {
// exception.isMatched(Book::id) ->
//                throw IllegalArgumentException(
//                    "ID为${exception[Book::id]}的书籍已经存在"
//                )

//            true ->
//                throw IllegalArgumentException(
//                    exception.message,exception.cause
//                )

            else ->
                null //不做处理，也可以写作`exception`
        }
}
