package de.hamurasa.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import de.hamurasa.lesson.model.Lesson
import de.hamurasa.lesson.model.Vocable
import java.lang.reflect.Type

class VocableDeserializer : JsonDeserializer<Vocable> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Vocable {
        if(json != null){
            if(context != null){
                val jsonObject = json.asJsonObject

                val listStringType =
                    object : TypeToken<List<String?>?>() {}.type
                val listOfVocableJsonArray = jsonObject.getAsJsonArray("translation")
                val translation: List<String> = context.deserialize(listOfVocableJsonArray, listStringType)

                val id = 0

                val serverId = jsonObject.getAsJsonPrimitive("id").asLong
                val type = jsonObject.getAsJsonPrimitive("type").asString
                val value = jsonObject.getAsJsonPrimitive("value").asString

                return Vocable(0, serverId, value, type, translation)
            }

        }
        return Vocable()
    }

}

@Suppress
class LessonDeserializer : JsonDeserializer<Lesson> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Lesson {
        if (json != null) {
            if (context != null) {
                val jsonObject = json.asJsonObject
                val lesson = Lesson()

                val wordsJson = jsonObject.getAsJsonArray("words")
                val wordsJsonType =
                    object : TypeToken<ArrayList<Vocable?>?>() {}.type
                val list: ArrayList<Vocable> = context.deserialize(wordsJson, wordsJsonType)

                val wordIdJson = jsonObject.getAsJsonPrimitive("id")
                val id = wordIdJson.asLong

                lesson.serverId = id
                lesson.words.addAll(list.map { it.id = 0; it })
                return lesson
            }
        }
        return Lesson()
    }
}
