package de.hamurasa.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.hamurasa.main.fragments.dialogs.EditVocableDialog
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.edit_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class EditFragment : Fragment(), VocableRecyclerViewAdapter.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var vocableRecyclerViewAdapter: VocableRecyclerViewAdapter

    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_fragment, main_container, false)
    }

    override fun onStart() {
        super.onStart()
        init()
    }


    private fun init() {
        recyclerView = view!!.findViewById(R.id.editRecyclerView)
        vocableRecyclerViewAdapter =
            VocableRecyclerViewAdapter(
                context!!,
                this
            )
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = vocableRecyclerViewAdapter


        myViewModel.launch {
            MainContext.EditContext.lesson
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    val lessons = it.words.toList().sortedBy { vocable -> vocable.value }

                    vocableRecyclerViewAdapter.setWords(lessons)
                    vocableRecyclerViewAdapter.notifyDataSetChanged()
                    if (listIsEmpty != null) {
                        if (lessons.isEmpty()) {
                            listIsEmpty.visibility = View.VISIBLE
                        } else {
                            listIsEmpty.visibility = View.INVISIBLE
                        }
                    }
                }
        }

    }

    override fun onItemClick(position: Int) {
        val vocable = MainContext.EditContext.lesson.blockingFirst()
            .words
            .sortedBy { vocable -> vocable.value }[position]
        val fragment = EditVocableDialog(vocable)
        fragment.show(fragmentManager!!, "")
    }


}