package de.hamurasa.main.fragments.home

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider

class HomeViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {

    fun updateHome() {
        MainContext.HomeContext.lessons = lessonService.findAll()
    }

    fun deleteLesson(lesson: Lesson) {
        lessonService.delete(lesson)
    }


    fun saveLesson(lesson: Lesson) {
        lessonService.save(lesson)
    }

}