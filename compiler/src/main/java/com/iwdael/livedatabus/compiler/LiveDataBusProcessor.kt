package com.iwdael.livedatabus.compiler

import com.google.auto.service.AutoService
import com.iwdael.livedatabus.annotation.UseLiveDataBus
import java.io.File
import java.io.FileWriter
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
@AutoService(Processor::class)
class LiveDataBusProcessor : AbstractProcessor() {
    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(UseLiveDataBus::class.java.canonicalName)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        environment: RoundEnvironment
    ): Boolean {
        environment.getElementsAnnotatedWith(UseLiveDataBus::class.java)
            .sortedBy { it.simpleName.toString() }
            .map { LiveDataBus(ClassElement(it)) }
            .apply {
                if (isNotEmpty()) {
                    val writer = FileWriter(createContentProvider(this[0]))
                    writer.write(ContentProviderGenerator(this).generate())
                    writer.flush()
                    writer.close()
                }
            }
            .forEach {
                val writer = FileWriter(findGenerateFile(it))
                if (it.element.sourceFileIsKotlin())
                    writer.write(KotlinGenerator(it).generate())
                else
                    writer.write(JavaGenerator(it).generate())
                writer.flush()
                writer.close()
            }

        return true
    }

    private fun createContentProvider(element: LiveDataBus): File {
        val generateFile = File(
            processingEnv.filer.createSourceFile(
                "com.iwdael.livedatabuds.LiveDataBusContentProvider",
                element.element.element
            ).toUri()
        ).apply {
            parentFile.mkdirs()
        }.let { it.parentFile }
        return File(generateFile, "LiveDataBusContentProvider.kt")
    }

    private fun findGenerateFile(element: LiveDataBus): File {
        val generateFile = File(
            processingEnv.filer.createSourceFile(
                element.generatedFullClassName,
                element.element.element
            ).toUri()
        ).apply { parentFile.mkdirs() }
        return if (element.element.sourceFileIsKotlin()) {
            File(
                generateFile.parentFile,
                "${element.generatedClassName}.kt"
            ).apply { parentFile.mkdirs() }
        } else generateFile
    }


}

fun String.firstUpper(): String {
    return this.substring(0, 1).toUpperCase() + this.substring(1)
}


fun String.javaFullClass2KotlinShotClass(): String {
    return when {
        this == "java.lang.String" -> "String"
        this == "java.lang.String[]" -> "Array<String>"
        this == "int" -> "Int"
        this == "int[]" -> "IntArray"
        this == "boolean" -> "Boolean"
        this == "boolean[]" -> "BooleanArray"
        this == "float" -> "Float"
        this == "float[]" -> "FloatArray"
        this == "long" -> "Long"
        this == "long[]" -> "LongArray"
        this == "double" -> "Double"
        this == "double[]" -> "DoubleArray"
        this == "short" -> "Short"
        this == "short[]" -> "ShortArray"
        this == "byte" -> "Byte"
        this == "byte[]" -> "ByteArray"
        this == "char" -> "Char"
        this == "char[]" -> "CharArray"
        this.endsWith("[]") -> "Array<${this.replace("[]", "")}>".javaFullClass2KotlinShotClass()
        this.contains("java.util.") -> this.replace("java.util.", "")
            .javaFullClass2KotlinShotClass()
        this.contains("java.lang.") -> this.replace("java.lang.", "")
            .javaFullClass2KotlinShotClass()
        this.contains("Integer") -> this.replace("Integer", "Int").javaFullClass2KotlinShotClass()
        else -> this
    }
}


fun MethodElement.makeVariableName(): String {
    return "observe${getName().firstUpper()}_${
        this.getParameters().joinToString(
            separator = "_",
            transform = {
                it.asType().toString().javaFullClass2KotlinShotClass().replace("<", "_")
                    .replace(">", "")
            })
    }"
}


fun MethodElement.makeClassName(): String {
    return makeVariableName().firstUpper()
}

fun MethodElement.observeType(isKot: Boolean): String {
    return if (isKot) {
        getParameters().getOrNull(0)?.asType()?.toString()
            ?.javaFullClass2KotlinShotClass() ?: ""
    } else {
        getParameters().getOrNull(0)?.asType()?.toString() ?: ""
    }
}