package de.hamurasa.data.util

import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.data.lesson.LessonDeserializer
import de.hamurasa.data.vocable.VocableDeserializer


object GsonObject {
    var gson: Gson
        private set

    init {
        gson = Converters.registerDateTime(
            GsonBuilder()
        )
            .registerTypeAdapter(
                Lesson::class.java,
                LessonDeserializer()
            )
            .registerTypeAdapter(de.hamurasa.data.vocable.Vocable::class.java,
                VocableDeserializer()
            )
            .create()

    }
}

inline fun <reified T> getTypeToken() = object : TypeToken<T>() {}.type

