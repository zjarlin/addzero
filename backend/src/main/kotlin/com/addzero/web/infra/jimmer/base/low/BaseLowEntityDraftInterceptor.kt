//package com.addzero.web.infra.jimmer.base.low
//
//import cn.dev33.satoken.stp.StpUtil
//import com.addzero.common.kt_util.isBlank
//import com.addzero.web.infra.jimmer.base.baseentity.BaseEntityDraft
//import org.babyfish.jimmer.kt.isLoaded
//import org.babyfish.jimmer.sql.DraftInterceptor
//import org.springframework.stereotype.Component
//import java.time.LocalDateTime
//
//@Component
//class BaseLowEntityDraftInterceptor : DraftInterceptor<BaseLowEntity, BaseLowEntityDraft> {
//
//    override fun beforeSave(draft: BaseLowEntityDraft, original: BaseLowEntity?) {
//        val loginIdAsString = StpUtil.getLoginIdAsLong()
//        val now = LocalDateTime.now()
//        if (!isLoaded(draft, BaseLowEntityDraft::updateDate)) {
//            draft.updateDate = now
//        }
//
//        if (!isLoaded(draft, BaseLowEntityDraft::updateUser)) {
//            draft.updateUser = loginIdAsString
//        }
//
//        if (original === null) {
//            if (!isLoaded(draft, BaseLowEntityDraft::createDate)) {
//                draft.createDate = now
//            }
//
//            if (!isLoaded(draft, BaseLowEntityDraft::createUser)) {
//                draft.createUser = loginIdAsString
//            }
//        }
//    }
//}