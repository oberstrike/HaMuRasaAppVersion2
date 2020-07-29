package de.hamurasa.main.fragments.dictionary

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.data.lesson.LessonService
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.data.vocableStats.VocableStats
import de.hamurasa.data.vocableStats.VocableStatsService
import de.hamurasa.data.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import io.objectbox.kotlin.applyChangesToDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime

class DictionaryViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService,
    private val vocableStatsService: VocableStatsService
) : BaseViewModel(provider) {


    //Only Online + Offline
    @ExperimentalCoroutinesApi
    suspend fun addVocableToLesson(
        vocable: Vocable,
        lessonServerId: Long
    ) {
        addVocableToLesson(lessonServerId, vocable)

    }

    //Only Offline
    @ExperimentalCoroutinesApi
    private suspend fun addVocableToLesson(
        lessonId: Long,
        vocable: Vocable
    ) {
        val lesson = lessonService.findById(lessonId) ?: return
        if (lesson.words.any { it.value == vocable.value && it.translation == vocable.translation })
            return

        lesson.words.applyChangesToDb {
            add(vocable)
        }
        lesson.lastChanged = DateTime.now()
        lessonService.save(lesson)

        MainContext.EditContext.change(lesson)

    }

    @ExperimentalCoroutinesApi
    suspend fun getWord(name: String) {
        val vocables = vocableService.findByName(name)
        MainContext.DictionaryContext.change(vocables)

    }

    suspend fun saveVocableStats(vocableStats: VocableStats) {
        vocableStatsService.save(vocableStats)
    }

    @ExperimentalCoroutinesApi
    suspend fun deleteVocable(vocable: Vocable) {
        val lessons = lessonService.findByVocableId(vocableId = vocable.id)
        for (lesson in lessons) {
            lessonService.deleteVocableFromLesson(vocable, lesson)
        }

        vocableService.delete(vocable)
        val words = MainContext.DictionaryContext.value()
        MainContext.DictionaryContext.change(words.filterNot { it.id == vocable.id })

    }

    suspend fun saveVocable(vocable: Vocable): Vocable {
        return vocableService.save(vocable)
    }

}