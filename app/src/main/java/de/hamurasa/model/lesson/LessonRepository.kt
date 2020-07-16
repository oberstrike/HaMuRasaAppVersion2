package de.hamurasa.model.lesson

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query
import org.joda.time.DateTime

interface LessonRepository {
    suspend fun findById(id: Long): Lesson?

    suspend fun findAll(): List<Lesson>

    suspend fun save(lesson: Lesson)

    suspend fun delete(lesson: Lesson)

    suspend fun getNumberOfLessons(): Int

    suspend fun deleteAll()

    suspend fun findByServerId(serverId: Long): Lesson?
}

class LessonRepositoryImpl : LessonRepository {
    private var lessonBox: Box<Lesson> = ObjectBox.boxStore.boxFor()

    override suspend fun getNumberOfLessons(): Int {
        return lessonBox.query { build() }.find().size
    }

    override suspend fun delete(lesson: Lesson) {
        lessonBox.remove(lesson)
    }

    override suspend fun findAll(): List<Lesson> {
        return lessonBox.query().build().find()
    }


    override suspend fun findById(id: Long): Lesson? {
        return lessonBox.query().equal(Lesson_.id, id).build().findFirst()

    }

    override suspend fun findByServerId(serverId: Long): Lesson? =
        lessonBox.query().equal(Lesson_.id, serverId)
            .build()
            .findFirst()


    override suspend fun save(lesson: Lesson) {
        lesson.lastChanged = DateTime.now()
        lessonBox.put(lesson)
    }

    override suspend fun deleteAll() {
        lessonBox.removeAll()
    }


}
