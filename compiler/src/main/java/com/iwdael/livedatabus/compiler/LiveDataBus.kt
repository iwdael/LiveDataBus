package com.iwdael.livedatabus.compiler


import com.iwdael.livedatabus.annotation.Observe
import com.iwdael.livedatabus.annotation.ObserveSticky
class LiveDataBus(val  element: ClassElement) {
    val packageName = element.getPackage()
    val targetClassName = element.getClassName()
    val generatedClassName = targetClassName + "LiveDataBus"
    val generatedFullClassName = "${packageName}.${generatedClassName}"
    val stickyEventMethods = element.getMethodElement(ObserveSticky::class)
    val eventMethods = element.getMethodElement(Observe::class)

}
