package de.hamurasa.main.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.DictionaryFragment
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewVocableDialog(private val vocable: Vocable) : AbstractDialog<Vocable>(vocable) {

    private lateinit var valueEditText: EditText
    private lateinit var translationsEditText: EditText
    private lateinit var addVocableButton: Button
    private lateinit var newVocableTypeSpinner: Spinner
    private lateinit var newVocableLanguageSpinner: Spinner
    private lateinit var newVocableOfflineCheckBox: CheckBox

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_new_vocable

    override fun createView(view: View) {
        vocable.id = 0

        valueEditText = view.findViewById(R.id.vocable_value_editText)
        valueEditText.afterTextChanged {
            if (it.isNotEmpty()) vocable.value = it
        }

        translationsEditText = view.findViewById(R.id.vocable_translations_editText)
        translationsEditText.afterTextChanged {
            if (it.isNotEmpty()) vocable.translation = it.split(",")
        }

        addVocableButton = view.findViewById(R.id.add_vocable_button)
        addVocableButton.setOnClickListener(this)

        newVocableTypeSpinner = view.findViewById(R.id.new_vocable_type_spinner)
        newVocableTypeSpinner.afterSelectedChanged {
            val type = VocableType.valueOf(it)
            vocable.type = type
        }

        newVocableLanguageSpinner = view.findViewById(R.id.new_vocable_language_spinner)
        newVocableLanguageSpinner.afterSelectedChanged {
            val language = Language.valueOf(it)
            vocable.language = language
        }


        newVocableOfflineCheckBox = view.findViewById(R.id.new_vocable_offline_checkbox)
        newVocableOfflineCheckBox.setOnCheckedChangeListener { _, checked ->
            vocable.isOffline = checked
        }


        activity!!.createSpinner<VocableType>(newVocableTypeSpinner)
        activity!!.createSpinner<Language>(newVocableLanguageSpinner)


    }

    override fun onClick(v: View?) {
        if (!vocable.isValid()) {
            activity?.toast("Please fill in all the information.")
            return
        }

        if (MainContext.activeFragment is DictionaryFragment) {
            myViewModel.addVocableToServer(vocable)
        } else {
            val activeLesson = MainContext.EditContext.lesson.blockingFirst()
            myViewModel.addVocableToLesson(vocable, activeLesson.serverId)
        }

        dismiss()
    }

}

