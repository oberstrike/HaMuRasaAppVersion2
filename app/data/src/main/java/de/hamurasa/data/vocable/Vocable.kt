package de.hamurasa.data.vocable

import com.google.gson.reflect.TypeToken
import de.hamurasa.data.util.*
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import org.joda.time.DateTime
import java.util.*

@Entity
class Vocable(
    @Id var id: Long = 0,
    var value: String = "",
    @Convert(converter = VocableTypeConverter::class, dbType = Long::class)
    var type: VocableType = VocableType.VERB,
    @Convert(converter = ListOfStringConverter::class, dbType = String::class)
    var translation: List<String> = arrayListOf(),
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var language: Language = Language.ES,
    @Convert(converter = DateTimeStringConverter::class, dbType = String::class)
    var lastChanged: DateTime = DateTime.now()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vocable
        if (id != other.id) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class VocableType(val id: Long) {
    VERB(0), SUBSTANTIVE(1), ADJECTIVE(2), PREPOSITIONS(3), CONJUNCTIONS(4), ADVERBS(5);

    companion object {
        fun fromId(id: Long) = values().firstOrNull { it.id == id }
    }
}



enum class Language(val id: Long, val letterCode: String) {
    ES(0, "ES"), GER(1, "GER");

    companion object {
        fun fromId(id: Long) = values().firstOrNull { it.id == id }

        fun fromLetterCode(letterCode: String) =
            values().firstOrNull {
                it.letterCode.toLowerCase(Locale.ROOT) == letterCode.toLowerCase(
                    Locale.ROOT
                )
            }
    }

}