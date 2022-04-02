package com.iwdael.livedatabus.compiler
import javax.lang.model.element.*
/**
 * author : 段泽全(iwdael)
 * e-mail : iwdael@outlook.com
 * time   : 2019/8/5
 * desc   :
 * version: 1.0
 */
class MethodElement(private val element: ExecutableElement) {

    fun getSimpleClassName() = element.enclosingElement.simpleName
    fun getFullClassName() = element.enclosingElement.toString()
    fun getName() = element.simpleName.toString()
    override fun toString() = "$element"
    fun <T : Annotation> getAnnotation(clazz: Class<T>): T? {
        return element.getAnnotation(clazz)
    }

    fun getParameters(): MutableList<out VariableElement> {
        return element.parameters ?: arrayListOf()
    }

    fun isPublic(): Boolean {
        return element.modifiers?.contains(Modifier.PUBLIC) == true
    }
}