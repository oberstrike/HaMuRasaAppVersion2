package de.hamurasa.lesson.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
class Lesson(
    @Id var id: Long = 0,
    var serverId: Long = 0
) {
    lateinit var words: ToMany<Vocable>
}