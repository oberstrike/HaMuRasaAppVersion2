package de.hamurasa.settings

import de.hamurasa.settings.model.Settings
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.internal.format
import java.lang.Exception

object SettingsContext {

    private lateinit var settings: Settings

    fun init(settings: Settings) {
        this.settings = settings
        forceOffline = settings.appOffline
        isOffline = BehaviorSubject.just(forceOffline)
    }

    lateinit var isOffline: Observable<Boolean>

    var forceOffline: Boolean = false
        set(value) {
            field = value
            settings.appOffline = value
        }
}

fun <R> request(default: R, block: () -> R): R {
    try {
        return block.invoke()
    } catch (exception: Exception) {
        println("There was no connection found.")
    }
    return default
}