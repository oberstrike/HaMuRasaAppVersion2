package de.hamurasa.main

import androidx.fragment.app.Fragment
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.vocable.Vocable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object MainContext {
    lateinit var isLoggedIn: Observable<Boolean>

    lateinit var activeFragment: Fragment


    object HomeContext {
        lateinit var lessons: Observable<List<Lesson>>
    }

    object EditContext {
        lateinit var lesson: BehaviorSubject<Lesson>

    }


}

