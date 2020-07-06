package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.util.hamurasa.utility.util.AbstractFragment
import kotlinx.android.synthetic.main.edit_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class EditFragment : AbstractFragment(), VocableRecyclerViewAdapter.OnClickListener {

    private val myViewModel: EditViewModel by sharedViewModel()

    private lateinit var vocableEditRecyclerViewAdapter: VocableRecyclerViewAdapter

    override fun getLayoutId(): Int = R.layout.edit_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vocableEditRecyclerViewAdapter = VocableRecyclerViewAdapter(requireContext(), this)
        edit_fragment_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        edit_fragment_recyclerView.adapter = vocableEditRecyclerViewAdapter
        listIsEmpty.visibility = View.VISIBLE

        myViewModel.observe(MainContext.EditContext.lesson) {
            val lessons = it.words.toList().sortedBy { vocable -> vocable.value }
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


    override fun onItemClick(position: Int) {
        val vocable = MainContext.EditContext.lesson.blockingFirst()
            .words
            .sortedBy { vocable -> vocable.value }[position]
        val fragment by inject<EditVocableDialog> { parametersOf(vocable) }
        fragment.show(parentFragmentManager, "")
    }

    override fun onStop() {
        myViewModel.onCleared()
        super.onStop()
    }


}