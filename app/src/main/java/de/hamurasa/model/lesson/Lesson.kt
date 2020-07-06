package de.hamurasa.model.lesson

import de.hamurasa.model.vocable.*
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import org.joda.time.DateTime

@Entity
class Lesson(
    @Id var id: Long = 0,
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var language: Language = Language.ES,
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var validationLanguage: Language = Language.GER,
    @Convert(converter = DateTimeStringConverter::class, dbType = String::class)
    var lastChanged: DateTime = DateTime.now()
) {
    lateinit var words: ToMany<Vocable>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Lesson
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

data class LessonDTO(
    val id: Long = 0,
    var vocables: List<VocableDTO>,
    var language: Language,
    var validationLanguage: Language,
    var lastChanged: DateTime
)