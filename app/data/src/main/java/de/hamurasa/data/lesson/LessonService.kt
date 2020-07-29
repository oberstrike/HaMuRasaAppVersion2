package de.hamurasa.data.lesson

import de.hamurasa.data.vocable.Vocable
import io.objectbox.kotlin.applyChangesToDb
import org.joda.time.DateTime

interface LessonService {
    suspend fun findById(id: Long): Lesson?

    suspend fun findAll(): List<Lesson>

    suspend fun save(lesson: Lesson)

    suspend fun delete(lesson: Lesson)

    suspend fun size(): Int

    suspend fun deleteAll()

    suspend fun findByVocableId(vocableId: Long): List<Lesson>

    suspend fun removeVocable(lesson: Lesson, vocable: Vocable): Boolean

    suspend fun addVocable(lesson: Lesson, vocable: Vocable): Boolean

    suspend fun deleteVocableFromLesson(vocableId: Vocable, lesson: Lesson)

}

class LessonServiceImpl(private val lessonRepository: LessonRepository) :
    LessonService {

    override suspend fun findById(id: Long): Lesson? = lessonRepository.findById(id)

    override suspend fun findAll(): List<Lesson> = lessonRepository.findAll()

    override suspend fun save(lesson: Lesson) {
        lessonRepository.save(lesson)
    }

    override suspend fun delete(lesson: Lesson) = lessonRepository.delete(lesson)

    override suspend fun size(): Int = lessonRepository.getNumberOfLessons()

    override suspend fun deleteAll() = lessonRepository.deleteAll()

    override suspend fun findByVocableId(vocableId: Long): List<Lesson> {
        return lessonRepository.findByVocableId(vocableId)
    }

    override suspend fun removeVocable(
        lesson: Lesson,
        vocable: Vocable
    ): Boolean {
        val oldSize = lesson.words.size
        val new = lesson.words.filterNot { it.id == vocable.id }
        lesson.words.clear()
        lesson.words.addAll(new)
        lesson.words.applyChangesToDb()
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return oldSize != new.size
    }

    override suspend fun addVocable(
        lesson: Lesson,
        vocable: Vocable
    ): Boolean {
        val success = lesson.words.add(vocable)
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return success
    }

    override suspend fun deleteVocableFromLesson(vocableId: Vocable, lesson: Lesson) {
        lesson.words.removeIf { it.id == vocableId.id }
        lesson.lastChanged = DateTime.now()

        lesson.words.applyChangesToDb(resetFirst = true) {
            removeById(vocableId.id)
        }

    }


}