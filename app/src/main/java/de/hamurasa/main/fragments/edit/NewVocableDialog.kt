package de.hamurasa.main.fragments.edit

import de.hamurasa.data.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.dialogs.BaseNewVocableDialog
import de.hamurasa.main.fragments.dictionary.DictionaryViewModel
import de.hamurasa.data.vocableStats.VocableStats
import de.hamurasa.main.fragments.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewVocableDialog(
    private val vocable: Vocable,
    private val vocableStats: VocableStats
) :
    BaseNewVocableDialog(vocable) {

    override val myViewModel: DictionaryViewModel by sharedViewModel()

    @ExperimentalCoroutinesApi
    override fun onAddVocable() {
        val lesson = MainContext.EditContext.value() ?: return

        myViewModel.launchJob {
            val newVocable = myViewModel.saveVocable(vocable)
            vocableStats.vocable.target = newVocable
            myViewModel.saveVocableStats(vocableStats)
            myViewModel.addVocableToLesson(newVocable, lesson.id)
        }
    }


}


