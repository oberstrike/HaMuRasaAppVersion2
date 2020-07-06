package de.hamurasa.settings

import de.hamurasa.settings.model.Settings
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object SettingsContext {

    private lateinit var settings: Settings

    fun init(settings: Settings) {
        this.settings = settings
        activeLessonId = settings.activeLessonId
        SessionSettings.maxRepetitions = settings.maxRepetitions
        SessionSettings.maxVocableCount = settings.maxVocableCount
        SessionSettings.standardInputType = settings.standardInputType
        SessionSettings.alternativeInputType = settings.alternativeInputType
        SessionSettings.writingInputType = settings.writingInputType

    }


    var activeLessonId: Int = 0
        set(value) {
            field = value
            settings.activeLessonId = value

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

        var writingInputType: Boolean = true
            set(value) {
                field = value
                settings.writingInputType = value
            }

        var standardInputType: Boolean = true
            set(value) {
                field = value
                settings.standardInputType = value
            }

        var alternativeInputType: Boolean = true
            set(value) {
                field = value
                settings.alternativeInputType = value
            }

    }

}