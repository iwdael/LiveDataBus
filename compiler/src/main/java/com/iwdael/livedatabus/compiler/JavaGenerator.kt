package com.iwdael.livedatabus.compiler

import com.iwdael.livedatabus.annotation.Observe
import com.iwdael.livedatabus.annotation.ObserveSticky
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
        "import com.iwdael.livedatabus.ObserveLiveDataBus;\n\n\n"
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
                    "    private void observe${it.getName().firstUpper()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.with(${it.getParameters()[0].asType()}.class)\n" +
                            "                .observe(owner, new Observer<String>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(String s) {\n" +
                            "                        owner.${it.getName()}(s);\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n"
                )
            else
                builder.append(
                    "    private void observe${it.getName().firstUpper()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.<${it.getParameters()[0].asType()}>with(\"${type}\")\n" +
                            "                .observe(owner, new Observer<String>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(String s) {\n" +
                            "                        owner.${it.getName()}(s);\n" +
                            "                    }\n" +
                            "                });\n" +
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
                    "    private void observe${it.getName().firstUpper()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.with(${it.getParameters()[0].asType()}.class)\n" +
                            "                .observeSticky(owner, new Observer<String>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(String s) {\n" +
                            "                        owner.${it.getName()}(s);\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n"
                )
            else
                builder.append(
                    "    private void observe${it.getName().firstUpper()}(${bus.targetClassName} owner) {\n" +
                            "        LiveDataBus.<${it.getParameters()[0].asType()}>with(\"${type}\")\n" +
                            "                .observeSticky(owner, new Observer<String>() {\n" +
                            "                    @Override\n" +
                            "                    public void onChanged(String s) {\n" +
                            "                        owner.${it.getName()}(s);\n" +
                            "                    }\n" +
                            "                });\n" +
                            "    }\n\n\n"
                )
        }
        return builder.toString()
    }

    fun subscribe(): String {
        val builder = StringBuilder()
        builder.append(
            "    public ObserveLiveDataBus<${bus.targetClassName}> observe(${bus.targetClassName} owner) {\n"
        )
        bus.stickyEventMethods.forEach {
            builder.append("        observe${it.getName().firstUpper()}(owner);\n")
        }
        bus.eventMethods.forEach {
            builder.append("        observe${it.getName().firstUpper()}(owner);\n")
        }
        builder.append("        return this;\n")
        builder.append("    }\n\n\n")
        return builder.toString()
    }

}
