package de.hamurasa.main.fragments

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.hamurasa.main.fragments.dialogs.EditVocableDialog
import de.util.hamurasa.utility.AbstractFragment
import kotlinx.android.synthetic.main.edit_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class EditFragment : AbstractFragment(), VocableRecyclerViewAdapter.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var vocableEditRecyclerViewAdapter: VocableRecyclerViewAdapter

    private lateinit var vocableEditRecyclerView: RecyclerView

    override fun getLayoutId(): Int = R.layout.edit_fragment

    override fun init(view: View) {
        vocableEditRecyclerView = view.findViewById(R.id.editRecyclerView)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    //only Onlnine
    private fun init() {
        myViewModel.updateEdit()
        vocableEditRecyclerViewAdapter = VocableRecyclerViewAdapter(context!!, this)
        vocableEditRecyclerView.layoutManager = LinearLayoutManager(context!!)
        vocableEditRecyclerView.adapter = vocableEditRecyclerViewAdapter


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


}