package de.hamurasa.main.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import de.hamurasa.R
import de.hamurasa.model.vocable.Language
import de.hamurasa.model.lesson.Lesson
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.util.AbstractDialog
import de.util.hamurasa.utility.util.bind
import de.util.hamurasa.utility.util.initAdapter
import de.util.hamurasa.utility.util.toast
import kotlinx.android.synthetic.main.dialog_new_lesson.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewLessonDialog(private val lesson: Lesson) : AbstractDialog(), View.OnClickListener {
    private val myViewModel: HomeViewModel by sharedViewModel()

    @ExperimentalCoroutinesApi
    override fun onClick(v: View?) {
        if (!lesson.isValid()) {
            requireActivity().toast("The language have to be different")
            return
        }

        myViewModel.saveLesson(lesson)
        val text = "The creation was successful!"
        val toast = Toast.makeText(activity, text, Toast.LENGTH_LONG)
        toast.show()
        dismiss()
    }

    override fun getLayoutId(): Int = R.layout.dialog_new_lesson

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_lesson_okButton.setOnClickListener(this)
        new_lesson_cancelButton.setOnClickListener {
            dismiss()
        }

        //Language Binding

        new_lesson_language_spinner.initAdapter<Language>()
        new_lesson_language_spinner.bind(lesson::language) {
            Language.valueOf(it)
        }

        //Validation Language Binding
        new_lesson_validation_language_spinner.initAdapter<Language>()
        new_lesson_validation_language_spinner.bind(lesson::validationLanguage) {
            Language.valueOf(it)
        }
    }
}