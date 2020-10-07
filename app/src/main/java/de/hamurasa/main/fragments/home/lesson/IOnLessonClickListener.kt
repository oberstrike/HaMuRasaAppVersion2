package de.hamurasa.main.fragments.home.lesson

import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.edit.EditFragment
import de.hamurasa.main.fragments.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

interface IOnLessonClickListener {
    fun onLessonClick(lesson: Lesson)
}

class OnLessonClickListenerImpl(
    private val myViewModel: HomeViewModel
) : IOnLessonClickListener {

    @ExperimentalCoroutinesApi
    override fun onLessonClick(lesson: Lesson) {
        myViewModel.launchJob {
            GlobalScope.launch(Dispatchers.IO) {
                MainContext.EditContext.change(lesson)
            }
        }

    }
}
