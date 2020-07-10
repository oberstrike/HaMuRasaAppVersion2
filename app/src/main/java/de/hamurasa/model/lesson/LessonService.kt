package de.hamurasa.model.lesson

import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableDTO
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

interface LessonService {
    fun findById(id: Long): Lesson?

    fun findAll(): List<Lesson>

    fun save(lesson: Lesson)

    fun delete(lesson: Lesson)

    fun size(): Int

    fun deleteAll()

    fun findByServerId(serverId: Long): Lesson?

    fun removeVocable(lesson: Lesson, vocable: Vocable): Boolean

    fun addVocable(lesson: Lesson, vocable: Vocable): Boolean

    fun convertToDTO(lesson: Lesson, vocableDTOs: List<VocableDTO>): LessonDTO
}

class LessonServiceImpl(private val lessonRepository: LessonRepository) : LessonService {

    override fun findById(id: Long): Lesson? = lessonRepository.findById(id)

    override fun findAll(): List<Lesson> = lessonRepository.findAll()

    override fun save(lesson: Lesson) {
        lessonRepository.save(lesson)
    }

    override fun delete(lesson: Lesson) = lessonRepository.delete(lesson)

    override fun size(): Int = lessonRepository.size()

    override fun deleteAll() = lessonRepository.deleteAll()

    override fun findByServerId(serverId: Long): Lesson? = lessonRepository.findByServerId(serverId)

    override fun removeVocable(lesson: Lesson, vocable: Vocable): Boolean {
        val oldSize = lesson.words.size
        val new = lesson.words.filterNot { it.id == vocable.id }
        lesson.words.clear()
        lesson.words.addAll(new)
        lesson.words.applyChangesToDb()
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return oldSize != new.size
    }

    override fun addVocable(lesson: Lesson, vocable: Vocable): Boolean {
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