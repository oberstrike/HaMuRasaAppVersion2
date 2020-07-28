package de.hamurasa.main.fragments.edit

import android.content.Context
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.main.MainContext
import de.hamurasa.data.lesson.LessonService
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.data.vocableStats.VocableStats
import de.hamurasa.data.vocableStats.VocableStatsService
import de.hamurasa.data.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime

class EditViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService,
    private val vocableStatsService: VocableStatsService
) : BaseViewModel(provider) {


    @ExperimentalCoroutinesApi
    suspend fun patchVocable(vocable: Vocable) {
        vocableService.save(vocable)

        val activeLesson = MainContext.EditContext.lesson.value ?: return
        val newLesson = lessonService.findById(activeLesson.id) ?: return
        MainContext.EditContext.setLesson(newLesson)

    }

    @ExperimentalCoroutinesApi
    suspend fun deleteVocableFromLesson(vocable: Vocable, lesson: Lesson) {
        //val vocable = vocableService.findById(vocable.id)!!
        lessonService.deleteVocableFromLesson(vocable, lesson)

        MainContext.EditContext.setLesson(lesson)
    }

    suspend fun stats(vocable: Vocable): VocableStats? {
        return vocableStatsService.findByVocable(vocable)
    }
}