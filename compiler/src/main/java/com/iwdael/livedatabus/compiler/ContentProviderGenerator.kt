package com.iwdael.livedatabus.compiler

import java.lang.StringBuilder

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class ContentProviderGenerator(val buses: List<LiveDataBus>) {
    fun generate(): String = "package com.iwdael.livedatabus\n" +
            "\n" +
            "import android.app.Activity\n" +
            "import android.app.Application\n" +
            "import android.content.ContentProvider\n" +
            "import android.content.ContentValues\n" +
            "import android.database.Cursor\n" +
            "import android.net.Uri\n" +
            "import android.os.Bundle\n" +
            "\n" +
            "/**\n" +
            " * author : Iwdael\n" +
            " * e-mail : iwdael@outlook.com\n" +
            " * desc   : This document cannot be modified\n" +
            " */\n" +
            "class LiveDataBusContentProvider : ContentProvider(), Application.ActivityLifecycleCallbacks {\n" +
            "    override fun onCreate(): Boolean {\n" +
            "        (context?.applicationContext as Application?)?.registerActivityLifecycleCallbacks(this)\n" +
            "        return true\n" +
            "    }\n" +
            "\n" +
            "    override fun query(\n" +
            "        uri: Uri,\n" +
            "        projection: Array<out String>?,\n" +
            "        selection: String?,\n" +
            "        selectionArgs: Array<out String>?,\n" +
            "        sortOrder: String?\n" +
            "    ): Cursor? {\n" +
            "        return null\n" +
            "    }\n" +
            "\n" +
            "    override fun getType(uri: Uri): Nothing? = null\n" +
            "    override fun insert(uri: Uri, values: ContentValues?): Nothing? = null\n" +
            "    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = -1\n" +
            "\n" +
            "    override fun update(\n" +
            "        uri: Uri,\n" +
            "        values: ContentValues?,\n" +
            "        selection: String?,\n" +
            "        selectionArgs: Array<out String>?\n" +
            "    ) = -1\n" +
            "\n" +
            "    override fun onActivityCreated(p0: Activity, p1: Bundle?) {\n" +
            "        observeLiveDataBus(p0)\n" +
            "    }\n" +
            "\n" +
            "    override fun onActivityStarted(p0: Activity) {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    override fun onActivityResumed(p0: Activity) {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    override fun onActivityPaused(p0: Activity) {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    override fun onActivityStopped(p0: Activity) {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    override fun onActivityDestroyed(p0: Activity) {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    private fun observeLiveDataBus(activity: Activity) {\n" +
            "        when (activity) {\n" +
            observeLiveDataBus() +
            "        }\n" +
            "    }\n" +
            "\n" +
            "}\n"

    private fun observeLiveDataBus(): String {
        val builder = StringBuilder()
        buses.forEach {
            builder.append(
                "            is ${it.packageName}.${it.targetClassName} -> " +
                        "${it.generatedFullClassName}().observe(activity)\n"
            )
        }
        return builder.toString()
    }

}