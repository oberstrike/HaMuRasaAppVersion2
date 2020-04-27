package de.hamurasa.util

import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableType
import org.joda.time.DateTime
import java.lang.reflect.Type

object GsonObject {
    var gson: Gson
        private set

    init {
        gson = Converters.registerDateTime(
            GsonBuilder()
        )
            .registerTypeAdapter(Lesson::class.java, LessonDeserializer())
            .registerTypeAdapter(Vocable::class.java, VocableDeserializer())
            .create()

    }
}

class VocableDeserializer : JsonDeserializer<Vocable> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Vocable? {
        if (json != null) {
            if (context != null) {
                val jsonObject = json.asJsonObject

                val listStringType =
                    object : TypeToken<List<String?>?>() {}.type
                val listOfVocableJsonArray = jsonObject.getAsJsonArray("translations")
                val translation: List<String> =
                    context.deserialize(listOfVocableJsonArray, listStringType)

                val serverId = jsonObject.getAsJsonPrimitive("id").asLong
                val type = VocableType.valueOf(jsonObject.getAsJsonPrimitive("type").asString)
                val value = jsonObject.getAsJsonPrimitive("value").asString

                return Vocable(
                    0,
                    serverId,
                    false,
                    value,
                    type,
                    translation,
                    Language.ES
                )
            }

        }
        return null
    }

}

@Suppress
class LessonDeserializer : JsonDeserializer<Lesson> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Lesson? {
        if (json != null) {
            if (context != null) {
                val jsonObject = json.asJsonObject
                val lesson = Lesson(
                    0,
                    0,
                    false,
                    Language.ES,
                    Language.GER,
                    DateTime.now()
                )

                val wordsJson = jsonObject.getAsJsonArray("vocables")
                val wordsJsonType =
                    object : TypeToken<ArrayList<Vocable?>?>() {}.type
                val list: ArrayList<Vocable> = context.deserialize(wordsJson, wordsJsonType)

                val wordIdJson = jsonObject.getAsJsonPrimitive("id")
                val id = wordIdJson.asLong

                val lastChangedJson = jsonObject.getAsJsonPrimitive("lastChanged").asString
                val lastChanged = DateTime.parse(lastChangedJson)

                lesson.lastChanged = lastChanged
                lesson.serverId = id
                lesson.words.addAll(list.map { it.id = 0; it })
                return lesson
            }
        }
        return null
    }
}
