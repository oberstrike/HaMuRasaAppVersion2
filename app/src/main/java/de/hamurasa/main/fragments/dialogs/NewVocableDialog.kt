package de.hamurasa.main.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.DictionaryFragment
import de.hamurasa.util.createSpinner
import de.hamurasa.util.withDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class NewVocableDialog : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var valueEditText: EditText
    private lateinit var translationsEditText: EditText
    private lateinit var addVocableButton: Button
    private lateinit var newVocableTypeSpinner: Spinner
    private lateinit var newVocableLanguageSpinner: Spinner

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onClick(v: View?) {
        val value = valueEditText.text.toString()
        val translations = translationsEditText.text.toString()
        if (value.isEmpty() || translations.isEmpty()) {
            val toast = Toast.makeText(
                activity!!,
                "Please complete all available fields.",
                Toast.LENGTH_LONG
            )
            toast.show()
            return
        }

        val translationArray = translations.split(",")
        val type = VocableType.valueOf(newVocableTypeSpinner.selectedItem.toString())

        val vocableDTO = VocableDTO.create(
            0,
            value,
            type,
            translationArray
        )




        if (MainContext.activeFragment is DictionaryFragment) {
            myViewModel.addVocableToServer(vocableDTO)
        } else {
            val activeLesson = MainContext.EditContext.lesson.blockingFirst()
            myViewModel.addVocableToLesson(vocableDTO, activeLesson.serverId)
        }

        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        withDialog(activity!!, R.layout.dialog_new_vocable) { view ->
            valueEditText = view.findViewById(R.id.vocable_value_editText)
            translationsEditText = view.findViewById(R.id.vocable_translations_editText)
            addVocableButton = view.findViewById(R.id.add_vocable_button)
            addVocableButton.setOnClickListener(this)
            newVocableTypeSpinner = view.findViewById(R.id.new_vocable_type_spinner)
            newVocableLanguageSpinner = view.findViewById(R.id.new_vocable_language_spinner)


            createSpinner<VocableType>(activity!!, newVocableTypeSpinner)
            createSpinner<Language>(activity!!, newVocableLanguageSpinner)

        }.create()




}

