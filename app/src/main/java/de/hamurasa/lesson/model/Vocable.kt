package de.hamurasa.lesson.model

import com.google.gson.reflect.TypeToken
import de.hamurasa.util.GsonObject
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class Vocable(@Id var id: Long = 0,
                   var value: String,
                   var type: String,
                   @Convert(converter = ListOfStringConverter::class, dbType = String::class)
                   var translation: List<String>)

class ListOfStringConverter : PropertyConverter<List<String>, String> {
    override fun convertToDatabaseValue(entityProperty: List<String>?): String {
        return GsonObject.gson.toJson(entityProperty)

    }

    override fun convertToEntityProperty(databaseValue: String?): List<String> {
        val listOfStringTypeToken = object : TypeToken<List<String>>() {}.type
        return GsonObject.gson.fromJson(databaseValue, listOfStringTypeToken)
    }
}