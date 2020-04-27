package de.hamurasa.lesson.model.vocable

import com.google.gson.reflect.TypeToken
import de.hamurasa.util.GsonObject
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import org.joda.time.DateTime

@Entity
data class Vocable(
    @Id var id: Long = 0,
    var serverId: Long = 0,
    var isOffline: Boolean = false,
    var value: String,
    @Convert(converter = VocableTypeConverter::class, dbType = Long::class)
    var type: VocableType,
    @Convert(converter = ListOfStringConverter::class, dbType = String::class)
    var translation: List<String>,
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var language: Language
) {

    constructor() : this(0, 0, false, "",
        VocableType.VERB, arrayListOf(),
        Language.ES
    )

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

enum class VocableType(val id: Long) {
    VERB(0), SUBSTANTIVE(1), ADJECTIVE(2);

    companion object {
        fun fromId(id: Long) = values().firstOrNull { it.id == id }
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
        return Language.fromId(
            databaseValue!!
        )!!
    }
}

class DateTimeStringConverter: PropertyConverter<DateTime, String> {
    override fun convertToDatabaseValue(entityProperty: DateTime?): String {
        return entityProperty.toString()
    }

    override fun convertToEntityProperty(databaseValue: String?): DateTime {
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

enum class Language(val id: Long, val letterCode: String) {
    ES(0, "ES"), GER(1, "GER");

    companion object {
        fun fromId(id: Long) = values().firstOrNull { it.id == id }

        fun fromLetterCode(letterCode: String) =
            values().firstOrNull { it.letterCode.toLowerCase() == letterCode.toLowerCase() }
    }

}

data class VocableDTO private constructor(
    var id: Long,
    val value: String,
    val type: VocableType,
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
            type: VocableType,
            translations: List<String>,
            language: Language = Language.ES
        ) = VocableDTO(
            id,
            value,
            type,
            translations,
            language
        )

        fun copy(vocableDTO: VocableDTO) =
            VocableDTO(vocableDTO)
    }
}
