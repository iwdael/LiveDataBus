package com.iwdael.livedatabus.compiler

import com.iwdael.livedatabus.annotation.*
import java.lang.StringBuilder

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class JavaGenerator(val bus: LiveDataBus) {

    private val header = "package ${bus.packageName};\n"
    private val packages = arrayListOf<String>(
        "import ${bus.packageName}.${bus.targetClassName};\n",
        "import com.iwdael.livedatabus.LiveDataBus;\n",
        "import androidx.lifecycle.Observer;\n",
        "import kotlin.Unit;\n",
        "import kotlin.jvm.functions.Function0;\n",
        "import static com.iwdael.livedatabus.LiveDataBusCompatKt.runOnBackgroundThread;\n",
        "import com.iwdael.livedatabus.ObserveLiveDataBus;\n\n\n\n\n"
    )
    private val class_header =
        "/**\n" +
                " * author : Iwdael\n" +
                " * e-mail : iwdael@outlook.com\n" +
                " * desc   : This document cannot be modified\n" +
                " */\n" +
                "public class ${bus.generatedClassName} implements ObserveLiveDataBus<${bus.targetClassName}> {\n"

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
                "\n\n\n\n\n\n"
    }

    private fun removeObserver(): String {
        val builder = StringBuilder()

        builder.append(
            "    @Override\n" +
                    "    public void removeAllObserver() {\n"
        )
        bus.observeForever.forEach {
            val type = it.getAnnotation(ObserveForever::class.java)?.value
            if (type?.isEmpty() == true) {
                builder.append(
                    "        if(${it.makeVariableName()} != null) \n" +
                            "            LiveDataBus.with(${it.observeType(false)}.class).removeObserver(${it.makeVariableName()}); \n"
                )
            } else {
                builder.append(
                    "        if(${it.makeVariableName()} != null)  \n" +
                            "            LiveDataBus.with(${it.observeType(false)}.class).removeObserver(${it.makeVariableName()}); \n"
                )
            }
        }
        bus.observeForeverSticky.forEach {
            val type = it.getAnnotation(ObserveForever::class.java)?.value
            if (type?.isEmpty() == true) {
                builder.append(
                    "        if(${it.makeVariableName()} != null) \n" +
                            "            LiveDataBus.with(${it.observeType(false)}.class).removeObserver(${it.makeVariableName()}); \n"
                )
            } else {
                builder.append(
                    "        if(${it.makeVariableName()} != null)  \n" +
                            "            LiveDataBus.with(${it.observeType(false)}.class).removeObserver(${it.makeVariableName()}); \n"
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
                "    private class ${it.makeClassName()} implements Observer<${it.observeType(false)}> { \n" +
                        "        private final ${bus.targetClassName} owner;\n" +
                        "\n" +
                        "        public ${it.makeClassName()}(${bus.targetClassName} owner) {\n" +
                        "            this.owner = owner;\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public void onChanged(${it.observeType(false)} it) {\n" +
                        "            ${it.makeCallBack(false,dispatcher != Dispatcher.Main)};\n" +
                        "        }\n" +
                        "    }\n"
            )
            builder.append(
                "    private Observer<String> ${it.makeVariableName()} = null;\n"
            )
            if (type?.isEmpty() == true)
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        ${it.makeVariableName()} = new ${it.makeClassName()}(owner);\n" +
                            "        LiveDataBus.with(${it.observeType(false)}.class)\n" +
                            "                .observeForever(${it.makeVariableName()});\n" +
                            "    }\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        ${it.makeVariableName()} = new ${it.makeClassName()}(owner);\n" +
                            "        LiveDataBus.<${it.observeType(false)}>with(\"${type}\")\n" +
                            "                .observeForever(${it.makeVariableName()});\n" +
                            "    }\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun observeForeverSticky(): String {
        val builder = StringBuilder()
        bus.observeForeverSticky.forEach {
            val type = it.getAnnotation(ObserveForeverSticky::class.java)?.value
            val dispatcher = it.getAnnotation(ObserveForeverSticky::class.java)?.dispatcher
            builder.append(
                "    private class ${it.makeClassName()} implements Observer<${it.observeType(false)}> { \n" +
                        "        private final ${bus.targetClassName} owner;\n" +
                        "\n" +
                        "        public ${it.makeClassName()}(${bus.targetClassName} owner) {\n" +
                        "            this.owner = owner;\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public void onChanged(${it.observeType(false)} it) {\n" +
                        "            ${it.makeCallBack(false,dispatcher != Dispatcher.Main)};\n" +
                        "        }\n" +
                        "    }\n"
            )
            builder.append(
                "    private Observer<String> ${it.makeVariableName()} = null;\n"
            )
            if (type?.isEmpty() == true)
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        ${it.makeVariableName()} = new ${it.makeClassName()}(owner);\n" +
                            "        LiveDataBus.with(${it.observeType(false)}.class)\n" +
                            "                .observeForeverSticky(${it.makeVariableName()});\n" +
                            "    }\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        ${it.makeVariableName()} = new ${it.makeClassName()}(owner);\n" +
                            "        LiveDataBus.<${it.observeType(false)}>with(\"${type}\")\n" +
                            "                .observeForeverSticky(${it.makeVariableName()});\n" +
                            "    }\n\n\n\n\n"
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
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.with(${it.observeType(false)}.class)\n" +
                            "                .observe(owner, new Observer<${it.observeType(false)}>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(${it.observeType(false)} it) {\n" +
                            "                        ${it.makeCallBack(false,dispatcher != Dispatcher.Main)};\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.<${it.observeType(false)}>with(\"${type}\")\n" +
                            "                .observe(owner, new Observer<${it.observeType(false)}>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(${it.observeType(false)} it) {\n" +
                            "                        ${it.makeCallBack(false,dispatcher != Dispatcher.Main)};\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun observeSticky(): String {
        val builder = StringBuilder()
        bus.observeSticky.forEach {
            val type = it.getAnnotation(ObserveSticky::class.java)?.value
            val dispatcher = it.getAnnotation(ObserveSticky::class.java)?.dispatcher
            if (type?.isEmpty() == true)
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.with(${it.observeType(false)}.class)\n" +
                            "                .observeSticky(owner, new Observer<${
                                it.observeType(
                                    false
                                )
                            }>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(${it.observeType(false)} it) {\n" +
                            "                        ${it.makeCallBack(false,dispatcher != Dispatcher.Main)};\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n\n\n"
                )
            else
                builder.append(
                    "    private void ${it.makeVariableName()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.<${it.observeType(false)}>with(\"${type}\")\n" +
                            "                .observeSticky(owner, new Observer<${
                                it.observeType(
                                    false
                                )
                            }>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(${it.observeType(false)} it) {\n" +
                            "                        ${it.makeCallBack(false,dispatcher != Dispatcher.Main)};\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n\n\n"
                )
        }
        return builder.toString()
    }

    private fun subscribe(): String {
        val builder = StringBuilder()
        builder.append(
            "    public ObserveLiveDataBus<${bus.targetClassName}> observe(${bus.targetClassName} owner) {\n"
        )
        bus.observeSticky.forEach {
            builder.append("        ${it.makeVariableName()}(owner);\n")
        }
        bus.observe.forEach {
            builder.append("        ${it.makeVariableName()}(owner);\n")
        }
        bus.observeForeverSticky.forEach {
            builder.append("        ${it.makeVariableName()}(owner);\n")
        }
        bus.observeForever.forEach {
            builder.append("        ${it.makeVariableName()}(owner);\n")
        }
        builder.append("        return this;\n")
        builder.append("    }\n\n\n\n\n")
        return builder.toString()
    }

}
