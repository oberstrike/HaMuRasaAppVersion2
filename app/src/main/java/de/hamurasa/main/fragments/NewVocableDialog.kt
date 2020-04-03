package de.hamurasa.main.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.VocableDTO
import de.hamurasa.main.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class NewVocableDialog : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var valueEditText: EditText
    private lateinit var translationsEditText: EditText
    private lateinit var addVocableButton: Button

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater

        val view = inflater.inflate(R.layout.dialog_new_vocable, null)

        valueEditText = view.findViewById(R.id.vocable_value_editText)
        translationsEditText = view.findViewById(R.id.vocable_translations_editText)
        addVocableButton = view.findViewById(R.id.add_vocable_button)



        with(builder) {
            setView(view)
        }

        val alertDialog = builder.create()


        addVocableButton.setOnClickListener {
            val value = valueEditText.text.toString()
            val translations = translationsEditText.text.toString()
            if(value.isEmpty() || translations.isEmpty()){
                return@setOnClickListener
            }

            val translationArray = translations.split(",")
            myViewModel.addVocable(VocableDTO.create(0, value,"Verb", translationArray))
            alertDialog.dismiss()
        }

        return alertDialog
    }
}