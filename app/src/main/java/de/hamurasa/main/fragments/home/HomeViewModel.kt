package de.hamurasa.main.fragments.home

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {

    @ExperimentalCoroutinesApi
    suspend fun updateHome() {
        MainContext.HomeContext.setLessons(lessonService.findAll())
    }


    @ExperimentalCoroutinesApi
    suspend fun deleteLesson(lesson: Lesson) {
        lessonService.delete(lesson)

        MainContext.HomeContext.setLessons(lessonService.findAll())
    }


    @ExperimentalCoroutinesApi
    suspend fun saveLesson(lesson: Lesson) {
        lessonService.save(lesson)

        MainContext.HomeContext.setLessons(lessonService.findAll())
    }

}