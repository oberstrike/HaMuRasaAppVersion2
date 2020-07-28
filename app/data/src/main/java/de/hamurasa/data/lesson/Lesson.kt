package de.hamurasa.data.lesson

import de.hamurasa.data.util.DateTimeStringConverter
import de.hamurasa.data.util.LanguageStringConverter
import de.hamurasa.data.vocable.Language
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import org.joda.time.DateTime

@Entity
data class Lesson(
    @Id var id: Long = 0,
    @Convert(
        converter = LanguageStringConverter::class,
        dbType = Long::class
    )
    var language: Language = Language.ES,
    @Convert(
        converter = LanguageStringConverter::class,
        dbType = Long::class
    )
    var validationLanguage: Language = Language.GER,
    @Convert(
        converter = DateTimeStringConverter::class,
        dbType = String::class
    )
    var lastChanged: DateTime = DateTime.now()
) {
    lateinit var words: ToMany<de.hamurasa.data.vocable.Vocable>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Lesson

        if (id != other.id) return false
        if (language != other.language) return false
        if (validationLanguage != other.validationLanguage) return false
        if (lastChanged != other.lastChanged) return false
        if (words != other.words) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + validationLanguage.hashCode()
        result = 31 * result + lastChanged.hashCode()
        result = 31 * result + words.hashCode()
        return result
    }

}

fun getLesson() = Lesson(
    0,
    Language.ES,
    Language.GER,
    DateTime.now()
)