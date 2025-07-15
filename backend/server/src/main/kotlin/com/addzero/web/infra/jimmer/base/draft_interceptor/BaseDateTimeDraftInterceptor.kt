package com.addzero.web.infra.jimmer.base.draft_interceptor

import com.addzero.model.common.BaseDateTime
import com.addzero.model.common.BaseDateTimeDraft
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BaseDateTimeDraftInterceptor : DraftInterceptor<BaseDateTime, BaseDateTimeDraft> {

    override fun beforeSave(draft: BaseDateTimeDraft, original: BaseDateTime?) {

        val now = LocalDateTime.now()
        if (!isLoaded(draft, BaseDateTimeDraft::updateTime)) {
            draft.updateTime = now
        }


        if (original === null) {
            if (!isLoaded(draft, BaseDateTimeDraft::createTime)) {
                draft.createTime = now
            }

        }


    }
}