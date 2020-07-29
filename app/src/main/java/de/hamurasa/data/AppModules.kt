package de.hamurasa.data

import de.hamurasa.data.lesson.*
import de.hamurasa.data.profile.*
import de.hamurasa.data.util.ObjectBox
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
import io.objectbox.Box
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {

    //Overview
    single { Settings(get()) }

    single { ObjectBox(get()) }


    //Repositories

    single { SchedulerProviderImpl() } bind SchedulerProvider::class

    single { VocableRepositoryImpl(get<ObjectBox>().getBox(Vocable::class.java), get<ObjectBox>().getBox(VocableStats::class.java)) } bind VocableRepository::class

    single { LessonRepositoryImpl(get<ObjectBox>().getBox(Lesson::class.java) ) } bind LessonRepository::class

    single { ProfileRepositoryImpl(get<ObjectBox>().getBox(Profile::class.java) ) } bind ProfileRepository::class

    single { VocableStatsRepositoryImpl(get<ObjectBox>().getBox(VocableStats::class.java)) } bind VocableStatsRepository::class

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


