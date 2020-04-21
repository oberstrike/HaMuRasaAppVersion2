package de.hamurasa.settings

import de.hamurasa.settings.model.Settings
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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