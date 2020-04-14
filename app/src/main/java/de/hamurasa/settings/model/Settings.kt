package de.hamurasa.settings.model

import android.content.Context
import android.content.SharedPreferences
import de.hamurasa.util.boolean

class Settings(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("", 0)

    var appOffline: Boolean by sharedPreferences.boolean(APP_OFFLINE)

    private companion object Key {
        const val APP_OFFLINE: String = "VERSION_1"
    }

}