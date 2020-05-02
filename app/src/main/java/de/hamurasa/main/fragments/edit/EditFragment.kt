package de.hamurasa.main.fragments.edit

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.util.hamurasa.utility.AbstractFragment
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
        myViewModel.updateEdit()
        vocableEditRecyclerViewAdapter = VocableRecyclerViewAdapter(context!!, this)
        edit_fragment_recyclerView.layoutManager = LinearLayoutManager(context!!)
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
        fragment.show(fragmentManager!!, "")
    }

    override fun onStop() {
        myViewModel.onCleared()
        super.onStop()
    }


}