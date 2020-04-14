package de.hamurasa.main

import android.accounts.AccountManager
import android.content.Context
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.lesson.LessonDTO
import de.hamurasa.lesson.model.lesson.LessonRepository
import de.hamurasa.lesson.model.lesson.LessonService
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableService
import de.hamurasa.network.RetrofitServices
import de.hamurasa.settings.SettingsContext
import de.hamurasa.settings.request
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

class MainViewModel(
    val context: Context,
    val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : AbstractViewModel() {

    private val accountManager: AccountManager = AccountManager.get(context)

    lateinit var isLoggedIn: Observable<Boolean>

    lateinit var words: BehaviorSubject<List<Vocable>>

    val lessons = lessonService.findAll()

    fun init() {
        val loggedIn = accountManager.accounts.isNotEmpty()
        MainContext.isLoggedIn = Observable.just(loggedIn)
        isLoggedIn = MainContext.isLoggedIn
        words = BehaviorSubject.create()

        words.onNext(listOf())
        checkConnection()
    }

    fun checkConnection() {
        if (!SettingsContext.forceOffline) {
            runBlocking {
                checkVersion()
            }
        }
    }


    fun logout() = withOnline {
        if (accountManager.accounts.isNotEmpty()) {
            accountManager.removeAccountExplicitly(accountManager.accounts.first())
            MainContext.isLoggedIn = Observable.just(false)
            isLoggedIn = MainContext.isLoggedIn
        }
    }


    fun update() = withOnline {
        val account = accountManager.accounts.first()
        val username = account.name
        val password = accountManager.getPassword(account)

        RetrofitServices.initVocableRetrofitService(username, password)
        RetrofitServices.initLessonRetrofitService(username, password)

        //Receive the current status of the server
        GlobalScope.launch(Dispatchers.IO) {
            val lessons: List<Lesson> = RetrofitServices.lessonRetrofitService.getLessons()
            val oldLessons = lessonService.findAll().blockingFirst()

            for (oldLesson in oldLessons) {
                if (oldLesson !in lessons) {
                    lessonService.delete(oldLesson)
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
                    lessonService.save(oldLesson)
                } else {
                    lessonService.save(newLesson)
                }
            }
            MainContext.HomeContext.lessons = BehaviorSubject.just(lessons)
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

    fun addLesson(lesson: LessonDTO) = withOnline(block = {
        GlobalScope.launch(Dispatchers.IO) {
            RetrofitServices.lessonRetrofitService.addNewLesson(lesson)
            update()
        }
    }, alternative = {
        println("Not Online")

    })

    fun addVocableToServer(vocableDTO: VocableDTO) = withOnline {
        GlobalScope.launch(Dispatchers.IO) {
            RetrofitServices.vocableRetrofitService.addVocable(vocableDTO)
            update()
        }
    }


    fun addVocableToLesson(vocableDTO: VocableDTO, lessonServerId: Long) {
        withOnline {
            GlobalScope.launch(Dispatchers.IO) {
                RetrofitServices.lessonRetrofitService.addVocableToLesson(
                    lessonServerId,
                    vocableDTO
                )
            }
        }
        val lesson = lessonService.findByServerId(lessonServerId)
        val vocable = vocableService.save(vocableDTO, true)
        lesson!!.words.add(vocable)
        lessonService.save(lesson)

        MainContext.EditContext.lesson.onNext(lesson)
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

    fun deleteLesson(lesson: Lesson) = withOnline {
        GlobalScope.launch(Dispatchers.IO) {
            RetrofitServices.lessonRetrofitService.deleteLesson(lesson.serverId)
            lessonService.delete(lesson)
            update()
        }
    }


    private suspend fun checkVersion() =
        GlobalScope.launch(Dispatchers.IO) {
            val status = request(true) {
                !RetrofitServices.updateRetrofitService.status().execute().isSuccessful
            }

            SettingsContext.isOffline = Observable.just(status)
        }.join()

    private inline fun withOnline(
        crossinline block: () -> Unit,
        crossinline alternative: () -> Unit
    ) {
        val offline = SettingsContext.isOffline.blockingFirst()
        if (!offline) {
            block.invoke()
        } else {
            alternative.invoke()
        }
    }

    private inline fun withOnline(crossinline block: () -> Unit) {
        withOnline(block) {
            Unit
        }
    }


    fun patchVocable(vocableDTO: VocableDTO, offline: Boolean) {
        if (!offline) {
            withOnline {
                //TODO ONLINE DELETE
            }
        } else {
            vocableService.update(vocableDTO)
            val lessonId = MainContext.EditContext.lesson.blockingFirst().id
            val lesson = lessonService.findById(lessonId)!!
            MainContext.EditContext.lesson.onNext(lesson)

        }
    }

    fun deleteVocable(vocableDTO: VocableDTO, offline: Boolean) {
        if (!offline) {
            withOnline {
                val serverId = MainContext.EditContext.lesson.blockingFirst().serverId
                GlobalScope.launch(Dispatchers.IO) {
                    RetrofitServices.lessonRetrofitService.removeVocableFromLesson(
                        serverId,
                        vocableDTO.id
                    )
                }
            }
        } else {
            val vocable = vocableService.findById(vocableDTO.id)!!
            val lesson = MainContext.EditContext.lesson.blockingFirst()
            lessonService.removeVocable(lesson, vocable)
            MainContext.EditContext.lesson.onNext(lesson)
        }


    }
}