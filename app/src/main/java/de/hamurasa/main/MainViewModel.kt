package de.hamurasa.main


import android.content.Context

import de.hamurasa.data.lesson.LessonService
import de.hamurasa.data.profile.Profile
import de.hamurasa.data.profile.ProfileService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainViewModel(
    val context: Context,
    private val provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val profileService: ProfileService
) : BaseViewModel(provider) {


    @ExperimentalCoroutinesApi
    suspend fun init() {
        val profiles = profileService.findAll()
        if (profiles.isEmpty()) {
            val profile = Profile()
            profile.name = MainContext.name
            profile.lessons.addAll(lessonService.findAll())
            profileService.save(profile)
        }

        MainContext.HomeContext.change(profileService.findByName(MainContext.name)!!)
    }


    suspend fun export(): String {
        val allLessons = lessonService.findAll()
        return de.hamurasa.data.util.GsonObject.gson.toJson(allLessons)
    }

    suspend fun save(profile: Profile) {
        profileService.save(profile)
    }
}
