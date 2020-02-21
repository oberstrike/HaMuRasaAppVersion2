package de.hamurasa.main.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.main.MainViewModel
import de.hamurasa.util.afterTextChanged
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : Fragment(), ResultRecyclerViewAdapter.OnClickListener {
    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var resultRecyclerViewAdapter: ResultRecyclerViewAdapter
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_fragment, container, false)

    }

    override fun onItemClick(position: Int) {
        val word = resultRecyclerViewAdapter.items[position]
        val builder = AlertDialog.Builder(activity)

        with(builder){
            setTitle(word.value)
            setMessage(word.translation.toString())
        }

        builder.create().show()

    }

    override fun onStart() {
        super.onStart()
        recyclerView = view!!.findViewById(R.id.wordRecyclerView)
        searchEditText = view!!.findViewById(R.id.searchEditText)
        searchButton = view!!.findViewById(R.id.searchButton)
        init()
    }

    private fun init() {
        resultRecyclerViewAdapter = ResultRecyclerViewAdapter(context!!, this)
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = resultRecyclerViewAdapter


        myViewModel.launch {
            myViewModel.words
                .subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    resultRecyclerViewAdapter.setWords(it)
                    resultRecyclerViewAdapter.notifyDataSetChanged()
                }
        }

        searchButton.setOnClickListener{
            myViewModel.getWord(searchEditText.text.toString())
        }

    }

}