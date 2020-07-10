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
    var name: String = "Profile $staticId",
    var offline: Boolean = false
) {


    lateinit var lessons: ToMany<Lesson>

    companion object {
        var staticId: Long = 0
            get() {
                field += 1
                return field
            }
    }
}

data class ProfileDTO(
    var id: Long,
    var name: String = "Profile",
    var offline: Boolean = false,
    var lessons: List<LessonDTO>
)

