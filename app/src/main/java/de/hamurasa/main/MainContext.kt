package de.hamurasa.main

import androidx.fragment.app.Fragment
import de.hamurasa.main.fragments.home.HomeFragment
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.model.vocable.Vocable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object MainContext {
    var isLoggedIn: Boolean = false

    lateinit var activeFragment: Fragment

    object HomeContext {
        lateinit var lessons: Observable<List<Lesson>>
    }

    object EditContext {
        lateinit var lesson: BehaviorSubject<Lesson>

    }

    object DictionaryContext {
        lateinit var words: Observable<List<Vocable>>
    }
}


