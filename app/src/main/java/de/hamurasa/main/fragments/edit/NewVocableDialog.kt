package de.hamurasa.main.fragments.edit

import de.hamurasa.model.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.dialogs.AbstractNewVocableDialog
import de.hamurasa.main.fragments.dictionary.DictionaryViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewVocableDialog(private val vocable: Vocable) : AbstractNewVocableDialog(vocable) {

    override val myViewModel: DictionaryViewModel by sharedViewModel()

    override fun onAddVocable() {
        val activeLesson = MainContext.EditContext.lesson.blockingFirst()

        myViewModel.addVocableToLesson(vocable, activeLesson.id)
    }

}

