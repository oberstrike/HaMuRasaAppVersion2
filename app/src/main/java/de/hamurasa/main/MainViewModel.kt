package de.hamurasa.main

import android.accounts.AccountManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hamurasa.data.CommandLineRunner
import de.hamurasa.network.RetrofitServices
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.GsonObject
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.lesson.model.Lesson
import de.hamurasa.lesson.model.LessonRepository
import de.hamurasa.lesson.model.Vocable
import de.hamurasa.lesson.model.VocableRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.CompletableFuture

class MainViewModel(
    val context: Context,
    val provider: SchedulerProvider,
    private val lessonRepository: LessonRepository,
    private val vocableRepository: VocableRepository,
    private val commandLineRunner: CommandLineRunner
) : AbstractViewModel() {

    private val accountManager: AccountManager = AccountManager.get(context)

    lateinit var isLoggedIn: Observable<Boolean>

    lateinit var words: BehaviorSubject<List<Vocable>>

    val lessons = lessonRepository.findAll()

    fun init() {
        commandLineRunner.init()
        val loggedIn = accountManager.accounts.isNotEmpty()
        MainContext.isLoggedIn = Observable.just(loggedIn)
        isLoggedIn = MainContext.isLoggedIn
        words = BehaviorSubject.create()
        words.onNext(listOf())
    }


    fun logout() {
        if (accountManager.accounts.isNotEmpty()) {
            accountManager.removeAccountExplicitly(accountManager.accounts.first())
            MainContext.isLoggedIn = Observable.just(false)
            isLoggedIn = MainContext.isLoggedIn
        }
    }

    fun update() {
        val account = accountManager.accounts.first()
        val username = account.name
        val password = accountManager.getPassword(account)

        RetrofitServices.initVocableRetrofitService(username, password)
        RetrofitServices.initLessonRetrofitService(username, password)

        val response = CompletableFuture.supplyAsync {
            RetrofitServices.lessonRetrofitService.getLessons().execute()
        }

        val body = response.get().body()?.string()


        if (body != null) {
            val lessons: List<Lesson> = GsonObject.gson.fromJson(body)
            lessonRepository.deleteAll()

            for (lesson in lessons) {
                lesson.id = 0
                lessonRepository.save(lesson)
            }
            MainContext.lessons = BehaviorSubject.just(lessons)


        }
    }


    fun getWord(value: String) {

        if(value.isNotEmpty()){
            val response = CompletableFuture.supplyAsync {
                RetrofitServices.vocableRetrofitService.getWordsByText(value).blockingFirst()
            }.get()
            words.onNext(response)

        }else{
            words.onNext(listOf())
        }
    }
}

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)