package de.hamurasa.model.lesson

import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableDTO
import org.joda.time.DateTime

interface LessonService {
    suspend fun findById(id: Long): Lesson?

    suspend fun findAll(): List<Lesson>

    suspend fun save(lesson: Lesson)

    suspend fun delete(lesson: Lesson)

    suspend fun size(): Int

    suspend fun deleteAll()

    suspend fun findByServerId(serverId: Long): Lesson?

    suspend fun removeVocable(lesson: Lesson, vocable: Vocable): Boolean

    suspend fun addVocable(lesson: Lesson, vocable: Vocable): Boolean

    fun convertToDTO(lesson: Lesson, vocableDTOs: List<VocableDTO>): LessonDTO
}

class LessonServiceImpl(private val lessonRepository: LessonRepository) : LessonService {

    override suspend fun findById(id: Long): Lesson? = lessonRepository.findById(id)

    override suspend fun findAll(): List<Lesson> = lessonRepository.findAll()

    override suspend fun save(lesson: Lesson) {
        lessonRepository.save(lesson)
    }

    override suspend fun delete(lesson: Lesson) = lessonRepository.delete(lesson)

    override suspend fun size(): Int = lessonRepository.getNumberOfLessons()

    override suspend fun deleteAll() = lessonRepository.deleteAll()

    override suspend fun findByServerId(serverId: Long): Lesson? =
        lessonRepository.findByServerId(serverId)

    override suspend fun removeVocable(lesson: Lesson, vocable: Vocable): Boolean {
        val oldSize = lesson.words.size
        val new = lesson.words.filterNot { it.id == vocable.id }
        lesson.words.clear()
        lesson.words.addAll(new)
        lesson.words.applyChangesToDb()
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return oldSize != new.size
    }

    override suspend fun addVocable(lesson: Lesson, vocable: Vocable): Boolean {
        val success = lesson.words.add(vocable)
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return success
    }

    override fun convertToDTO(lesson: Lesson, vocableDTOs: List<VocableDTO>): LessonDTO {
        return LessonDTO(
            lesson.id,
            vocableDTOs,
            lesson.language,
            lesson.validationLanguage,
            lesson.lastChanged
        )
    }

}