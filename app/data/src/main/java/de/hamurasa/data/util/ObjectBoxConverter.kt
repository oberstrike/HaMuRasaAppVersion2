package de.hamurasa.data.util

import com.google.gson.reflect.TypeToken
import de.hamurasa.data.vocable.Language
import de.hamurasa.data.vocable.VocableType
import io.objectbox.converter.PropertyConverter
import org.joda.time.DateTime


class ListOfStringConverter : PropertyConverter<List<String>, String> {
    override fun convertToDatabaseValue(entityProperty: List<String>?): String {
        return GsonObject.gson.toJson(entityProperty)

    }

    override fun convertToEntityProperty(databaseValue: String?): List<String> {
        val listOfStringTypeToken = object : TypeToken<List<String>>() {}.type
        return GsonObject.gson.fromJson(databaseValue, listOfStringTypeToken)
    }
}

class LanguageStringConverter : PropertyConverter<Language, Long> {
    override fun convertToDatabaseValue(entityProperty: Language?): Long {
        return entityProperty?.id ?: 1
    }

    override fun convertToEntityProperty(databaseValue: Long?): Language {
        return Language.fromId(
            databaseValue!!
        )!!
    }
}

class DateTimeStringConverter : PropertyConverter<DateTime, String> {
    override fun convertToDatabaseValue(entityProperty: DateTime?): String {
        return entityProperty.toString()
    }

    override fun convertToEntityProperty(databaseValue: String?): DateTime {
        if (databaseValue == null) return DateTime.now()
        return DateTime.parse(databaseValue)
    }

}


class VocableTypeConverter : PropertyConverter<VocableType, Long> {
    override fun convertToDatabaseValue(entityProperty: VocableType?): Long {
        return entityProperty?.id ?: 1
    }

    override fun convertToEntityProperty(databaseValue: Long?): VocableType {
        return VocableType.fromId(
            databaseValue!!
        )!!
    }

}