package de.hamurasa.lesson.model

import com.google.gson.reflect.TypeToken
import de.hamurasa.util.GsonObject
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class Vocable(@Id var id: Long = 0,
                   var serverId: Long = 0,
                   var value: String,
                   var type: String,
                   @Convert(converter = ListOfStringConverter::class, dbType = String::class)
                   var translation: List<String>){

    constructor() : this(0, 0, "", "", arrayListOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vocable

        if (serverId != other.serverId) return false

        return true
    }

    override fun hashCode(): Int {
        return serverId.hashCode()
    }


}

class ListOfStringConverter : PropertyConverter<List<String>, String> {
    override fun convertToDatabaseValue(entityProperty: List<String>?): String {
        return GsonObject.gson.toJson(entityProperty)

    }

    override fun convertToEntityProperty(databaseValue: String?): List<String> {
        val listOfStringTypeToken = object : TypeToken<List<String>>() {}.type
        return GsonObject.gson.fromJson(databaseValue, listOfStringTypeToken)
    }
}