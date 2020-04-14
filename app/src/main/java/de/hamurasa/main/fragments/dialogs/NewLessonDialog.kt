package de.hamurasa.main.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.lesson.LessonDTO
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.withDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class NewLessonDialog(lesson: Lesson) : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var newLanguageSpinner: Spinner

    private lateinit var validationSpinner: Spinner

    private lateinit var addButton: Button

    private lateinit var cancelButton: Button


    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onClick(v: View?) {
        val language = Language.fromLetterCode(newLanguageSpinner.selectedItem.toString())!!
        val validationLanguage =
            Language.fromLetterCode(validationSpinner.selectedItem.toString())!!
        if (language == validationLanguage) {
            val text = "Please select two different languages!"
            val toast = Toast.makeText(activity, text, Toast.LENGTH_LONG)
            toast.show()
            return
        }


        val lessonDTO = LessonDTO(
            0,
            listOf(),
            language,
            validationLanguage
        )
        myViewModel.addLesson(lessonDTO)
        val text = "The creation was successful!"
        val toast = Toast.makeText(activity, text, Toast.LENGTH_LONG)
        toast.show()
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        withDialog(activity!!, R.layout.dialog_new_lesson) { view ->
            newLanguageSpinner = view.findViewById(R.id.new_language_spinner)
            validationSpinner = view.findViewById(R.id.new_validation_language_spinner)
            addButton = view.findViewById(R.id.okButton)
            cancelButton = view.findViewById(R.id.cancelButton)

            addButton.setOnClickListener(this)
            cancelButton.setOnClickListener {
                dismiss()
            }

            val array = Language.values().map { it.letterCode }

            val arrayAdapter =
                ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, array)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            newLanguageSpinner.adapter = arrayAdapter
            validationSpinner.adapter = arrayAdapter
        }.create()

}