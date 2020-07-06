package de.hamurasa.main.fragments.edit

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableDTO
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import org.joda.time.DateTime

class EditViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {


    fun patchVocable(vocable: Vocable) {
        val vocableDTO = vocableService.convertToDTO(vocable)
        vocableDTO.id = vocable.id
        val id = vocableService.save(vocableDTO)?.id ?: return
        val oldVocable = vocableService.findById(id)!!

        vocableService.update(oldVocable)
        val lessonId = MainContext.EditContext.lesson.blockingFirst().id
        val lesson = lessonService.findById(lessonId)!!
        MainContext.EditContext.lesson.onNext(lesson)
    }

    fun deleteVocableFromLesson(vocableDTO: VocableDTO, lesson: Lesson) {
        val vocable = vocableService.findById(vocableDTO.id)!!

        lesson.words.removeIf { it.id == vocable.id }
        lesson.lastChanged = DateTime.now()
        lessonService.save(lesson)

        vocableService.delete(vocable)
        MainContext.EditContext.lesson.onNext(lesson)
    }


}