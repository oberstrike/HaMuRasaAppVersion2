package de.hamurasa.main

import androidx.fragment.app.Fragment
import de.hamurasa.lesson.model.Lesson
import io.reactivex.Observable

object MainContext {
    lateinit var isLoggedIn: Observable<Boolean>
    
    lateinit var activeFragment: Fragment

    lateinit var lessons: Observable<List<Lesson>>

}
