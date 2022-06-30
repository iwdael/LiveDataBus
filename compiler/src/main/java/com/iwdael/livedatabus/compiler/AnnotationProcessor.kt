package com.iwdael.livedatabus.compiler

import com.google.auto.service.AutoService
import com.iwdael.annotationprocessorparser.Class
import com.iwdael.livedatabus.annotation.UseLiveDataBus
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes() =
        mutableSetOf(UseLiveDataBus::class.java.canonicalName)

    private var initialized = false
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (initialized) return false
        roundEnv?.getElementsAnnotatedWith(UseLiveDataBus::class.java)
            ?.map { Class(it) }
            ?.apply { ContentProviderMaker(this).make(processingEnv.filer) }
            ?.map { LiveDataBusMaker(it) }
            ?.forEach { it.make(processingEnv.filer) }
        initialized = true
        return false
    }
}