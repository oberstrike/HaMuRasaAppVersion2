package de.hamurasa.data.util


import android.content.Context
import android.util.Log
import de.hamurasa.data.BuildConfig
import de.hamurasa.data.MyObjectBox

import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set


    fun init(context: Context) {
        boxStore = MyObjectBox
            .builder()
            .androidContext(context.applicationContext).build()
        boxStore.close()
        boxStore = MyObjectBox
            .builder()
            .androidContext(context.applicationContext).build()

        if (BuildConfig.DEBUG) {
            Log.d(
                "Debug",
                "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})"
            )
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }
}
