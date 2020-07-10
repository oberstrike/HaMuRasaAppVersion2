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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime

class EditViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {


    @ExperimentalCoroutinesApi
    fun patchVocable(vocable: Vocable) {
        vocableService.update(vocable)

        val activeLesson = MainContext.EditContext.lesson.value ?: return
        val newLesson = lessonService.findById(activeLesson.id) ?: return
        MainContext.EditContext.setLesson(newLesson)

    }

    @ExperimentalCoroutinesApi
    fun deleteVocableFromLesson(vocableDTO: VocableDTO, lesson: Lesson) {
        val vocable = vocableService.findById(vocableDTO.id)!!

        lesson.words.removeIf { it.id == vocable.id }
        lesson.lastChanged = DateTime.now()
        lessonService.save(lesson)

        vocableService.delete(vocable)
     //   MainContext.EditContext.setLesson(null)
        MainContext.EditContext.setLesson(lesson)
    }
}