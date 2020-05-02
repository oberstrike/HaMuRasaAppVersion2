package de.hamurasa.data

import de.hamurasa.lesson.model.lesson.*
import de.hamurasa.login.LoginViewModel
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.SchedulerProviderImpl
import de.hamurasa.session.SessionViewModel
import de.hamurasa.lesson.model.vocable.*
import de.hamurasa.main.fragments.edit.EditVocableDialog
import de.hamurasa.main.fragments.dialogs.NewVocableDialog
import de.hamurasa.main.fragments.edit.EditViewModel
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

    viewModel { SessionViewModel(get(), get()) }

    viewModel { SettingsViewModel(get()) }

    viewModel { EditViewModel(get(), get(), get(), get()) }
}


val modelModules = module {
    factory { Vocable() }

    factory { Lesson() }

    factory { NewVocableDialog(get()) }

    factory { params -> EditVocableDialog(params[0]) }


}