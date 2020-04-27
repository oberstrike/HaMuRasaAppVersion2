package de.hamurasa.main.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.lesson.model.vocable.VocableDTO
import de.hamurasa.lesson.model.vocable.VocableType
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.isValid
import de.util.hamurasa.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


//Reworked
class EditVocableDialog(val vocable: Vocable) : AbstractDialog<Vocable>(vocable) {

    private lateinit var valueEditText: EditText

    private lateinit var translationEditText: EditText

    private lateinit var isOfflineCheckBox: CheckBox

    private lateinit var applyButton: Button

    private lateinit var deleteButton: Button

    private lateinit var typeSpinner: Spinner

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId(): Int = R.layout.dialog_edit_vocable

    override fun createView(view: View) {
        valueEditText = view.findViewById(R.id.valueEditText)
        valueEditText.setText(vocable.value)
        valueEditText.afterTextChanged {
            if (it.isNotEmpty()) vocable.value = it
        }

        translationEditText = view.findViewById(R.id.translationEditText)
        translationEditText.setText(vocable.translation.reduce { acc, s -> "$acc,$s" })
        translationEditText.afterTextChanged {
            if (it.isNotBlank()) vocable.translation = it.split(",")
        }

        isOfflineCheckBox = view.findViewById(R.id.editVocableOfflineCheckBox)
        isOfflineCheckBox.isChecked = vocable.isOffline
        isOfflineCheckBox.setOnCheckedChangeListener { _, checked -> vocable.isOffline = checked }

        applyButton = view.findViewById(R.id.applyButton)
        applyButton.setOnClickListener(this)

        typeSpinner = view.findViewById(R.id.editVocableTypeSpinner)
        activity!!.createSpinner<VocableType>(typeSpinner)
        typeSpinner.afterSelectedChanged { vocable.type = VocableType.valueOf(it) }
        val position = VocableType.values().map { it.name }.indexOf(vocable.type.toString())
        typeSpinner.setSelection(position)

        deleteButton = view.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            delete()
        }
    }

    override fun onClick(v: View?) {
        if (!vocable.isValid()) {
            activity!!.toast("Please fill all required forms")
            return
        }

        myViewModel.patchVocable(vocable)

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