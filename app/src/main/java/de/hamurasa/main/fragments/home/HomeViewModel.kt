package de.hamurasa.main.fragments.home

import android.content.Context
import de.hamurasa.main.MainContext
import de.hamurasa.data.lesson.LessonService
import de.hamurasa.data.profile.Profile
import de.hamurasa.data.profile.ProfileService
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import kotlinx.coroutines.*
import org.joda.time.DateTime

class HomeViewModel(
    val context: Context,
    provider: SchedulerProvider,
    private val lessonService: LessonService,
    private val profileService: ProfileService
) : BaseViewModel(provider) {

    lateinit var profile: Profile

    @ExperimentalCoroutinesApi
    suspend fun updateHome() {
        val profile = profileService.findByName(MainContext.name)!!
        MainContext.HomeContext.setProfile(profile)
    }


    @ExperimentalCoroutinesApi
    suspend fun deleteLesson(lesson: de.hamurasa.data.lesson.Lesson) {
        lessonService.delete(lesson)
        val profile = MainContext.HomeContext.profile.value!!
        profile.lessons.remove(lesson)
        MainContext.HomeContext.setProfile(profile)
    }


    @ExperimentalCoroutinesApi
    suspend fun saveLesson(lesson: de.hamurasa.data.lesson.Lesson) {
        lessonService.save(lesson)
        val profile = MainContext.HomeContext.profile.value ?: return

        profile.lessons.add(lesson)
        profile.lastTimeChanged = DateTime.now()
        profileService.save(profile)
        MainContext.HomeContext.setProfile(profile)
    }

    suspend fun findProfileByName(it: String): Profile? {
        return profileService.findByName(it)
    }

    fun getAllProfiles(): List<Profile> = runBlocking {
        profileService.findAll()
    }

}