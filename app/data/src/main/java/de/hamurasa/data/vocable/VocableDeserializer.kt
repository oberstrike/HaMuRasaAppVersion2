package de.hamurasa.data.vocable

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import de.hamurasa.data.util.getTypeToken
import org.joda.time.DateTime
import java.lang.reflect.Type


class VocableDeserializer : JsonDeserializer<Vocable> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Vocable? {
        if (json == null)
            return null
        if (context == null)
            return null
        val vocable = Vocable()
        val jsonObject = json.asJsonObject


        //Id
        val id = jsonObject.getAsJsonPrimitive("id").asLong
        vocable.id = id

        //Translations
        val listStringType = getTypeToken<List<String?>?>()
        val listOfVocableJsonArray = jsonObject.getAsJsonArray("translations")
        val translation: List<String> =
            context.deserialize(listOfVocableJsonArray, listStringType)
        vocable.translation = translation

        //Type
        val type = VocableType.valueOf(jsonObject.getAsJsonPrimitive("type").asString)
        vocable.type = type

        //Value
        val value = jsonObject.getAsJsonPrimitive("value").asString
        vocable.value = value

        //Last Changed
        val lastChangedString = jsonObject.getAsJsonPrimitive("lastChanged").asString
        val lastChanged = DateTime.parse(lastChangedString)
        vocable.lastChanged = lastChanged

        //Language
        val languageJson = jsonObject.getAsJsonObject("language")
        val language = context.deserialize<Language>(languageJson, Language::class.java)
        vocable.language = language

        return vocable
    }

}

