package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mitteloupe.solid.fragment.handler.LifecycleHandler
import com.mitteloupe.solid.recyclerview.SolidAdapter
import de.hamurasa.R
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.SolidVocableViewBinder
import de.hamurasa.main.fragments.adapters.SolidVocableViewHolder
import de.hamurasa.main.fragments.adapters.SolidVocableViewProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class EditFragmentLifeCycleHandler(
    private val myViewModel: EditViewModel,
    private val fragment: EditFragment
) : LifecycleHandler {

    private lateinit var vocableEditRecyclerViewAdapter: SolidAdapter<SolidVocableViewHolder, Vocable>

    private lateinit var listIsEmpty: TextView

    private lateinit var editFragmentRecyclerview: RecyclerView

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vocableEditRecyclerViewAdapter = SolidAdapter(
            viewProvider = SolidVocableViewProvider(fragment.layoutInflater),
            viewBinder = SolidVocableViewBinder(fragment),
            viewHoldersProvider = { v, _ -> SolidVocableViewHolder(v) }
        )

        editFragmentRecyclerview = view.findViewById(R.id.edit_fragment_recyclerView)
        listIsEmpty = view.findViewById(R.id.listIsEmpty)

        editFragmentRecyclerview.layoutManager = LinearLayoutManager(fragment.requireContext())
        editFragmentRecyclerview.adapter = vocableEditRecyclerViewAdapter


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

    private suspend fun updateLesson(it: de.hamurasa.data.lesson.Lesson) {
        val words = it.words.toList().sortedBy { vocable -> vocable.value }
        withContext(Dispatchers.Main) {
            vocableEditRecyclerViewAdapter.setItems(words)
            vocableEditRecyclerViewAdapter.notifyDataSetChanged()
            listIsEmpty.visibility = if (words.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }


}