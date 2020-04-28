package de.hamurasa.main

import androidx.fragment.app.Fragment
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.vocable.Vocable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

object MainContext {
    lateinit var isLoggedIn: Observable<Boolean>

    lateinit var activeFragment: Fragment

    object HomeContext {
        lateinit var lessons: Observable<List<Lesson>>

        var updateLessons: PublishSubject<Map<Lesson, Lesson>> = PublishSubject.create()
    }

    object EditContext {
        lateinit var lesson: BehaviorSubject<Lesson>

    }

    object DictionaryContext {
        lateinit var words: BehaviorSubject<List<Vocable>>
    }


}

