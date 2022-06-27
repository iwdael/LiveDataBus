package com.iwdael.livedatabus.compiler2

import com.iwdael.annotationprocessorparser.Class
import com.iwdael.livedatabus.annotation.*
import com.iwdael.livedatabus.compiler2.Maker.Companion.TAB
import com.squareup.javapoet.*
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class LiveDataBusMaker(val clazz: Class) : Maker {
    override val `package` = clazz.`package`.name
    override val packageGenerator = "${clazz.`package`.name}.livedatabus"
    override val className = clazz.name
    override val classNameGenerator = "${clazz.name}LiveDataBus"

    data class Tmf(val typeSpec: TypeSpec, val methodSpec: MethodSpec, val fieldSpec: FieldSpec?)

    override fun make(filer: Filer) {

        val observe = MethodSpec.methodBuilder("observe")
            .addParameter(ClassName.get(`package`, className), "owner")
            .apply {
                clazz.methods
                    .filter { it.getAnnotation(Observe::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        addStatement(
                            CodeBlock.of(
                                "observe${it.name}_${
                                    ClassName.bestGuess(it.parameter.first().type).simpleName()
                                }(owner)"
                            )
                        )
                    }

                clazz.methods
                    .filter { it.getAnnotation(ObserveSticky::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        addStatement(
                            CodeBlock.of(
                                "observe${it.name}_${
                                    ClassName.bestGuess(it.parameter.first().type).simpleName()
                                }(owner)"
                            )
                        )
                    }

                clazz.methods
                    .filter { it.getAnnotation(ObserveForever::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        addStatement(
                            CodeBlock.of(
                                "observe${it.name}_${
                                    ClassName.bestGuess(it.parameter.first().type).simpleName()
                                }(owner)"
                            )
                        )
                    }

                clazz.methods
                    .filter { it.getAnnotation(ObserveForeverSticky::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        addStatement(
                            CodeBlock.of(
                                "observe${it.name}_${
                                    ClassName.bestGuess(it.parameter.first().type).simpleName()
                                }(owner)"
                            )
                        )
                    }
            }
            .addStatement("return this")
            .addModifiers(Modifier.PUBLIC)
            .returns(
                ParameterizedTypeName.get(
                    ClassName.get("com.iwdael.livedatabus", "ObserveLiveDataBus"),
                    ClassName.get(`package`, className)
                )
            )
            .build()

        val methods = mutableListOf<Tmf>()
            //ObserveSticky
            .apply {
                clazz.methods
                    .filter { it.getAnnotation(ObserveSticky::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        val clazz =
                            "Observe${it.name}_${
                                ClassName.bestGuess(it.parameter.first().type).simpleName()
                            }"
                        val method = "observe${it.name}_${
                            ClassName.bestGuess(it.parameter.first().type).simpleName()
                        }"
                        val typeSpec = TypeSpec.classBuilder(clazz)
                            .addSuperinterface(
                                ParameterizedTypeName.get(
                                    ClassName.get(
                                        "androidx.lifecycle",
                                        "Observer"
                                    ),
                                    ClassName.bestGuess(it.parameter.first().type)
                                )
                            )
                            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                            .addField(
                                FieldSpec
                                    .builder(ClassName.get(`package`, className), "owner")
                                    .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                                    .build()
                            )
                            .addMethod(
                                MethodSpec
                                    .constructorBuilder()
                                    .addParameter(ClassName.get(`package`, className), "owner")
                                    .addStatement("this.owner = owner")
                                    .build()
                            )
                            .addMethod(
                                MethodSpec.methodBuilder("onChanged")
                                    .addModifiers(Modifier.PUBLIC)
                                    .addParameter(
                                        ClassName.bestGuess(it.parameter.first().type),
                                        "it"
                                    )
                                    .apply {
                                        val annotation =
                                            it.getAnnotation(ObserveSticky::class.java)!!
                                        if (annotation.dispatcher == Dispatcher.Main) {
                                            addStatement("this.owner.${it.name}(it)")
                                        } else {
                                            addStatement(
                                                "runOnBackgroundThread(new \$T() {\n" +
                                                        "${TAB}@Override\n" +
                                                        "${TAB}public \$T invoke() {\n" +
                                                        "${TAB}${TAB}owner.${it.name}(it);\n" +
                                                        "${TAB}${TAB}return null;\n" +
                                                        "${TAB}}\n" +
                                                        "})",
                                                ClassName.get("kotlin.jvm.functions", "Function0"),
                                                ClassName.get("kotlin", "Unit")
                                            )
                                        }
                                    }

                                    .build()
                            )
                            .build()
                        val fieldSpec: FieldSpec? = null
                        val methodSpec = MethodSpec.methodBuilder(method)
                            .addParameter(ClassName.get(`package`, className), "owner")
                            .addModifiers(Modifier.PRIVATE)
                            .apply {
                                val annotation = it.getAnnotation(ObserveSticky::class.java)!!
                                if (annotation.value.isEmpty())
                                    addStatement(
                                        "\$T.with(\$T.class).observeSticky(owner, new \$T(owner))",
                                        ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                        ClassName.bestGuess(it.parameter.first().type),
                                        ClassName.get(
                                            "${packageGenerator}.${classNameGenerator}",
                                            clazz
                                        )
                                    )
                                else
                                    addStatement(
                                        "\$T.<\$T>with(\"${annotation.value}\").observeSticky(owner, new \$T(owner))",
                                        ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                        ClassName.bestGuess(it.parameter.first().type),
                                        ClassName.get(
                                            "${packageGenerator}.${classNameGenerator}",
                                            clazz
                                        )
                                    )
                            }
                            .build()
                        add(Tmf(typeSpec, methodSpec, fieldSpec))
                    }
            }
            //Observe
            .apply {
                clazz.methods
                    .filter { it.getAnnotation(Observe::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        val clazz =
                            "Observe${it.name}_${
                                ClassName.bestGuess(it.parameter.first().type).simpleName()
                            }"
                        val method = "observe${it.name}_${
                            ClassName.bestGuess(it.parameter.first().type).simpleName()
                        }"
                        val typeSpec = TypeSpec.classBuilder(clazz)
                            .addSuperinterface(
                                ParameterizedTypeName.get(
                                    ClassName.get(
                                        "androidx.lifecycle",
                                        "Observer"
                                    ),
                                    ClassName.bestGuess(it.parameter.first().type)
                                )
                            )
                            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                            .addField(
                                FieldSpec
                                    .builder(ClassName.get(`package`, className), "owner")
                                    .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                                    .build()
                            )
                            .addMethod(
                                MethodSpec
                                    .constructorBuilder()
                                    .addParameter(ClassName.get(`package`, className), "owner")
                                    .addStatement("this.owner = owner")
                                    .build()
                            )
                            .addMethod(
                                MethodSpec.methodBuilder("onChanged")
                                    .addModifiers(Modifier.PUBLIC)
                                    .addParameter(
                                        ClassName.bestGuess(it.parameter.first().type),
                                        "it"
                                    )
                                    .apply {
                                        val annotation =
                                            it.getAnnotation(Observe::class.java)!!
                                        if (annotation.dispatcher == Dispatcher.Main) {
                                            addStatement("this.owner.${it.name}(it)")
                                        } else {
                                            addStatement(
                                                "runOnBackgroundThread(new \$T() {\n" +
                                                        "${TAB}@Override\n" +
                                                        "${TAB}public \$T invoke() {\n" +
                                                        "${TAB}${TAB}owner.${it.name}(it);\n" +
                                                        "${TAB}${TAB}return null;\n" +
                                                        "${TAB}}\n" +
                                                        "})",
                                                ClassName.get("kotlin.jvm.functions", "Function0"),
                                                ClassName.get("kotlin", "Unit")
                                            )
                                        }
                                    }

                                    .build()
                            )
                            .build()
                        val fieldSpec: FieldSpec? = null
                        val methodSpec = MethodSpec.methodBuilder(method)
                            .addParameter(ClassName.get(`package`, className), "owner")
                            .addModifiers(Modifier.PRIVATE)
                            .apply {
                                val annotation =
                                    it.getAnnotation(Observe::class.java)!!
                                if (annotation.value.isEmpty())
                                    addStatement(
                                        "\$T.with(\$T.class).observe(owner, new \$T(owner))",
                                        ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                        ClassName.bestGuess(it.parameter.first().type),
                                        ClassName.get(
                                            "${packageGenerator}.${classNameGenerator}",
                                            clazz
                                        )
                                    )
                                else addStatement(
                                    "\$T.<\$T>with(\"${annotation.value}\").observe(owner, new \$T(owner))",
                                    ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                    ClassName.bestGuess(it.parameter.first().type),
                                    ClassName.get(
                                        "${packageGenerator}.${classNameGenerator}",
                                        clazz
                                    )
                                )
                            }
                            .build()
                        add(Tmf(typeSpec, methodSpec, fieldSpec))
                    }
            }
            //ObserveForeverSticky
            .apply {
                clazz.methods
                    .filter { it.getAnnotation(ObserveForeverSticky::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        val clazz =
                            "Observe${it.name}_${
                                ClassName.bestGuess(it.parameter.first().type).simpleName()
                            }"
                        val method = "observe${it.name}_${
                            ClassName.bestGuess(it.parameter.first().type).simpleName()
                        }"
                        val typeSpec = TypeSpec.classBuilder(clazz)
                            .addSuperinterface(
                                ParameterizedTypeName.get(
                                    ClassName.get(
                                        "androidx.lifecycle",
                                        "Observer"
                                    ),
                                    ClassName.bestGuess(it.parameter.first().type)
                                )
                            )
                            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                            .addField(
                                FieldSpec
                                    .builder(ClassName.get(`package`, className), "owner")
                                    .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                                    .build()
                            )
                            .addMethod(
                                MethodSpec
                                    .constructorBuilder()
                                    .addParameter(ClassName.get(`package`, className), "owner")
                                    .addStatement("this.owner = owner")
                                    .build()
                            )
                            .addMethod(
                                MethodSpec.methodBuilder("onChanged")
                                    .addModifiers(Modifier.PUBLIC)
                                    .addParameter(
                                        ClassName.bestGuess(it.parameter.first().type),
                                        "it"
                                    )
                                    .apply {
                                        val annotation =
                                            it.getAnnotation(ObserveForeverSticky::class.java)!!
                                        if (annotation.dispatcher == Dispatcher.Main) {
                                            addStatement("this.owner.${it.name}(it)")
                                        } else {
                                            addStatement(
                                                "runOnBackgroundThread(new \$T() {\n" +
                                                        "${TAB}@Override\n" +
                                                        "${TAB}public \$T invoke() {\n" +
                                                        "${TAB}${TAB}owner.${it.name}(it);\n" +
                                                        "${TAB}${TAB}return null;\n" +
                                                        "${TAB}}\n" +
                                                        "})",
                                                ClassName.get("kotlin.jvm.functions", "Function0"),
                                                ClassName.get("kotlin", "Unit")

                                            )
                                        }
                                    }

                                    .build()
                            )
                            .build()
                        val fieldSpec: FieldSpec =
                            FieldSpec.builder(ClassName.bestGuess(clazz), method)
                                .addModifiers(Modifier.PRIVATE)
                                .initializer("null")
                                .build()
                        val methodSpec = MethodSpec.methodBuilder(method)
                            .addParameter(ClassName.get(`package`, className), "owner")
                            .addModifiers(Modifier.PRIVATE)
                            .apply {
                                val annotation =
                                    it.getAnnotation(ObserveForeverSticky::class.java)!!
                                addStatement(
                                    "this.${method} = new \$T(owner)",
                                    ClassName.get(
                                        "${packageGenerator}.${classNameGenerator}",
                                        clazz
                                    )
                                )
                                if (annotation.value.isEmpty())
                                    addStatement(
                                        "\$T.with(\$T.class).observeForeverSticky(${method})",
                                        ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                        ClassName.bestGuess(it.parameter.first().type),
                                    )
                                else
                                    addStatement(
                                        "\$T.<\$T>with(\"${annotation.value}\").observeForeverSticky(${method})",
                                        ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                        ClassName.bestGuess(it.parameter.first().type),
                                    )
                            }
                            .build()
                        add(Tmf(typeSpec, methodSpec, fieldSpec))
                    }
            }
            //ObserveForever
            .apply {
                clazz.methods
                    .filter { it.getAnnotation(ObserveForever::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        val clazz =
                            "Observe${it.name}_${
                                ClassName.bestGuess(it.parameter.first().type).simpleName()
                            }"
                        val method = "observe${it.name}_${
                            ClassName.bestGuess(it.parameter.first().type).simpleName()
                        }"
                        val typeSpec = TypeSpec.classBuilder(clazz)
                            .addSuperinterface(
                                ParameterizedTypeName.get(
                                    ClassName.get(
                                        "androidx.lifecycle",
                                        "Observer"
                                    ),
                                    ClassName.bestGuess(it.parameter.first().type)
                                )
                            )
                            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                            .addField(
                                FieldSpec
                                    .builder(ClassName.get(`package`, className), "owner")
                                    .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                                    .build()
                            )
                            .addMethod(
                                MethodSpec
                                    .constructorBuilder()
                                    .addParameter(ClassName.get(`package`, className), "owner")
                                    .addStatement("this.owner = owner")
                                    .build()
                            )
                            .addMethod(
                                MethodSpec.methodBuilder("onChanged")
                                    .addModifiers(Modifier.PUBLIC)
                                    .addParameter(
                                        ClassName.bestGuess(it.parameter.first().type),
                                        "it"
                                    )
                                    .apply {
                                        val annotation =
                                            it.getAnnotation(ObserveForever::class.java)!!
                                        if (annotation.dispatcher == Dispatcher.Main) {
                                            addStatement("this.owner.${it.name}(it)")
                                        } else {
                                            addStatement(
                                                "runOnBackgroundThread(new \$T() {\n" +
                                                        "${TAB}@Override\n" +
                                                        "${TAB}public \$T invoke() {\n" +
                                                        "${TAB}${TAB}owner.${it.name}(it);\n" +
                                                        "${TAB}${TAB}return null;\n" +
                                                        "${TAB}}\n" +
                                                        "})",
                                                ClassName.get("kotlin.jvm.functions", "Function0"),
                                                ClassName.get("kotlin", "Unit")
                                            )
                                        }
                                    }
                                    .build()
                            )
                            .build()
                        val fieldSpec: FieldSpec =
                            FieldSpec.builder(ClassName.bestGuess(clazz), method)
                                .addModifiers(Modifier.PRIVATE)
                                .initializer("null")
                                .build()
                        val methodSpec = MethodSpec.methodBuilder(method)
                            .addParameter(ClassName.get(`package`, className), "owner")
                            .addModifiers(Modifier.PRIVATE)
                            .apply {
                                val annotation = it.getAnnotation(ObserveForever::class.java)!!
                                addStatement(
                                    "this.${method} = new \$T(owner)",
                                    ClassName.get(
                                        "${packageGenerator}.${classNameGenerator}",
                                        clazz
                                    )
                                )
                                if (annotation.value.isEmpty())
                                    addStatement(
                                        "\$T.with(\$T.class).observeForever(${method})",
                                        ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                        ClassName.bestGuess(it.parameter.first().type),
                                    )
                                else addStatement(
                                    "\$T.<\$T>with(\"${annotation.value}\").observeForever(${method})",
                                    ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                    ClassName.bestGuess(it.parameter.first().type),
                                )
                            }
                            .build()
                        add(Tmf(typeSpec, methodSpec, fieldSpec))
                    }
            }

        val removeAllObserver = MethodSpec.methodBuilder("removeAllObserver")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .apply {
                clazz.methods
                    .filter { it.getAnnotation(ObserveForeverSticky::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        val method = "observe${it.name}_${
                            ClassName.bestGuess(it.parameter.first().type).simpleName()
                        }"
                        val annotation = it.getAnnotation(ObserveForeverSticky::class.java)!!
                        beginControlFlow(CodeBlock.of("if(${method} != null)"))
                        if (annotation.value.isEmpty())
                            addStatement(
                                "\$T.with(\$T.class).removeObserver(${method})",
                                ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                ClassName.bestGuess(it.parameter.first().type)
                            )
                        else
                            addStatement(
                                "\$T.<\$T>with(\"${annotation.value}\").removeObserver(${method})",
                                ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                ClassName.bestGuess(it.parameter.first().type)
                            )

                        endControlFlow()
                    }

                clazz.methods
                    .filter { it.getAnnotation(ObserveForever::class.java) != null }
                    .filter { if (it.parameter.size != 1) throw Exception("${it.owner}.${it.name} can only have one parameter") else true }
                    .forEach {
                        val method = "observe${it.name}_${
                            ClassName.bestGuess(it.parameter.first().type).simpleName()
                        }"
                        val annotation = it.getAnnotation(ObserveForever::class.java)!!
                        beginControlFlow(CodeBlock.of("if(${method} != null)"))
                        if (annotation.value.isEmpty())
                            addStatement(
                                "\$T.with(\$T.class).removeObserver(${method})",
                                ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                ClassName.bestGuess(it.parameter.first().type)
                            )
                        else
                            addStatement(
                                "\$T.<\$T>with(\"${annotation.value}\").removeObserver(${method})",
                                ClassName.get("com.iwdael.livedatabus", "LiveDataBus"),
                                ClassName.bestGuess(it.parameter.first().type)
                            )
                        endControlFlow()
                    }
            }
            .build()
        JavaFile
            .builder(
                packageGenerator,
                TypeSpec
                    .classBuilder(classNameGenerator)
                    .addSuperinterface(
                        ParameterizedTypeName.get(
                            ClassName.get("com.iwdael.livedatabus", "ObserveLiveDataBus"),
                            ClassName.get(`package`, className)
                        )
                    )
                    .addMethod(observe)
                    .addMethod(removeAllObserver)
                    .apply {
                        methods.forEach {
                            this.addType(it.typeSpec)
                            this.addMethod(it.methodSpec)
                            it.fieldSpec?.let { this.addField(it) }
                        }
                    }
                    .build()
            )
            .indent(TAB)
            .addStaticImport(
                ClassName.bestGuess("com.iwdael.livedatabus.LiveDataBusCompatKt"),
                "runOnBackgroundThread"
            )
            .build()
            .writeTo(filer)
    }
}