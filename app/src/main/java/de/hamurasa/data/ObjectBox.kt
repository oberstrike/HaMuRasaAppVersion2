package de.hamurasa.data

import android.content.Context
import android.util.Log
import de.hamurasa.BuildConfig
import de.hamurasa.lesson.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context){
        boxStore = MyObjectBox
            .builder()
            .androidContext(context.applicationContext).build()
        boxStore.close()
        boxStore.deleteAllFiles()
        boxStore = MyObjectBox
            .builder()
            .androidContext(context.applicationContext).build()

        if (BuildConfig.DEBUG) {
            Log.d("Debug", "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }
}