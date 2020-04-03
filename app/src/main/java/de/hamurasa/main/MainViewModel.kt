package de.hamurasa.main

import android.accounts.AccountManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hamurasa.data.CommandLineRunner
import de.hamurasa.lesson.model.*
import de.hamurasa.network.RetrofitServices
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.GsonObject
import de.hamurasa.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.coroutineContext

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

        GlobalScope.launch(Dispatchers.IO) {
            val lessons: List<Lesson> = RetrofitServices.lessonRetrofitService.getLessons()
            val oldLessons = lessonRepository.findAll().blockingFirst()

            for (oldLesson in oldLessons) {
                if (oldLesson !in lessons) {
                    lessonRepository.delete(oldLesson)
                }
            }

            for (newLesson in lessons) {
                val serverId = newLesson.serverId
                val words = newLesson.words

                val oldLesson = oldLessons.find { it.serverId == serverId }

                if (oldLesson != null) {
                    val oldWords = oldLesson.words
                    for (word in words) {
                        if (word !in oldWords) {
                            oldLesson.words.add(word)
                        }
                    }
                    lessonRepository.save(oldLesson)
                } else {
                    lessonRepository.save(newLesson)
                }
            }
            MainContext.lessons = BehaviorSubject.just(lessons)
        }

    }


    fun getWord(value: String) {
        if (value.isNotEmpty()) {
            CompletableFuture.supplyAsync {
                RetrofitServices.vocableRetrofitService.getWordsByText(value).blockingFirst()
            }
                .thenApply {
                    words.onNext(it)
                }
        } else {
            words.onNext(listOf())
        }
    }

    fun addLesson(lesson: LessonDTO) {

        <<<<<<< HEAD
        GlobalScope.launch(Dispatchers.IO) {
            val result = RetrofitServices.lessonRetrofitService.addNewLesson(lesson)
            //    val body = result.string()
            //   GsonObject.gson.fromJson<LessonDTO>(body)
            update()
        }
    }

    fun addVocable(vocableDTO: VocableDTO) {

        GlobalScope.launch(Dispatchers.IO) {
            val result = RetrofitServices.vocableRetrofitService.addVocable(vocableDTO)
            result.string()
            update()
        }

    }


    fun addVocableToLesson(vocableDTO: VocableDTO, lessonId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val result =
                RetrofitServices.lessonRetrofitService.addVocableToLesson(lessonId, vocableDTO)
        }
    }

    fun getWordByTranslation(value: String) {
        if (value.isNotEmpty()) {
            val response = CompletableFuture.supplyAsync {
                RetrofitServices.vocableRetrofitService.getWordsByTranslation(value).blockingFirst()
            }.get()
            words.onNext(response)
        } else {
            words.onNext(listOf())
        }

    }

}

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)