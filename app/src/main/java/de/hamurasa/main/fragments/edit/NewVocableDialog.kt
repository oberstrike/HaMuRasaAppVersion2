package de.hamurasa.main.fragments.edit

import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.dialogs.AbstractNewVocableDialog
import de.hamurasa.main.fragments.dictionary.DictionaryViewModel
import de.hamurasa.model.vocable.Vocable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewVocableDialog(
    private val vocable: Vocable
) :
    AbstractNewVocableDialog(vocable) {

    override val myViewModel: DictionaryViewModel by sharedViewModel()

    @ExperimentalCoroutinesApi
    override fun onAddVocable() {
        val lesson = MainContext.EditContext.lesson.value ?: return
        myViewModel.addVocableToLesson(vocable, lesson.id)
    }


}


