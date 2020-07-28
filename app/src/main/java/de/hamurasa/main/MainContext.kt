package de.hamurasa.main

import de.hamurasa.data.profile.Profile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MainContext {

    const val name: String = "Spanisch"


    @ExperimentalCoroutinesApi
    object HomeContext {
        private val profileMutableStateFlow: MutableStateFlow<Profile?> =
            MutableStateFlow(null)

        var profile: StateFlow<Profile?> = profileMutableStateFlow

        fun setProfile(profile: Profile) {
            profileMutableStateFlow.value = profile
        }
    }

    @ExperimentalCoroutinesApi
    object EditContext {
        private var lessonMutableStateFlow: MutableStateFlow<de.hamurasa.data.lesson.Lesson?> = MutableStateFlow(null)

        val lesson: StateFlow<de.hamurasa.data.lesson.Lesson?> = lessonMutableStateFlow

        fun setLesson(newLesson: de.hamurasa.data.lesson.Lesson?) {
            if (newLesson == lessonMutableStateFlow.value)
                lessonMutableStateFlow.value = null
            lessonMutableStateFlow.value = newLesson
        }
    }

    @ExperimentalCoroutinesApi
    object DictionaryContext {
        private val wordMutableStateFlow: MutableStateFlow<List<de.hamurasa.data.vocable.Vocable>> =
            MutableStateFlow(listOf())

        val words: StateFlow<List<de.hamurasa.data.vocable.Vocable>> = wordMutableStateFlow

        fun setWords(newWords: List<de.hamurasa.data.vocable.Vocable>) {
            wordMutableStateFlow.value = newWords
        }
    }
}


