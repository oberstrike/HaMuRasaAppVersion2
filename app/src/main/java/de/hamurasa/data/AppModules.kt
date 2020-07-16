package de.hamurasa.data

import de.hamurasa.model.lesson.*
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.dictionary.DictionaryFragment
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.SchedulerProviderImpl
import de.hamurasa.session.SessionViewModel
import de.hamurasa.model.vocable.*
import de.hamurasa.main.fragments.dictionary.DictionaryViewModel
import de.hamurasa.main.fragments.edit.EditFragment
import de.hamurasa.main.fragments.edit.EditVocableDialog
import de.hamurasa.main.fragments.edit.NewVocableDialog
import de.hamurasa.main.fragments.edit.EditViewModel
import de.hamurasa.main.fragments.home.HomeFragment
import de.hamurasa.main.fragments.home.HomeViewModel
import de.hamurasa.model.profile.Profile
import de.hamurasa.model.user.User
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

    viewModel { SessionViewModel(get(), get(), get(), get()) }

    viewModel { SettingsViewModel(get()) }

    viewModel { EditViewModel(get(), get(), get(), get()) }

    viewModel { HomeViewModel(get(), get(), get(), get()) }

    viewModel { DictionaryViewModel(get(), get(), get(), get()) }
}


val modelModules = module {
    factory { Vocable() }

    factory { Lesson() }

    factory { Profile() }

    factory { User() }

}