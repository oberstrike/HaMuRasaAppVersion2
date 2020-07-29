package de.hamurasa.main.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.data.vocable.Language
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.util.BaseDialog
import de.hamurasa.util.isValid
import de.hamurasa.util.toast
import de.hamurasa.util.widgets.bind
import de.hamurasa.util.widgets.initAdapter
import kotlinx.android.synthetic.main.dialog_new_lesson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewLessonDialog(
    private val lesson: de.hamurasa.data.lesson.Lesson,
    private val homeFragment: HomeFragment
) :
    BaseDialog(), View.OnClickListener {
    private val myViewModel: HomeViewModel by sharedViewModel()

    @ExperimentalCoroutinesApi
    override fun onClick(v: View?) {
        if (!lesson.isValid()) {
            requireActivity().toast("The language have to be different")
            return
        }

        myViewModel.launchJob {
            myViewModel.saveLesson(lesson)
            withContext(Dispatchers.Main) {
                val text = "The creation was successful!"
                val toast = Toast.makeText(activity, text, Toast.LENGTH_LONG)
                toast.show()
                dismiss()
            }
        }

    }

    override fun getLayoutId(): Int = R.layout.dialog_new_lesson

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_lesson_okButton.setOnClickListener(this)
        new_lesson_cancelButton.setOnClickListener {
            dismiss()
        }

        //Language Binding
        new_lesson_language_spinner.initAdapter<de.hamurasa.data.vocable.Language>()
        new_lesson_language_spinner.bind(lesson::language) {
            de.hamurasa.data.vocable.Language.valueOf(it)
        }

        //Validation Language Binding
        new_lesson_validation_language_spinner.initAdapter<de.hamurasa.data.vocable.Language>()
        new_lesson_validation_language_spinner.bind(lesson::validationLanguage) {
            de.hamurasa.data.vocable.Language.valueOf(it)
        }
    }
}