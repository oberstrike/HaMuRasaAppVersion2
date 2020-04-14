package de.hamurasa.main.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.DictionaryFragment
import de.hamurasa.util.withDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class NewVocableDialog : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var valueEditText: EditText
    private lateinit var translationsEditText: EditText
    private lateinit var addVocableButton: Button
    private lateinit var translationListView: ListView

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        withDialog(activity!!, R.layout.dialog_new_vocable) { view ->
            valueEditText = view.findViewById(R.id.vocable_value_editText)
            translationsEditText = view.findViewById(R.id.vocable_translations_editText)
            addVocableButton = view.findViewById(R.id.add_vocable_button)
            translationListView = view.findViewById(R.id.translation_listView)

            addVocableButton.setOnClickListener {
                val value = valueEditText.text.toString()
                val translations = translationsEditText.text.toString()
                if (value.isEmpty() || translations.isEmpty()) {
                    val toast = Toast.makeText(
                        activity!!,
                        "Please complete all available fields.",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return@setOnClickListener
                }

                val translationArray = translations.split(",")

                val vocableDTO = VocableDTO.create(
                    0, value,
                    VocableType.VERB,
                    translationArray
                )

                if (MainContext.activeFragment is DictionaryFragment) {
                    myViewModel.addVocableToServer(vocableDTO)
                } else {
                    val activeLesson = MainContext.EditContext.lesson.blockingFirst()
                    myViewModel.addVocableToLesson(vocableDTO, activeLesson.id)
                }

                dismiss()
            }

        }.create()


}