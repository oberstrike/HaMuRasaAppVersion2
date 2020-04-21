package de.hamurasa.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.hamurasa.main.fragments.dialogs.ResultAlertDialog
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : Fragment(), VocableRecyclerViewAdapter.OnClickListener {
    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var vocableRecyclerViewAdapter: VocableRecyclerViewAdapter
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dictionary_fragment, container, false)
        recyclerView = view!!.findViewById(R.id.wordRecyclerView)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        spinner = activity!!.findViewById(R.id.modeSpinner)

        return view

    }

    override fun onItemClick(position: Int) {
        val word = vocableRecyclerViewAdapter.items[position]
        val fragment = ResultAlertDialog(word)

        fragment.show(activity!!.supportFragmentManager, "Result")
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        vocableRecyclerViewAdapter =
            VocableRecyclerViewAdapter(
                context!!,
                this
            )
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = vocableRecyclerViewAdapter


        myViewModel.launch {
            myViewModel.words
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    vocableRecyclerViewAdapter.setWords(it)
                    vocableRecyclerViewAdapter.notifyDataSetChanged()
                }
        }

        searchButton.setOnClickListener {
            myViewModel.getWord(searchEditText.text.toString())
        }


        val array = arrayOf("ES-GER", "GER-ES")
        val arrayAdapter =
            ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, array)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

    }

}