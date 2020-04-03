package de.hamurasa.lesson.model

import com.google.gson.reflect.TypeToken
import de.hamurasa.util.GsonObject
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class Vocable(
    @Id var id: Long = 0,
    var serverId: Long = 0,
    var value: String,
    var type: String,
    @Convert(converter = ListOfStringConverter::class, dbType = String::class)
    var translation: List<String>,
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var language: Language
) {

    constructor() : this(0, 0, "", "", arrayListOf(), Language.ES)

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

class LanguageStringConverter : PropertyConverter<Language, Long> {
    override fun convertToDatabaseValue(entityProperty: Language?): Long {
        return entityProperty?.id ?: 1
    }

    override fun convertToEntityProperty(databaseValue: Long?): Language {
        return Language.fromId(databaseValue!!)!!
    }

}

enum class Language(val id: Long, val letterCode: String) {
    ES(0, "ES"), GER(1, "GER");

    companion object {
        fun fromId(id: Long) = values().firstOrNull { it.id == id }

        fun fromLetterCode(letterCode: String) =
            values().firstOrNull { it.letterCode.toLowerCase() == letterCode.toLowerCase() }
    }

}

data class VocableDTO private constructor(
    val id: Long,
    val value: String,
    val type: String,
    val translations: List<String>,
    val language: Language = Language.ES
) {
    private constructor(vocableDTO: VocableDTO) : this(
        vocableDTO.id,
        vocableDTO.value,
        vocableDTO.type,
        vocableDTO.translations,
        vocableDTO.language
    )

    companion object {
        fun create(
            id: Long,
            value: String,
            type: String,
            translations: List<String>,
            language: Language = Language.ES
        ) = VocableDTO(id, value, type, translations, language)

        fun copy(vocableDTO: VocableDTO) = VocableDTO(vocableDTO)
    }
}