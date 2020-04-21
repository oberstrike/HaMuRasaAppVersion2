package de.hamurasa.lesson.model.lesson

import de.hamurasa.lesson.model.vocable.*
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import org.joda.time.DateTime

@Entity
class Lesson(
    @Id var id: Long = 0,
    var serverId: Long = 0,
    val isOffline: Boolean = false,
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var language: Language,
    @Convert(converter = LanguageStringConverter::class, dbType = Long::class)
    var validationLanguage: Language,
    @Convert(converter = DateTimeStringConverter::class, dbType = String::class)
    var lastChanged: DateTime
) {
    lateinit var words: ToMany<Vocable>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Lesson
        if (serverId != other.serverId) return false
        return true
    }

    override fun hashCode(): Int {
        return serverId.hashCode()
    }
}

data class LessonDTO(
    val id: Long = 0,
    var vocables: List<VocableDTO>,
    var language: Language,
    var validationLanguage: Language
)