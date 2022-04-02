package com.iwdael.livedatabus.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import kotlin.reflect.KClass


/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class ClassElement(val element: Element) {
    fun getPackage() = element.enclosingElement.toString()
    fun getClassName() = element.simpleName.toString()
    fun getMethodElement(clazz: KClass<*>) = element.enclosedElements.filter {
        it.annotationMirrors.map { it.annotationType }.map { it.toString() }
            .contains(clazz.qualifiedName)
    }.map { MethodElement(it as ExecutableElement) }


    fun sourceFileIsKotlin() = element.getAnnotation(Metadata::class.java) != null

    override fun toString() = "${getPackage()}.${getClassName()}"
}