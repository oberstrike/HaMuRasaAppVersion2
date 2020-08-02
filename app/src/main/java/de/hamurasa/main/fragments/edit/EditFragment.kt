package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mitteloupe.solid.recyclerview.SolidAdapter
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.*
import de.hamurasa.util.AbstractSelfCleaningFragment
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class EditFragment : AbstractSelfCleaningFragment(), VocableOnClickListener {

    override val myViewModel: EditViewModel by sharedViewModel()

    private lateinit var vocableEditRecyclerViewAdapter: SolidAdapter<SolidVocableViewHolder, de.hamurasa.data.vocable.Vocable>

    override val layoutId: Int = R.layout.edit_fragment

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vocableEditRecyclerViewAdapter = SolidAdapter(
            viewProvider = SolidVocableViewProvider(layoutInflater),
            viewBinder = SolidVocableViewBinder(this),
            viewHoldersProvider = { v, _ -> SolidVocableViewHolder(v) }
        )

        edit_fragment_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        edit_fragment_recyclerView.adapter = vocableEditRecyclerViewAdapter



        myViewModel.launchJob {
            initObserver()
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.EditContext.flow.collect {
            val lesson = it.value
            if (lesson == null) {
                withContext(Dispatchers.Main) {
                    listIsEmpty.visibility = View.VISIBLE
                }
                return@collect
            }
            updateLesson(lesson)
        }
    }

    suspend fun updateLesson(it: de.hamurasa.data.lesson.Lesson) {
        val words = it.words.toList().sortedBy { vocable -> vocable.value }
        withContext(Dispatchers.Main) {
            vocableEditRecyclerViewAdapter.setItems(words)
            vocableEditRecyclerViewAdapter.notifyDataSetChanged()
            listIsEmpty.visibility = if (words.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }


    @ExperimentalCoroutinesApi
    override fun onItemClick(vocable: de.hamurasa.data.vocable.Vocable) {
        val fragment by inject<EditVocableDialog> { parametersOf(vocable, this) }
        fragment.show(parentFragmentManager, "")
    }


}