package com.iwdael.livedatabus.compiler

import com.iwdael.livedatabus.annotation.Observe
import com.iwdael.livedatabus.annotation.ObserveSticky
import java.lang.StringBuilder

class KotlinGenerator(val bus: LiveDataBus) {

    private val header = "package ${bus.packageName}\n"
    private val packages = arrayListOf<String>(
        "import ${bus.packageName}.${bus.targetClassName}\n",
        "import com.iwdael.livedatabus.LiveDataBus\n",
        "import com.iwdael.livedatabus.ObserveLiveDataBus\n\n\n"
    )
    private val class_header =
        "/**\n" +
                " * author : Iwdael\n" +
                " * e-mail : iwdael@outlook.com\n" +
                " * desc   : This document cannot be modified\n" +
                " */\n" +
                "class ${bus.generatedClassName} : ObserveLiveDataBus<${bus.targetClassName}> {\n"

    private val class_footer = "\n}"
    fun generate(): String {
        return header + packages.joinToString(separator = "") +
                class_header +
                subscribe() +
                subscribeEvent() +
                subscribeStickyEvent() +
                class_footer +
                "\n\n\n\n"
    }

    private fun subscribeEvent(): String {
        val builder = StringBuilder()
        bus.eventMethods.forEach {
            val type = it.getAnnotation(Observe::class.java)?.value
            if (type?.isEmpty() == true)
                builder.append(
                    "    private fun observe${it.getName().firstUpper()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with(" +
                            "${
                                it.getParameters()[0].asType()
                                    .toString().javaFullClass2KotlinShotClass()
                            }::class.java)\n" +
                            "            .observe(owner) { owner.${it.getName()}(it) }\n" +
                            "    }\n\n\n"
                )
            else
                builder.append(
                    "    private fun observe${it.getName().firstUpper()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with<${
                                it.getParameters()[0].asType()
                                    .toString().javaFullClass2KotlinShotClass()
                            }>(\"${type}\")\n" +
                            "            .observe(owner) { owner.${it.getName()}(it) }\n" +
                            "    }\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun subscribeStickyEvent(): String {
        val builder = StringBuilder()
        bus.stickyEventMethods.forEach {
            val type = it.getAnnotation(ObserveSticky::class.java)?.value
            if (type?.isEmpty() == true)
                builder.append(
                    "    private fun observe${it.getName().firstUpper()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with(${
                                it.getParameters()[0].asType()
                                    .toString().javaFullClass2KotlinShotClass()
                            }::class.java)\n" +
                            "            .observeSticky(owner) { owner.${it.getName()}(it) }\n" +
                            "    }\n\n\n"
                )
            else
                builder.append(
                    "    private fun observe${it.getName().firstUpper()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with<${
                                it.getParameters()[0].asType()
                                    .toString().javaFullClass2KotlinShotClass()
                            }>(\"${type}\")\n" +
                            "            .observeSticky(owner) { owner.${it.getName()}(it) }\n" +
                            "    }\n\n\n"
                )
        }
        return builder.toString()
    }

    fun subscribe(): String {
        val builder = StringBuilder()
        builder.append(
            "    override fun observe(owner: ${bus.targetClassName}): ${bus.generatedClassName} {\n"
        )
        bus.stickyEventMethods.forEach {
            builder.append("        observe${it.getName().firstUpper()}(owner)\n")
        }
        bus.eventMethods.forEach {
            builder.append("        observe${it.getName().firstUpper()}(owner)\n")
        }
        builder.append("        return this\n")
        builder.append("    }\n\n\n")
        return builder.toString()
    }

}
