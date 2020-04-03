package de.hamurasa.main.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.Language
import de.hamurasa.lesson.model.Lesson
import de.hamurasa.lesson.model.LessonDTO
import de.hamurasa.main.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class NewLessonDialog(lesson: Lesson) : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var newLanguageSpinner: Spinner

    private lateinit var validationSpinner: Spinner

    private lateinit var addButton: Button

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onClick(v: View?) {
        val language = Language.fromLetterCode( newLanguageSpinner.selectedItem.toString() )!!
        val validationLanguage = Language.fromLetterCode(validationSpinner.selectedItem.toString() )!!
        val lessonDTO = LessonDTO(0, listOf(), language, validationLanguage)
        myViewModel.addLesson(lessonDTO)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater

        val view = inflater.inflate(R.layout.dialog_new_lesson, null)



        newLanguageSpinner = view.findViewById(R.id.new_language_spinner)
        validationSpinner = view.findViewById(R.id.new_validation_language_spinner)
        addButton = view.findViewById(R.id.add_button)

        addButton.setOnClickListener(this)

        val array = Language.values().map { it.letterCode }

        val arrayAdapter =
            ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, array)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        newLanguageSpinner.adapter = arrayAdapter
        validationSpinner.adapter = arrayAdapter


        with(builder) {
            setView(view)
        }

        //TODO

        return builder.create()
    }
}