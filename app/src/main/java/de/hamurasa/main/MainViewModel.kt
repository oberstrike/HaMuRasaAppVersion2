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
import de.hamurasa.util.withOnline
import io.reactivex.Observable
import io.reactivex.internal.operators.single.SingleDoOnSuccess
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class MainViewModel(
    val context: Context,
    val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val vocableService: VocableService
) : AbstractViewModel() {

    private val accountManager: AccountManager = AccountManager.get(context)

    lateinit var isLoggedIn: Observable<Boolean>


    fun init() {
        val loggedIn = accountManager.accounts.isNotEmpty()
        MainContext.isLoggedIn = Observable.just(loggedIn)
        isLoggedIn = MainContext.isLoggedIn
        MainContext.DictionaryContext.words = BehaviorSubject.create()

        MainContext.DictionaryContext.words.onNext(listOf())
        MainContext.HomeContext.lessons = lessonService.findAll()
    }

    fun saveLesson(lesson: Lesson) {
        try {
            lessonService.save(lesson)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }

    fun checkConnection() {
        if (!SettingsContext.forceOffline) {
            checkVersion()
        }
    }

    //Only Online
    fun logout() = withOnline {
        if (accountManager.accounts.isNotEmpty()) {
            accountManager.removeAccountExplicitly(accountManager.accounts.first())
            MainContext.isLoggedIn = Observable.just(false)
            isLoggedIn = MainContext.isLoggedIn
        }
    }

    //Only Online
    fun updateEdit() = withOnline {
        if (MainContext.EditContext.lesson.hasValue()) {
            val oldServerId = MainContext.EditContext.lesson.blockingFirst().serverId
            val newLesson = lessonService.findByServerId(oldServerId)
            if (newLesson != null) {
                MainContext.EditContext.lesson.onNext(newLesson)
            }
        }
    }

    //Only Online
    fun updateHome(async: Boolean = true) = withOnline(block = {
        val account = accountManager.accounts.first()
        val username = account.name
        val password = accountManager.getPassword(account)

        RetrofitServices.initVocableRetrofitService(username, password)
        RetrofitServices.initLessonRetrofitService(username, password)

        if (async) {
            requestAsync(action = {
                RetrofitServices.lessonRetrofitService.getLessons()
            }, onSuccess = { newLessons ->
                updateLessons(newLessons)
            })
        } else {
            request(action = {
                runBlocking {
                    RetrofitServices.lessonRetrofitService.getLessons()
                }
            }, onSuccess = { newLessons ->
                updateLessons(newLessons)

            })

        }

    }, alternative = {
        MainContext.HomeContext.lessons = lessonService.findAll()
    }, remember = false)

    //Only Online
    private fun updateLessons(newLessons: List<Lesson>) {
        val oldLessons = MainContext.HomeContext.lessons.blockingFirst()
        val combinedLessons = mutableListOf<Lesson>()

        for (newLesson in newLessons) {
            val serverId = newLesson.serverId
            val newLastChanged = newLesson.lastChanged

            val oldLesson = oldLessons.find { it.serverId == serverId }
            if (oldLesson != null) {
                val oldLastChanged = oldLesson.lastChanged
                when {
                    oldLastChanged.isAfter(newLastChanged) -> {
                        //EXPERIMENTAL
                        combinedLessons.add(oldLesson)
                        patchLesson(oldLesson)
                    }
                    oldLastChanged.isEqual(newLastChanged) -> {
                        for (vocable in newLesson.words) {
                            if (!oldLesson.words.contains(vocable)) {
                                oldLesson.words.add(vocable)
                            }
                        }
                        val distinct = oldLesson.words.distinctBy { it.serverId }
                        oldLesson.words.clear()
                        oldLesson.words.addAll(distinct)

                        combinedLessons.add(oldLesson)
                    }
                    oldLastChanged.isBefore(newLastChanged) -> {
                        newLesson.id = oldLesson.id
                        val oldVocables =
                            oldLesson.words.filter { newLesson.words.contains(it) }.toMutableList()
                        val newVocables = newLesson.words.filter { !oldLesson.words.contains(it) }

                        oldVocables.addAll(newVocables)
                        newLesson.words.clear()
                        newLesson.words.addAll(oldVocables)

                        combinedLessons.add(newLesson)
                    }
                }

            } else {
                combinedLessons.add(newLesson)
            }
        }

        lessonService.deleteAll()

        for (lesson in combinedLessons) {
            val oldLesson = lessonService.findById(lesson.id)
            if (oldLesson != null) {
                if (!oldLesson.words.containsAll(lesson.words)) {
                    saveLesson(lesson)
                }
            } else {
                saveLesson(lesson)
            }
        }


    }

    //Only Online
    fun getWord(value: String) = withOnline {
        if (value.isNotEmpty()) {
            requestAsync(action = {
                RetrofitServices.vocableRetrofitService.getWordsByText(value)
            }, onSuccess = {
                MainContext.DictionaryContext.words.onNext(it)
            })
        } else {
            MainContext.DictionaryContext.words.onNext(listOf())
        }
    }

    //Only Online + Offline
    fun addLesson(lesson: Lesson) = withOnline(block = {
        val lessonDTO = lessonService.convertToDTO(lesson, listOf())
        requestAsync(action = {
            RetrofitServices.lessonRetrofitService.addNewLesson(lessonDTO)
        }, onSuccess = {
            runBlocking {
                updateHome()
            }
        })
    }, alternative = {
        lessonService.save(lesson)
        println("Not Online")
    })

    //Only Online
    fun addVocableToServer(vocable: Vocable) = withOnline {
        requestAsync(action = {
            RetrofitServices.vocableRetrofitService.addVocable(vocableService.convertToDTO(vocable))
        }, onSuccess = {
            updateHome()
        })
    }

    //Only Online + Offline
    fun addVocableToLesson(
        vocable: Vocable,
        lessonServerId: Long
    ) {

        val vocableDTO = vocableService.convertToDTO(vocable)
        if (!vocable.isOffline) {
            withOnline(block = {
                requestAsync(action = {
                    RetrofitServices.lessonRetrofitService.addVocableToLesson(
                        lessonServerId,
                        vocableDTO
                    )
                }, onSuccess = {
                    updateHome()
                }, onFailure = {
                    println("Error in addVocableToLesson")
                    it.printStackTrace()
                })
            }, alternative = {
                addVocableToLessonOffline(lessonServerId, vocableDTO, vocable)
            })
        } else {
            addVocableToLessonOffline(lessonServerId, vocableDTO, vocable)
        }
    }

    //Only Offline
    private fun addVocableToLessonOffline(
        lessonServerId: Long,
        vocableDTO: VocableDTO,
        vocable: Vocable
    ) {
        val lesson = lessonService.findByServerId(lessonServerId)
        val newVocable = vocableService.save(vocableDTO, vocable.isOffline)
        lesson!!.words.add(newVocable)
        lessonService.save(lesson)

        MainContext.EditContext.lesson.onNext(lesson)
        updateHome()
    }

    fun getWordByTranslation(value: String) {
        if (value.isNotEmpty()) {
            requestAsync(action = {
                RetrofitServices.vocableRetrofitService.getWordsByTranslation(value)
            }, onSuccess = {
                MainContext.DictionaryContext.words.onNext(it)
            })
        } else {
            MainContext.DictionaryContext.words.onNext(listOf())

        }
    }

    fun deleteLesson(lesson: Lesson) = withOnline {
        requestAsync(action = {
            RetrofitServices.lessonRetrofitService.deleteLesson(lesson.serverId)
        }, onSuccess = {
            lessonService.delete(lesson)
            updateHome()
        }, onFailure = {
            it.printStackTrace()
        })
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


    fun patchVocable(vocable: Vocable) {
        val vocableDTO = vocableService.convertToDTO(vocable)
        val offline = vocable.isOffline

        if (!offline) {
            withOnline {
                request(action = {
                    runBlocking {
                        RetrofitServices.vocableRetrofitService.patchWord(vocableDTO.id, vocableDTO)
                    }
                }, onSuccess = {
                    updateHome()
                })
            }
        } else {
            vocableDTO.id = vocable.id
            val id = vocableService.save(vocableDTO)
            val oldVocable = vocableService.findById(id)!!

            oldVocable.serverId = 0
            vocableService.update(oldVocable)
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
                updateHome()
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

    fun setActiveLesson(id: Long) {
        val lesson = lessonService.findByServerId(id)
        if (lesson != null) {
            MainContext.EditContext.lesson.onNext(lesson)
        }
    }

    inline fun <T> requestAsync(
        crossinline action: suspend () -> T,
        crossinline onSuccess: (T) -> Unit
    ) {
        requestAsync(action = action, onSuccess = onSuccess, onFailure = {
            SettingsContext.isOffline
        })
    }


}

