package de.hamurasa.main.fragments.edit

import android.content.Context
import de.hamurasa.lesson.model.lesson.LessonService
import de.hamurasa.lesson.model.vocable.VocableService
import de.hamurasa.main.MainContext
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.withOnline
import io.reactivex.Observable

class EditViewModel(
    val context: Context,
    val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : AbstractViewModel() {
    

    fun <T> observe(observable: Observable<T>, action: (value: T) -> Unit) {
        observe(
            observable, provider.computation(), provider.ui(), action
        )
    }


    //Only Online
    fun updateEdit() = withOnline {
        if (MainContext.EditContext.lesson.hasValue()) {
            val oldServerId = MainContext.EditContext.lesson.blockingFirst().serverId
            val newLesson = lessonService.findByServerId(oldServerId)
            if (newLesson != null) {
                MainContext.EditContext.lesson.onNext(newLesson)
            }
            //TODO WHEN LESSON IS DELETED
        }
    }


}