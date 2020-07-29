package de.hamurasa.data.util


import android.content.Context
import android.util.Log
import de.hamurasa.data.BuildConfig
import de.hamurasa.data.MyObjectBox
import de.hamurasa.data.vocable.Vocable
import io.objectbox.Box

import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser


class ObjectBox(context: Context) {

    private var boxStore: BoxStore = MyObjectBox
        .builder()
        .androidContext(context.applicationContext).build()

    init {
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

    fun <T> getBox(clazz: Class<T>) = boxStore.boxFor(clazz)

}
