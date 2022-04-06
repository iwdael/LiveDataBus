package com.iwdael.livedatabus.compiler

import com.iwdael.livedatabus.annotation.*
import java.lang.StringBuilder

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class KotlinGenerator(val bus: LiveDataBus) {

    private val header = "package ${bus.packageName}\n"
    private val packages = arrayListOf<String>(
        "import ${bus.packageName}.${bus.targetClassName}\n",
        "import com.iwdael.livedatabus.LiveDataBus\n",
        "import androidx.lifecycle.Observer\n",
        "import com.iwdael.livedatabus.runOnBackgroundThread\n",
        "import com.iwdael.livedatabus.ObserveLiveDataBus\n\n\n\n\n\n"
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
                observe() +
                observeSticky() +
                observeForever() +
                observeForeverSticky() +
                removeObserver() +
                class_footer +
                "\n\n\n\n\n\n\n"
    }

    private fun removeObserver(): String {
        val builder = StringBuilder()
        builder.append("    override fun removeAllObserver() {\n")
        bus.observeForever.forEach {
            val type = it.getAnnotation(ObserveForever::class.java)?.value
            if (type?.isEmpty() == true) {
                builder.append(
                    "        ${it.makeVariableName()}?.let { LiveDataBus.with(${it.observeType(true)}::class.java).removeObserver(it) }\n"
                )
            } else {
                builder.append(
                    "        ${it.makeVariableName()}?.let { LiveDataBus.with(${it.observeType(true)}::class.java).removeObserver(it) }\n"
                )
            }
        }
        bus.observeForeverSticky.forEach {
            val type = it.getAnnotation(ObserveForeverSticky::class.java)?.value
            if (type?.isEmpty() == true) {
                builder.append(
                    "        ${it.makeVariableName()}?.let { LiveDataBus.with(${it.observeType(true)}::class.java).removeObserver(it) }\n"
                )
            } else {
                builder.append(
                    "        ${it.makeVariableName()}?.let { LiveDataBus.with(${it.observeType(true)}::class.java).removeObserver(it) }\n"
                )
            }
        }
        builder.append("    }\n\n\n\n\n")
        return builder.toString()
    }

    private fun observeForever(): String {
        val builder = StringBuilder()
        bus.observeForever.forEach {
            val dispatcher = it.getAnnotation(ObserveForever::class.java)?.dispatcher
            val type = it.getAnnotation(ObserveForever::class.java)?.value
            builder.append(
                "    private class ${it.makeClassName()}" +
                        "(private val owner: ${bus.targetClassName}) : " +
                        "Observer<${it.observeType(true)}> {\n" +
                        "        override fun onChanged(it: ${it.observeType(true)}) {\n" +
                        "            ${it.makeCallBack(true,dispatcher != Dispatcher.Main)}\n" +
                        "        }\n" +
                        "    }\n"
            )
            builder.append(
                "    private var ${it.makeVariableName()}: Observer<${it.observeType(true)}>? = null\n"
            )
            if (type?.isEmpty() == true)
                builder.append(
                    "    private fun ${it.makeVariableName()}(owner: ${bus.targetClassName}) {\n" +
                            "        ${it.makeVariableName()} = ${it.makeClassName()}(owner)\n" +
                            "        LiveDataBus.with(${it.observeType(true)}::class.java)\n" +
                            "            .observeForever(${it.makeVariableName()}!!)\n" +
                            "    }\n\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private fun ${it.makeVariableName()}(owner: ${bus.targetClassName}) {\n" +
                            "        ${it.makeVariableName()} = ${it.makeClassName()}(owner)\n" +
                            "        LiveDataBus.with<${it.observeType(true)}>(\"${type}\")\n" +
                            "            .observeForever(${it.makeVariableName()}!!)\n" +
                            "    }\n\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun observeForeverSticky(): String {
        val builder = StringBuilder()
        bus.observeForeverSticky.forEach {
            val dispatcher = it.getAnnotation(ObserveForeverSticky::class.java)?.dispatcher
            val type = it.getAnnotation(ObserveForeverSticky::class.java)?.value
            builder.append(
                "    private class ${it.makeClassName()}" +
                        "(private val owner: ${bus.targetClassName}) : Observer<${
                            it.observeType(
                                true
                            )
                        }> {\n" +
                        "        override fun onChanged(it: ${it.observeType(true)}) {\n" +
                        "            ${it.makeCallBack(true,dispatcher != Dispatcher.Main)}\n" +
                        "        }\n" +
                        "    }\n"
            )
            builder.append(
                "    private var ${it.makeVariableName()}: Observer<${
                    it.getParameters()[0].asType().toString()
                        .javaFullClass2KotlinShotClass()
                }>? = null\n"
            )
            if (type?.isEmpty() == true)
                builder.append(
                    "    private fun ${it.makeVariableName()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        ${it.makeVariableName()} = ${it.makeClassName()}(owner)\n" +
                            "        LiveDataBus.with(" +
                            "${it.observeType(true)}::class.java)\n" +
                            "            .observeForeverSticky(${it.makeVariableName()}!!)\n" +
                            "    }\n\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private fun ${it.makeVariableName()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        ${it.makeVariableName()} = ${it.makeClassName()}(owner)\n" +
                            "        LiveDataBus.with<${it.observeType(true)}>(\"${type}\")\n" +
                            "            .observeForeverSticky(${it.makeVariableName()}!!)\n" +
                            "    }\n\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun observe(): String {
        val builder = StringBuilder()
        bus.observe.forEach {
            val type = it.getAnnotation(Observe::class.java)?.value
            val dispatcher = it.getAnnotation(Observe::class.java)?.dispatcher
            if (type?.isEmpty() == true)
                builder.append(
                    "    private fun ${it.makeVariableName()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with(${it.observeType(true)}::class.java)\n" +
                            "            .observe(owner) { ${it.makeCallBack(true,dispatcher != Dispatcher.Main)} }\n" +
                            "    }\n\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private fun ${it.makeVariableName()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with<${it.observeType(true)}>(\"${type}\")\n" +
                            "            .observe(owner) { ${it.makeCallBack(true,dispatcher != Dispatcher.Main)} }\n" +
                            "    }\n\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun observeSticky(): String {
        val builder = StringBuilder()
        bus.observeSticky.forEach {
            val dispatcher = it.getAnnotation(ObserveSticky::class.java)?.dispatcher
            val type = it.getAnnotation(ObserveSticky::class.java)?.value
            if (type?.isEmpty() == true)
                builder.append(
                    "    private fun ${it.makeVariableName()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with(${it.observeType(true)}::class.java)\n" +
                            "            .observeSticky(owner) { ${it.makeCallBack(true,dispatcher != Dispatcher.Main)} }\n" +
                            "    }\n\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private fun ${it.makeVariableName()}" +
                            "(owner: ${bus.targetClassName}) {\n" +
                            "        LiveDataBus.with<${it.observeType(true)}>(\"${type}\")\n" +
                            "            .observeSticky(owner) { ${it.makeCallBack(true,dispatcher != Dispatcher.Main)} }\n" +
                            "    }\n\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun subscribe(): String {
        val builder = StringBuilder()
        builder.append(
            "    override fun observe(owner: ${bus.targetClassName}): ${bus.generatedClassName} {\n"
        )
        bus.observeSticky.forEach {
            builder.append("        ${it.makeVariableName()}(owner)\n")
        }
        bus.observe.forEach {
            builder.append("        ${it.makeVariableName()}(owner)\n")
        }
        bus.observeForever.forEach {
            builder.append("        ${it.makeVariableName()}(owner)\n")
        }
        bus.observeForeverSticky.forEach {
            builder.append("        ${it.makeVariableName()}(owner)\n")
        }
        builder.append("        return this\n")
        builder.append("    }\n\n\n\n\n\n")
        return builder.toString()
    }

}
