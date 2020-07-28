package de.hamurasa.data.profile

import de.hamurasa.data.lesson.Lesson
import de.hamurasa.data.util.DateTimeStringConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import org.joda.time.DateTime

@Entity
data class Profile(
    @Id
    var id: Long = 0,
    var name: String = "Profile",
    var offline: Boolean = false,
    @Convert(converter = DateTimeStringConverter::class, dbType = String::class)
    var lastTimeChanged: DateTime = DateTime.now(),
    var selfControlled: Boolean = false
) {
    lateinit var lessons: ToMany<Lesson>
}

