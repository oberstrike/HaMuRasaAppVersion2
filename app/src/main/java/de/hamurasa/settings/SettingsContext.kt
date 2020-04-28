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
        SessionSettings.maxRepetitions = settings.maxRepetitions
        SessionSettings.maxVocableCount = settings.maxVocableCount
        isOffline = Observable.just(forceOffline)

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

    object SessionSettings {

        var maxRepetitions: Int = 5
            set(value) {
                field = value
                settings.maxRepetitions = value
            }

        var maxVocableCount: Int = 20
            set(value) {
                field = value
                settings.maxVocableCount = value
            }


    }

}