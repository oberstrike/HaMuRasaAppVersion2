package de.hamurasa.main


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.accounts.AccountManager
import android.content.Context



import de.hamurasa.main.fragments.dialogs.ImportExportDialog
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.vocable.VocableService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.GsonObject
import de.hamurasa.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


class MainViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : BaseViewModel(provider) {

    private val accountManager: AccountManager = AccountManager.get(context)

    fun init() {
        MainContext.isLoggedIn = isLoggedIn()
        MainContext.DictionaryContext.words = BehaviorSubject.create()

        MainContext.DictionaryContext.words = Observable.just(listOf())
        MainContext.HomeContext.lessons = lessonService.findAll()
    }

    fun isLoggedIn() = accountManager.accounts.isNotEmpty()


    fun setActiveLesson(id: Long) {
        val lesson = lessonService.findById(id)
        if (lesson != null) {
            MainContext.EditContext.lesson.onNext(lesson)
        }
    }

    fun export(): String {
        val allLessons = lessonService.findAll().blockingFirst()
        return GsonObject.gson.toJson(allLessons)
    }


}

