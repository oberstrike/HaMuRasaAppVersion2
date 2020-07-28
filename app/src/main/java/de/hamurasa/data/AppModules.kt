package de.hamurasa.data

import de.hamurasa.data.lesson.*
import de.hamurasa.data.profile.*
import de.hamurasa.data.vocable.*
import de.hamurasa.data.vocableStats.*
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.SchedulerProviderImpl
import de.hamurasa.session.SessionViewModel
import de.hamurasa.main.fragments.dictionary.DictionaryViewModel
import de.hamurasa.main.fragments.edit.EditViewModel
import de.hamurasa.main.fragments.home.HomeViewModel
import de.hamurasa.main.mainModule
import de.hamurasa.session.sessionModules
import de.hamurasa.settings.SettingsViewModel
import de.hamurasa.settings.model.Settings
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {

    //Overview

    single { de.hamurasa.data.util.ObjectBox.init(get()) }

    single { Settings(get()) }

    //Repositories

    single { SchedulerProviderImpl() } bind SchedulerProvider::class

    single { VocableRepositoryImpl() } bind VocableRepository::class

    single { LessonRepositoryImpl() } bind LessonRepository::class

    single { ProfileRepositoryImpl() } bind ProfileRepository::class

    single { VocableStatsRepositoryImpl() } bind VocableStatsRepository::class

    //Services

    single { LessonServiceImpl(get()) } bind LessonService::class

    single { VocableServiceImpl(get()) } bind VocableService::class

    single { VocableStatsServiceImpl(get()) } bind VocableStatsService::class

    single { ProfileServiceImpl(get()) } bind ProfileService::class

    //Viewmodels

    viewModel { MainViewModel(get(), get(), get(), get()) }

    viewModel { SessionViewModel(get(), get(), get(), get()) }

    viewModel { SettingsViewModel(get()) }

    viewModel { EditViewModel(get(), get(), get(), get(), get()) }

    viewModel { HomeViewModel(get(), get(), get(), get()) }

    viewModel { DictionaryViewModel(get(), get(), get(), get(), get()) }

    factory { Vocable() }

    factory { Lesson() }

    factory { Profile() }

    factory { VocableStats() }


}.plus(sessionModules).plus(mainModule)


