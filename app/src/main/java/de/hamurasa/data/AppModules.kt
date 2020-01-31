package de.hamurasa.data

import de.hamurasa.login.LoginViewModel
import de.hamurasa.main.MainViewModel
import de.hamurasa.network.createUserService
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.SchedulerProviderImpl
import de.hamurasa.vocable.model.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {
    single { ObjectBox.init(get()) }

    single { SchedulerProviderImpl() } bind SchedulerProvider::class

    single { VocableRepositoryImpl() } bind VocableRepository::class

    single { LessonRepositoryImpl() } bind LessonRepository::class

    single { CommandLineRunner.init(get(), get()) }

    viewModel { MainViewModel(get(), get(),get(), get()) }

    viewModel { LoginViewModel(get()) }
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

    fun init(){
        val lesson = Lesson()
        lessonRepository.deleteAll()
        lesson.words.add(Vocable(0,"abuelo", "Nomen", listOf("Großvater")))
        lessonRepository.save(lesson)
        val observable = lessonRepository.findAll()
        val lessons = observable.blockingFirst()
        println(lessons)
    }


}