package de.hamurasa.main.fragments.dictionary

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableDTO
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.joda.time.DateTime

class DictionaryViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {


    //Only Online + Offline
    @ExperimentalCoroutinesApi
    fun addVocableToLesson(
        vocable: Vocable,
        lessonServerId: Long
    ) {
        val vocableDTO = vocableService.convertToDTO(vocable)
        addVocableToLesson(lessonServerId, vocableDTO)

    }

    //Only Offline
    @ExperimentalCoroutinesApi
    private fun addVocableToLesson(
        lessonId: Long,
        vocableDTO: VocableDTO
    ) {
        val lesson = lessonService.findById(lessonId)?: return

        val newVocable = vocableService.save(vocableDTO)
        if (newVocable != null) {
            lesson.words.add(newVocable)
            lesson.lastChanged = DateTime.now()
            lessonService.save(lesson)
            MainContext.EditContext.setLesson(lesson)

        }
    }

    @ExperimentalCoroutinesApi
    fun getWord(name: String) {
        val vocables = vocableService.findByName(name)
        MainContext.DictionaryContext.setWords(vocables)

    }

}