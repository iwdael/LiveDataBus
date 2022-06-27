package com.iwdael.livedatabus.compiler2

import javax.annotation.processing.Filer

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
interface Maker {
    companion object {
        const val TAB = "    "
    }

    val `package`: String
    val packageGenerator: String
    val className: String
    val classNameGenerator: String

    fun make(filer: Filer)
}