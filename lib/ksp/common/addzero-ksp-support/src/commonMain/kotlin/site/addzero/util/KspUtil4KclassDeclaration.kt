package site.addzero.util

import com.google.devtools.ksp.symbol.KSClassDeclaration


fun KSClassDeclaration.hasProperty(simpleName: String):
        Boolean {
    return this.getAllProperties().any { prop ->
        prop.simpleName.asString() == simpleName
    }
}
