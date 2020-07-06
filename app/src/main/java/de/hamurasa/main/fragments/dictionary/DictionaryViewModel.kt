package de.hamurasa.main.fragments.dictionary

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableDTO
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTime

class DictionaryViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {

    fun init() {
        MainContext.DictionaryContext.words = Observable.just(listOf())
    }

    //Only Online + Offline
    fun addVocableToLesson(
        vocable: Vocable,
        lessonServerId: Long
    ) {
        val vocableDTO = vocableService.convertToDTO(vocable)
        addVocableToLesson(lessonServerId, vocableDTO)

    }

    //Only Offline
    private fun addVocableToLesson(
        lessonId: Long,
        vocableDTO: VocableDTO
    ) {
        val lesson = lessonService.findById(lessonId)

        val newVocable = vocableService.save(vocableDTO)
        if (newVocable != null) {
            lesson!!.words.add(newVocable)
            lesson.lastChanged = DateTime.now()
            lessonService.save(lesson)
            MainContext.EditContext.lesson.onNext(lesson)
        }
    }

    fun getWord(name: String) {
        MainContext.DictionaryContext.words = vocableService.findByName(name)
    }

}