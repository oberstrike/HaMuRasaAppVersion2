package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import com.mitteloupe.solid.fragment.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.main.fragments.adapters.VocableOnClickListener
import de.hamurasa.util.AbstractSelfCleaningFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class EditFragment : AbstractSelfCleaningFragment(), VocableOnClickListener {

    override val myViewModel: EditViewModel by sharedViewModel()

    override val layoutId: Int = R.layout.edit_fragment

    override val lifecycleHandlers: List<LifecycleHandler> = listOf(
        EditFragmentLifeCycleHandler(
            myViewModel = get(),
            fragment = this
        )
    )

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @ExperimentalCoroutinesApi
    override fun onItemClick(vocable: Vocable) {
        val fragment by inject<EditVocableDialog> { parametersOf(vocable, this) }
        fragment.show(parentFragmentManager, "")
    }


}