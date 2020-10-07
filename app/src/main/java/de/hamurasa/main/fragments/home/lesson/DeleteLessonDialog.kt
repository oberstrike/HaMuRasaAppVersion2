package de.hamurasa.main.fragments.home.lesson

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.main.fragments.home.HomeViewModel
import de.hamurasa.util.BaseDialog
import kotlinx.android.synthetic.main.dialog_delete_lesson.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DeleteLessonDialog(private val lesson: Lesson) : BaseDialog(), View.OnClickListener {
    private val myViewModel: HomeViewModel by sharedViewModel()

    override fun getLayoutId() = R.layout.dialog_delete_lesson

    @ExperimentalCoroutinesApi
    override fun onClick(v: View) {
        when (v) {
            delete_lesson_button -> runBlocking {
                myViewModel.deleteLesson(lesson)
                dismiss()
            }
            cancel_lesson_button -> dismiss()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delete_lesson_button.setOnClickListener(this)
        cancel_lesson_button.setOnClickListener(this)
    }

}