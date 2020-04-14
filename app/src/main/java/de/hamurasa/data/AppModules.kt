package de.hamurasa.data

import de.hamurasa.login.LoginViewModel
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.SchedulerProviderImpl
import de.hamurasa.lesson.session.SessionViewModel
import de.hamurasa.lesson.model.lesson.LessonRepository
import de.hamurasa.lesson.model.lesson.LessonRepositoryImpl
import de.hamurasa.lesson.model.lesson.LessonService
import de.hamurasa.lesson.model.lesson.LessonServiceImpl
import de.hamurasa.lesson.model.vocable.VocableRepository
import de.hamurasa.lesson.model.vocable.VocableRepositoryImpl
import de.hamurasa.lesson.model.vocable.VocableService
import de.hamurasa.lesson.model.vocable.VocableServiceImpl
import de.hamurasa.settings.SettingsViewModel
import de.hamurasa.settings.model.Settings
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {
    single { ObjectBox.init(get()) }

    single { SchedulerProviderImpl() } bind SchedulerProvider::class

    single { VocableRepositoryImpl() } bind VocableRepository::class

    single { LessonRepositoryImpl() } bind LessonRepository::class

    single { LessonServiceImpl(get()) } bind LessonService::class

    single { VocableServiceImpl(get()) } bind VocableService::class

    single { Settings(get()) }

    viewModel { MainViewModel(get(), get(), get(), get()) }

    viewModel { LoginViewModel(get()) }

    viewModel {
        SessionViewModel(
            get(),
            get()
        )
    }

    viewModel {  SettingsViewModel(get()) }

}