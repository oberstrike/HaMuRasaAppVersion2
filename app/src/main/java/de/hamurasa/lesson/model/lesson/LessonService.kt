package de.hamurasa.lesson.model.lesson

import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import io.reactivex.Observable
import org.joda.time.DateTime

interface LessonService {
    fun findById(id: Long): Lesson?


    fun findAll(): Observable<List<Lesson>>

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

    override fun findAll(): Observable<List<Lesson>> = lessonRepository.findAll()

    override fun save(lesson: Lesson) {
        lessonRepository.save(lesson)
    }

    override fun delete(lesson: Lesson) = lessonRepository.delete(lesson)

    override fun size(): Int = lessonRepository.size()

    override fun deleteAll() = lessonRepository.deleteAll()

    override fun findByServerId(serverId: Long): Lesson? = lessonRepository.findByServerId(serverId)

    override fun removeVocable(lesson: Lesson, vocable: Vocable): Boolean {
        val success = lesson.words.removeAll { it.id == vocable.id }
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return success
    }

    override fun addVocable(lesson: Lesson, vocable: Vocable): Boolean {
        val success = lesson.words.add(vocable)
        lesson.lastChanged = DateTime.now()
        lessonRepository.save(lesson)
        return success
    }

    override fun convertToDTO(lesson: Lesson, vocableDTOs: List<VocableDTO>): LessonDTO {
        return LessonDTO(
            lesson.serverId,
            vocableDTOs,
            lesson.language,
            lesson.validationLanguage,
            lesson.lastChanged
        )
    }

}