package de.hamurasa.settings.model

import android.content.Context
import android.content.SharedPreferences
import de.util.hamurasa.utility.boolean
import de.util.hamurasa.utility.int

class Settings(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("", 0)

    var appOffline: Boolean by sharedPreferences.boolean(APP_OFFLINE, false)

    var activeLessonId: Int by sharedPreferences.int(ACTIVE_LESSON_ID, 0)

    var maxRepetitions: Int by sharedPreferences.int(MAX_REPETITIONS, 5)

    var maxVocableCount: Int by sharedPreferences.int(MAX_VOCABLE_COUNT, 20)

    private companion object Key {
        const val APP_OFFLINE: String = "VERSION_1"
        const val ACTIVE_LESSON_ID = "ACTIVE_LESSON"
        const val MAX_REPETITIONS = "MAX_REPETITIONS"
        const val MAX_VOCABLE_COUNT = "MAX_VOCABLE_COUNT"
    }

}