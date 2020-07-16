package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.hamurasa.model.vocable.Vocable
import de.util.hamurasa.utility.util.AbstractFragment
import de.util.hamurasa.utility.util.AbstractSelfCleanupFragment
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class EditFragment : AbstractSelfCleanupFragment(), VocableRecyclerViewAdapter.OnClickListener {

    override val myViewModel: EditViewModel by sharedViewModel()

    private lateinit var vocableEditRecyclerViewAdapter: VocableRecyclerViewAdapter

    override fun getLayoutId(): Int = R.layout.edit_fragment

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vocableEditRecyclerViewAdapter = VocableRecyclerViewAdapter(requireContext(), this)
        edit_fragment_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        edit_fragment_recyclerView.adapter = vocableEditRecyclerViewAdapter
        listIsEmpty.visibility = View.VISIBLE


        myViewModel.launchJob {
            initObserver()
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.EditContext.lesson.collect {
            if (it == null)
                return@collect
            val lessons = it.words.toList().sortedBy { vocable -> vocable.value }
            withContext(Dispatchers.Main) {
                vocableEditRecyclerViewAdapter.setWords(lessons)
                vocableEditRecyclerViewAdapter.notifyDataSetChanged()
                if (listIsEmpty != null) {
                    if (lessons.isEmpty()) {
                        listIsEmpty.visibility = View.VISIBLE
                    } else {
                        listIsEmpty.visibility = View.INVISIBLE
                    }

                }
            }
        }

        println("finish")
    }


    @ExperimentalCoroutinesApi
    override fun onItemClick(position: Int) {
        val vocable: Vocable? =
            MainContext.EditContext.lesson.value?.words?.sortedBy { vocable -> vocable.value }
                ?.get(position)

        val fragment by inject<EditVocableDialog> { parametersOf(vocable, this) }
        fragment.show(parentFragmentManager, "")
    }


}