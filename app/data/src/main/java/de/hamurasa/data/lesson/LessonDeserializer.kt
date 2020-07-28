package de.hamurasa.data.lesson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import de.hamurasa.data.util.getTypeToken
import de.hamurasa.data.vocable.Language
import de.hamurasa.data.vocable.Vocable
import org.joda.time.DateTime
import java.lang.reflect.Type

/**
 * @author Markus Jürgens
 * Klasse zum manuellen derilisieren der Klasse Lesson
 *
 */
class LessonDeserializer : JsonDeserializer<Lesson> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Lesson? {
        if (json == null)
            return null
        if (context == null)
            return null
        val jsonObject = json.asJsonObject
        val lesson = getLesson()

        //Language
        val languageJson = jsonObject.getAsJsonObject("language")
        val language = context.deserialize<Language>(languageJson, Language::class.java)
        lesson.language = language

        //Words
        val wordsJson = jsonObject.getAsJsonArray("vocables")
        val wordsJsonType = getTypeToken<ArrayList<Vocable?>?>()
        val list: ArrayList<Vocable> = context.deserialize(wordsJson, wordsJsonType)
        lesson.words.addAll(list.map { it.id = 0; it })

        //DDates
        val lastChangedJson = jsonObject.getAsJsonPrimitive("lastChanged").asString
        val lastChanged = DateTime.parse(lastChangedJson)

        lesson.lastChanged = lastChanged

        return lesson
    }
}