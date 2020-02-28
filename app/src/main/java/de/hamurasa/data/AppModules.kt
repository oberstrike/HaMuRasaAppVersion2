package de.hamurasa.data

import de.hamurasa.login.LoginViewModel
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.GsonObject
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.SchedulerProviderImpl
import de.hamurasa.lesson.LessonViewModel
import de.hamurasa.lesson.model.*
import de.hamurasa.settings.SettingsViewModel
import de.hamurasa.util.AbstractViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {
    single { ObjectBox.init(get()) }

    single { SchedulerProviderImpl() } bind SchedulerProvider::class

    single { VocableRepositoryImpl() } bind VocableRepository::class

    single { LessonRepositoryImpl() } bind LessonRepository::class

    single { CommandLineRunner.init(get(), get()) }

    viewModel { MainViewModel(get(), get(), get(), get(), get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { LessonViewModel(get(), get()) }

    viewModel {  SettingsViewModel() }
}


class CommandLineRunner(
    val vocableRepository: VocableRepository,
    val lessonRepository: LessonRepository
) {
    companion object {
        fun init(
            vocableRepository: VocableRepository,
            lessonRepository: LessonRepository
        ): CommandLineRunner {
            return CommandLineRunner(vocableRepository, lessonRepository)

        }
    }

    var isInit = false

    fun init() {
        if(isInit)
            return

        val lesson = Lesson()
        lessonRepository.deleteAll()
        lesson.words.add(Vocable(0,0, "abuelo", "Nomen", listOf("Großvater")))
        lessonRepository.save(lesson)
        val observable = lessonRepository.findAll()
        val lessons = observable.blockingFirst()

        val body = GsonObject.gson.toJson(lessons)
        isInit = true
    }


}