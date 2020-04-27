package de.hamurasa.settings.model

import android.content.Context
import android.content.SharedPreferences
import de.util.hamurasa.utility.boolean
import de.util.hamurasa.utility.int

class Settings(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("", 0)

    var appOffline: Boolean by sharedPreferences.boolean(APP_OFFLINE, false)

    var activeLessonId: Int by sharedPreferences.int(ACTIVE_LESSON_ID, 0)


    private companion object Key {
        const val APP_OFFLINE: String = "VERSION_1"
        const val ACTIVE_LESSON_ID = "ACTIVE_LESSON"
    }

}