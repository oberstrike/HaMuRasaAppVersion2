package de.hamurasa.model.profile

import de.hamurasa.model.lesson.Lesson
import de.hamurasa.model.lesson.LessonDTO
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Profile(
    @Id
    var id: Long = 0,
    var name: String = "Profile",
    var offline: Boolean = false
) {


    lateinit var lessons: ToMany<Lesson>


}

data class ProfileDTO(
    var id: Long,
    var name: String = "Profile",
    var offline: Boolean = false,
    var lessons: List<LessonDTO>
)

