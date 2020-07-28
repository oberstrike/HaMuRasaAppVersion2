package de.hamurasa.main.fragments.edit

import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.dialogs.BaseNewVocableDialog
import de.hamurasa.main.fragments.dictionary.DictionaryViewModel
import de.hamurasa.data.vocableStats.VocableStats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel

//Reworked
class NewVocableDialog(
    private val vocable: de.hamurasa.data.vocable.Vocable,
    private val vocableStats: VocableStats
) :
    BaseNewVocableDialog(vocable) {

    override val myViewModel: DictionaryViewModel by sharedViewModel()

    @ExperimentalCoroutinesApi
    override fun onAddVocable() {
        val lesson = MainContext.EditContext.lesson.value ?: return

        myViewModel.launchJob {
            vocableStats.vocable.target = vocable
            myViewModel.saveVocableStats(vocableStats)
            myViewModel.addVocableToLesson(vocable, lesson.id)
        }
    }


}


