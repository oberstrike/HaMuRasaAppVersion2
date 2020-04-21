package de.hamurasa.main.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.createSpinner
import de.hamurasa.util.withDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class EditVocableDialog(val vocable: Vocable) : AppCompatDialogFragment(), View.OnClickListener {

    private lateinit var valueEditText: EditText

    private lateinit var translationEditText: EditText

    private lateinit var isOfflineCheckBox: CheckBox

    private lateinit var applyButton: Button

    private lateinit var deleteButton: Button

    private lateinit var typeSpinner: Spinner

    private val myViewModel: MainViewModel by sharedViewModel()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        withDialog(activity!!, R.layout.dialog_edit_vocable) { view ->
            valueEditText = view.findViewById(R.id.valueEditText)
            translationEditText = view.findViewById(R.id.translationEditText)
            isOfflineCheckBox = view.findViewById(R.id.editVocableOfflineCheckBox)
            applyButton = view.findViewById(R.id.applyButton)
            typeSpinner = view.findViewById(R.id.editVocableTypeSpinner)

            deleteButton = view.findViewById(R.id.deleteButton)

            applyButton.setOnClickListener(this)
            deleteButton.setOnClickListener {
                delete()
            }
            init()
        }.create()


    private fun init() {
        valueEditText.setText(vocable.value)

        if (vocable.translation.isNotEmpty()) {
            val translations = vocable.translation.reduce { acc, s ->
                "$acc,$s"
            }

            translationEditText.setText(translations)
        }
        createSpinner<VocableType>(activity!!, typeSpinner)


        val vocableType = vocable.type.toString()
        val position = VocableType.values().map { it.name }.indexOf(vocableType)
        typeSpinner.setSelection(position)

        isOfflineCheckBox.isChecked = vocable.isOffline

    }

    override fun onClick(v: View?) {
        val newValue = valueEditText.text.toString()
        val newTranslations = translationEditText.text.toString().split(",")
        val newVocableType = VocableType.valueOf(typeSpinner.selectedItem.toString())

        val isOffline = isOfflineCheckBox.isChecked
        val id = if (!isOffline) vocable.serverId else vocable.id

        val vocableDTO =
            VocableDTO.create(id, newValue, newVocableType, newTranslations, vocable.language)

        myViewModel.patchVocable(vocableDTO, isOffline)

        dismiss()
    }

    private fun delete() {
        val isOffline = isOfflineCheckBox.isChecked
        val id = if (!isOffline) vocable.serverId else vocable.id

        val vocableDTO = VocableDTO.create(
            id,
            vocable.value,
            vocable.type,
            vocable.translation,
            vocable.language
        )
        myViewModel.deleteVocableFromLesson(vocableDTO, isOffline)
        dismiss()
    }
}