package de.hamurasa.main

import android.accounts.AccountManager
import android.content.Context
import de.hamurasa.lesson.model.lesson.Lesson
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
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime

class MainViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
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

    fun saveLesson(lesson: Lesson, lastChanged: DateTime? = null) {
        try {
            if (lastChanged != null) {
                lesson.lastChanged = lastChanged
            }

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

        val newLessonsIds = newLessons.map { it.serverId }
        val oldLessonsIds = oldLessons.map { it.serverId }

        val newLessonsDates = newLessons.map { it.lastChanged }
        val oldLessonsDates = oldLessons.map { it.lastChanged }

        if (newLessonsIds == oldLessonsIds && newLessonsDates == oldLessonsDates) {
            return
        }

        val updateLessons = mutableMapOf<Lesson, Lesson>()
        val combined = mutableListOf<Lesson>()

        for (old in oldLessons) {
            val oldServerId = old.serverId
            if (newLessonsIds.contains(oldServerId)) {
                val new = newLessons.first { it.serverId == oldServerId }
                if (new.lastChanged.isBefore(old.lastChanged)) {
                    new.words.addAll(old.words.filter { it.isOffline })

                    val oldLessonsLastChanged = old.words.map { it.lastChanged }
                    val newLessonsLastChanged = new.words.map { it.lastChanged }
                    if (oldLessonsLastChanged != newLessonsLastChanged) {
                        updateLessons[old] = new
                    } else {
                        combined.add(new)
                    }
                } else {
                    combined.add(new)
                }
            } else {
                if (old.isOffline) {
                    combined.add(old)
                }
            }
        }

        for (new in newLessons) {
            val newServerId = new.serverId
            if (!oldLessonsIds.contains(newServerId)) {
                combined.add(new)
            }

        }

        lessonService.deleteAll()
        vocableService.deleteAll()

        for (lesson in combined) {
            saveLesson(lesson)
        }
        MainContext.HomeContext.updateLessons.onNext(updateLessons)
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
    fun addLesson(lesson: Lesson) {
        if (!lesson.isOffline) {
            withOnline(block = {
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
            })
        } else {
            lessonService.save(lesson)
        }
    }

    //Only Online
    fun addVocableToServer(vocable: Vocable) = withOnline {
        requestAsync(action = {
            RetrofitServices.vocableRetrofitService.addVocable(vocableService.convertToDTO(vocable))
        }, onSuccess = {
            val newVocable = vocableService.save(it, false)
            MainContext.DictionaryContext.words.onNext(listOf(newVocable))
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
        lessonId: Long,
        vocableDTO: VocableDTO,
        vocable: Vocable
    ) {
        val lesson = lessonService.findById(lessonId)

        val newVocable = vocableService.save(vocableDTO, vocable.isOffline)
        lesson!!.words.add(newVocable)
        lesson.lastChanged = DateTime.now()
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

    fun deleteLesson(lesson: Lesson) {
        if (!lesson.isOffline) {
            withOnline {
                requestAsync(action = {
                    RetrofitServices.lessonRetrofitService.deleteLesson(lesson.serverId)
                }, onSuccess = {
                    lessonService.delete(lesson)
                    updateHome()
                }, onFailure = {
                    it.printStackTrace()
                })
            }
        } else {
            lessonService.delete(lesson)
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

            lesson.words.removeIf { it.id == vocable.id }
            lesson.lastChanged = DateTime.now()
            lessonService.save(lesson)

            vocableService.delete(vocable)
            MainContext.EditContext.lesson.onNext(lesson)
            updateHome()
        }
    }

    fun <T> observe(observable: Observable<T>, action: (value: T) -> Unit) {
        observe(
            observable, provider.computation(), provider.ui(), action
        )
    }

    fun setActiveLesson(id: Long) {
        val lesson = lessonService.findById(id)
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

