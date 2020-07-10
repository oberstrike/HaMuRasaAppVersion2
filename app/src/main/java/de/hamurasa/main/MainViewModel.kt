package de.hamurasa.main


import android.accounts.AccountManager
import android.content.Context
import com.google.gson.Gson

import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.GsonObject
import de.hamurasa.util.SchedulerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class MainViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {


    @ExperimentalCoroutinesApi
    fun init() {
        MainContext.HomeContext.setLessons(lessonService.findAll())
    }


    fun export(): String {
        val allLessons = lessonService.findAll()
        return GsonObject.gson.toJson(allLessons)
    }
}
