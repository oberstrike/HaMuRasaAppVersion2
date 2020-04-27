package de.hamurasa.settings

import de.hamurasa.settings.model.Settings
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object SettingsContext {

    private lateinit var settings: Settings

    fun init(settings: Settings) {
        this.settings = settings
        forceOffline = settings.appOffline
        activeLessonId = settings.activeLessonId
        isOffline = BehaviorSubject.just(forceOffline)

    }

    lateinit var isOffline: Observable<Boolean>

    var activeLessonId: Int = 0
        set(value) {
            field = value
            settings.activeLessonId = value

        }

    var forceOffline: Boolean = false
        set(value) {
            field = value
            settings.appOffline = value
        }


}