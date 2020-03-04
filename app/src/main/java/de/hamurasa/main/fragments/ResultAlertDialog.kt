package de.hamurasa.main.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ResultAlertDialog(val word: Vocable) : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var translationTextView: TextView

    private lateinit var resultLessonSpinner: Spinner

    private lateinit var addToLessonButton: Button

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater

        val view = inflater.inflate(R.layout.dialog_result, null)

        addToLessonButton = view.findViewById(R.id.add_to_lesson_button)
        addToLessonButton.setOnClickListener(this)

        translationTextView = view.findViewById( R.id.translation_text_view)
        translationTextView.text = word.translation.distinct().toString()

        resultLessonSpinner = view.findViewById(R.id.result_lesson_Spinner)

        val array = MainContext.lessons.blockingFirst().map { "Lesson ${it.id}" }

        val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, array)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resultLessonSpinner.adapter = arrayAdapter

        with(builder){
            setView(view)
        }

        return builder.create()
    }

    override fun onClick(v: View?) {
        val x = myViewModel != null

        println("HALLO")
    }


}