package com.iwdael.livedatabus.compiler


import com.iwdael.livedatabus.annotation.Observe
import com.iwdael.livedatabus.annotation.ObserveForever
import com.iwdael.livedatabus.annotation.ObserveForeverSticky
import com.iwdael.livedatabus.annotation.ObserveSticky

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class LiveDataBus(val  element: ClassElement) {
    val packageName = element.getPackage()
    val targetClassName = element.getClassName()
    val generatedClassName = targetClassName + "LiveDataBus"
    val generatedFullClassName = "${packageName}.${generatedClassName}"
    val observeSticky = element.getMethodElement(ObserveSticky::class)
    val observe = element.getMethodElement(Observe::class)
    val observeForever = element.getMethodElement(ObserveForever::class)
    val observeForeverSticky = element.getMethodElement(ObserveForeverSticky::class)

}
