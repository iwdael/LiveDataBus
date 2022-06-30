package com.iwdael.livedatabus.compiler

import com.iwdael.annotationprocessorparser.Class
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
class ContentProviderMaker(private val classes: List<Class>) : Maker {
    override val `package` = "com.iwdael.livedatabus"
    override val packageGenerator = "com.iwdael.livedatabus"
    override val className = "LiveDataBusContentProvider"
    override val classNameGenerator = "LiveDataBusContentProvider"

    private fun eventCollection() = FieldSpec
        .builder(
            ParameterizedTypeName.get(
                ClassName.get(Map::class.java),
                ClassName.get(Integer::class.java),
                ClassName.get("com.iwdael.livedatabus", "ObserveLiveDataBus")
            ),
            "eventCollection"
        )
        .addModifiers(Modifier.FINAL)
        .addModifiers(Modifier.PRIVATE)
        .initializer("new \$T<>()", ClassName.get(HashMap::class.java))
        .build()

    private fun observeLiveDataBus() = MethodSpec
        .methodBuilder("observeLiveDataBus")
        .addModifiers(Modifier.PRIVATE)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .returns(ClassName.get("com.iwdael.livedatabus", "ObserveLiveDataBus"))
        .apply {
            if (classes.isEmpty()) return@apply
            classes.forEachIndexed { index, clazz ->
                if (index == 0)
                    beginControlFlow(
                        "if (activity instanceof \$T)",
                        ClassName.get(clazz.`package`.name, clazz.name)
                    )
                else
                    nextControlFlow(
                        "else if (activity instanceof \$T)",
                        ClassName.get(clazz.`package`.name, clazz.name)
                    )
                addStatement(
                    "return new \$T().observe((\$T)activity)",
                    ClassName.get(
                        "${clazz.`package`.name}.livedatabus",
                        "${clazz.name}LiveDataBus"
                    ),
                    ClassName.get(clazz.`package`.name, clazz.name)
                )
            }
            endControlFlow()
            addStatement("return null")
        }
        .build()

    private fun onCreate() = MethodSpec.methodBuilder("onCreate")
        .returns(TypeName.BOOLEAN)
        .addAnnotation(Override::class.java)
        .addModifiers(Modifier.PUBLIC)
        .addStatement(
            "\$T application = (Application) getContext().getApplicationContext()",
            ClassName.get("android.app", "Application")
        )
        .addStatement("application.registerActivityLifecycleCallbacks(this)")

        .addStatement("return false")
        .build()

    private fun update() = MethodSpec.methodBuilder("update")
        .addParameter(ClassName.bestGuess("android.net.Uri"), "uri")
        .addParameter(ClassName.bestGuess("android.content.ContentValues"), "values")
        .addParameter(ClassName.bestGuess("java.lang.String"), "selection")
        .addParameter(
            ArrayTypeName.of(ClassName.get("java.lang", "String")),
            "selectionArgs"
        )
        .returns(TypeName.INT)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addStatement("return -1")
        .build()

    private fun query() = MethodSpec.methodBuilder("query")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.bestGuess("android.net.Uri"), "uri")
        .addParameter(
            ArrayTypeName.of(ClassName.get("java.lang", "String")),
            "projection"
        )
        .addParameter(ClassName.bestGuess("java.lang.String"), "selection")
        .addParameter(
            ArrayTypeName.of(ClassName.get("java.lang", "String")),
            "selectionArgs"
        )
        .addParameter(ClassName.bestGuess("java.lang.String"), "sortOrder")
        .returns(ClassName.get("android.database", "Cursor"))
        .addStatement("return null")
        .build()

    private fun delete() = MethodSpec.methodBuilder("delete")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.bestGuess("android.net.Uri"), "uri")
        .addParameter(ClassName.bestGuess("java.lang.String"), "selection")
        .addParameter(
            ArrayTypeName.of(ClassName.get("java.lang", "String")),
            "selectionArgs"
        )
        .returns(TypeName.INT)
        .addStatement("return -1")
        .build()

    private fun insert() = MethodSpec.methodBuilder("insert")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.bestGuess("android.net.Uri"), "uri")
        .addParameter(ClassName.bestGuess("android.content.ContentValues"), "values")
        .returns(ClassName.bestGuess("android.net.Uri"))
        .addStatement("return null")
        .build()

    private fun getType() = MethodSpec.methodBuilder("getType")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.bestGuess("android.net.Uri"), "uri")
        .returns(String::class.java)
        .addStatement("return null")
        .build()


    private fun onActivityDestroyed() = MethodSpec.methodBuilder("onActivityDestroyed")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .addStatement("ObserveLiveDataBus dataBus = eventCollection.get(activity.hashCode())")
        .beginControlFlow("if (dataBus != null)")
        .addStatement("dataBus.removeAllObserver()")
        .endControlFlow()
        .build()

    private fun onActivityStopped() = MethodSpec.methodBuilder("onActivityStopped")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .build()

    private fun onActivityPaused() = MethodSpec.methodBuilder("onActivityPaused")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .build()

    private fun onActivityResumed() = MethodSpec.methodBuilder("onActivityResumed")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .build()

    private fun onActivityStarted() = MethodSpec.methodBuilder("onActivityStarted")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .build()

    private fun onActivityCreated() = MethodSpec.methodBuilder("onActivityCreated")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override::class.java)
        .addParameter(ClassName.get("android.app", "Activity"), "activity")
        .addParameter(ClassName.get("android.os", "Bundle"), "bundle")
        .addStatement("ObserveLiveDataBus dataBus = observeLiveDataBus(activity)")
        .beginControlFlow("if (dataBus != null)")
        .addStatement("eventCollection.put(activity.hashCode(), dataBus)")
        .endControlFlow()
        .build()

    private fun onActivitySaveInstanceState() =
        MethodSpec.methodBuilder("onActivitySaveInstanceState")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addParameter(ClassName.get("android.app", "Activity"), "activity")
            .addParameter(ClassName.get("android.os", "Bundle"), "bundle")
            .build()

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
                    .addMethod(observeLiveDataBus())
                    .addMethod(onCreate())
                    .addMethod(update())
                    .addMethod(query())
                    .addMethod(delete())
                    .addMethod(insert())
                    .addMethod(getType())
                    .addMethod(onActivityDestroyed())
                    .addMethod(onActivityStopped())
                    .addMethod(onActivityPaused())
                    .addMethod(onActivityResumed())
                    .addMethod(onActivityStarted())
                    .addMethod(onActivityCreated())
                    .addMethod(onActivitySaveInstanceState())
                    .addField(eventCollection())
                    .build()
            )
            .build().writeTo(filer)

    }
}