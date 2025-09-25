//package site.addzero.lib_adpator.controller_advice
//
//import org.springframework.stereotype.Component
//import site.addzero.entity.Res
//import site.addzero.enums.ErrorEnum
//import site.addzero.web.infra.advice.inter.AbsRes
//
//@Component
//class ResImpl: AbsRes<Res<*>> {
//    override fun success(data: Any?): Res<*> {
//        return Res.success(data)
//    }
//
//    override fun fail(data: Any?): Res<*> {
//        return Res.fail(ErrorEnum.INTERNAL_SERVER_ERROR)
//    }
//
//
//}
