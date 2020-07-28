package de.hamurasa.main.fragments.home

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.util.BaseDialog
import kotlinx.android.synthetic.main.dialog_delete_lesson.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DeleteLessonDialog(private val lesson: de.hamurasa.data.lesson.Lesson) : BaseDialog() {
    private val myViewModel: HomeViewModel by sharedViewModel()

    override fun getLayoutId() = R.layout.dialog_delete_lesson

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delete_lesson_button.setOnClickListener {
            runBlocking {
                myViewModel.deleteLesson(lesson)
                dismiss()
            }
        }

        cancel_lesson_button.setOnClickListener {
            dismiss()


        }
    }

}