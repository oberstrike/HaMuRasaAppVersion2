package de.hamurasa.main

import android.accounts.AccountManager
import android.content.Context
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.lesson.LessonDTO
import de.hamurasa.lesson.model.lesson.LessonService
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableService
import de.hamurasa.network.RetrofitServices
import de.hamurasa.network.request
import de.hamurasa.network.requestAsync
import de.hamurasa.settings.SettingsContext
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import java.lang.Exception

class MainViewModel(
    val context: Context,
    val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : AbstractViewModel() {

    private val accountManager: AccountManager = AccountManager.get(context)

    lateinit var isLoggedIn: Observable<Boolean>

    lateinit var words: BehaviorSubject<List<Vocable>>

    fun init() {
        val loggedIn = accountManager.accounts.isNotEmpty()
        MainContext.isLoggedIn = Observable.just(loggedIn)
        isLoggedIn = MainContext.isLoggedIn
        words = BehaviorSubject.create()

        words.onNext(listOf())
        MainContext.HomeContext.lessons = lessonService.findAll()
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


    fun update() = withOnline(block = {
        val account = accountManager.accounts.first()
        val username = account.name
        val password = accountManager.getPassword(account)

        RetrofitServices.initVocableRetrofitService(username, password)
        RetrofitServices.initLessonRetrofitService(username, password)

        requestAsync(action = {
            RetrofitServices.lessonRetrofitService.getLessons()
        }, onSuccess = { newLessons ->
            val oldLessons = MainContext.HomeContext.lessons.blockingFirst()
            val combinedLessons = mutableListOf<Lesson>()

            for (newLesson in newLessons) {
                val serverId = newLesson.serverId
                val newLastChanged = newLesson.lastChanged

                val oldLesson = oldLessons.find { it.serverId == serverId }
                if (oldLesson != null) {
                    val oldLastChanged = oldLesson.lastChanged
                    if (newLastChanged.isAfter(oldLastChanged) || newLastChanged.isEqual(
                            oldLastChanged
                        )
                    ) {
                        combinedLessons.add(newLesson)
                    } else {
                        patchLesson(oldLesson)
                        combinedLessons.add(newLesson)
                    }
                } else {
                    combinedLessons.add(newLesson)
                }
            }

            MainContext.HomeContext.lessons = BehaviorSubject.just(combinedLessons)
            lessonService.deleteAll()

            for (lesson in combinedLessons) {
                lessonService.save(lesson)
            }

            if (MainContext.EditContext.lesson.hasValue()) {
                val oldServerId = MainContext.EditContext.lesson.blockingFirst().serverId
                val newLesson = combinedLessons.first { it.serverId == oldServerId }

                MainContext.EditContext.lesson.onNext(newLesson)
            }


        })
    }, alternative = {
        MainContext.HomeContext.lessons = lessonService.findAll()
    })


    fun getWord(value: String) {
        if (value.isNotEmpty()) {
            requestAsync(action = {
                RetrofitServices.vocableRetrofitService.getWordsByText(value)
            }, onSuccess = {
                words.onNext(it)
            })
        } else {
            words.onNext(listOf())
        }
    }

    fun addLesson(lesson: LessonDTO) = withOnline(block = {
        requestAsync(action = {
            RetrofitServices.lessonRetrofitService.addNewLesson(lesson)
        }, onSuccess = {
            runBlocking {
                update()
            }
        })
    }, alternative = {
        println("Not Online")
    })

    fun addVocableToServer(vocableDTO: VocableDTO) = withOnline {
        requestAsync(action = {
            RetrofitServices.vocableRetrofitService.addVocable(vocableDTO)
        }, onSuccess = {
            update()
        })
    }

    fun addVocableToLesson(vocableDTO: VocableDTO, lessonServerId: Long) {
        withOnline(block = {
            requestAsync(action = {
                RetrofitServices.lessonRetrofitService.addVocableToLesson(
                    lessonServerId,
                    vocableDTO
                )
            }, onSuccess = {
                update()
            }, onFailure = {
                println("Error in addVocableToLesson")
                it.printStackTrace()
            })
        }, alternative = {
            update()
            val lesson = lessonService.findByServerId(lessonServerId)
            val vocable = vocableService.save(vocableDTO, true)
            lesson!!.words.add(vocable)
            lessonService.save(lesson)

            MainContext.EditContext.lesson.onNext(lesson)
        })
    }

    fun getWordByTranslation(value: String) {
        if (value.isNotEmpty()) {
            requestAsync(action = {
                RetrofitServices.vocableRetrofitService.getWordsByTranslation(value)
            }, onSuccess = {
                words.onNext(it)
            })
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

    fun patchLesson(lesson: Lesson) = withOnline {
        requestAsync(action = {
            val vocableDTOs = lesson.words.map { vocableService.convertToDTO(it) }.toList()
            val lessonDTO = lessonService.convertToDTO(lesson, vocableDTOs)
            RetrofitServices.lessonRetrofitService.patchLesson(lesson.id, lessonDTO)
        }, onSuccess = { newLesson ->
            lessonService.save(newLesson)
        })
    }


    private fun checkVersion() =
        request(action = {
            runBlocking {
                RetrofitServices.updateRetrofitService.status()
            }
            SettingsContext.isOffline = Observable.just(false)
        }, onFailure = {
            SettingsContext.isOffline = Observable.just(true)
        })

    private inline fun withOnline(
        crossinline alternative: () -> Unit = {},
        crossinline block: () -> Unit
    ) {
        val offline = SettingsContext.isOffline.blockingFirst()
        if (!offline) {
            try {
                block.invoke()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        } else {
            alternative.invoke()
        }
    }


    fun patchVocable(vocableDTO: VocableDTO, offline: Boolean) {
        if (!offline) {
            withOnline {
                request(action = {
                    runBlocking {
                        RetrofitServices.vocableRetrofitService.patchWord(vocableDTO.id, vocableDTO)
                    }
                }, onSuccess = {
                    update()
                })
            }
        } else {
            vocableService.update(vocableDTO)
            val lessonId = MainContext.EditContext.lesson.blockingFirst().id
            val lesson = lessonService.findById(lessonId)!!
            MainContext.EditContext.lesson.onNext(lesson)

        }
    }

    fun deleteVocableFromLesson(vocableDTO: VocableDTO, offline: Boolean) {
        if (!offline) {
            request(action = {
                val serverId = MainContext.EditContext.lesson.blockingFirst().serverId
                runBlocking {
                    RetrofitServices.lessonRetrofitService.removeVocableFromLesson(
                        serverId,
                        vocableDTO.id
                    )
                }

                true
            }, onFailure = {
                it.printStackTrace()
            }, onSuccess = {
                update()
            })

        } else {
            val vocable = vocableService.findById(vocableDTO.id)!!
            val lesson = MainContext.EditContext.lesson.blockingFirst()
            lessonService.removeVocable(lesson, vocable)
            MainContext.EditContext.lesson.onNext(lesson)
        }
    }

    fun <T> observe(observable: Observable<T>, action: (value: T) -> Unit) {
        observe(
            observable, provider.computation(), provider.ui(), action
        )
    }
}