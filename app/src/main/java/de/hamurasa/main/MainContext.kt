package de.hamurasa.main

import androidx.fragment.app.Fragment
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.model.vocable.Vocable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MainContext {

    lateinit var activeFragment: Fragment

    @ExperimentalCoroutinesApi
    object HomeContext {
        private val lessonsMutableStateFlow: MutableStateFlow<List<Lesson>?> =
            MutableStateFlow(null)

        var lessons: StateFlow<List<Lesson>?> = lessonsMutableStateFlow

        fun setLessons(lessons: List<Lesson>) {
            lessonsMutableStateFlow.value = null
            lessonsMutableStateFlow.value = lessons
        }


    }

    @ExperimentalCoroutinesApi
    object EditContext {

        private var lessonMutableStateFlow: MutableStateFlow<Lesson?> = MutableStateFlow(null)

        val lesson: StateFlow<Lesson?> = lessonMutableStateFlow

        fun setLesson(newLesson: Lesson?) {
            if (newLesson == lessonMutableStateFlow.value)
                lessonMutableStateFlow.value = null
            lessonMutableStateFlow.value = newLesson
        }


    }
    @ExperimentalCoroutinesApi
    object DictionaryContext {
        private val wordMutableStateFlow: MutableStateFlow<List<Vocable>> = MutableStateFlow(listOf())

        val words: StateFlow<List<Vocable>> = wordMutableStateFlow

        fun setWords(newWords: List<Vocable>){
            wordMutableStateFlow.value = newWords
        }
    }
}


