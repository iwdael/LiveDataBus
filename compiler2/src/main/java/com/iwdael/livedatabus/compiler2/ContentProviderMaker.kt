package com.iwdael.livedatabus.compiler2

import com.squareup.javapoet.*
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class ContentProviderMaker : Maker {
    override val `package` = "com.iwdael.livedatabus2"
    override val packageGenerator = "com.iwdael.livedatabus2"
    override val className = "LiveDataBusContentProvider"
    override val classNameGenerator = "LiveDataBusContentProvider"

    override fun make(filer: Filer) {
        JavaFile
            .builder(
                `package`,
                TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.get("android.content", "ContentProvider"))
                    .addSuperinterface(
                        ClassName.get(
                            "android.app.Application",
                            "ActivityLifecycleCallbacks"
                        )
                    )
                    .addMethod(
                        MethodSpec.methodBuilder("onCreate")
                            .addStatement("return false")
                            .returns(TypeName.BOOLEAN)
                            .build()
                    )
                    .build()
            )
            .build().writeTo(filer)

    }
}