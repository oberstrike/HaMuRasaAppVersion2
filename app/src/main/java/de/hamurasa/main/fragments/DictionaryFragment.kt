package de.hamurasa.main.fragments

import android.app.AlertDialog
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
import de.hamurasa.util.afterTextChanged
import okhttp3.internal.lockAndWaitNanos
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : Fragment(), ResultRecyclerViewAdapter.OnClickListener {
    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var resultRecyclerViewAdapter: ResultRecyclerViewAdapter
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_fragment, container, false)

    }

    override fun onItemClick(position: Int) {
        val word = resultRecyclerViewAdapter.items[position]
        val fragment = ResultAlertDialog(word)

        fragment.show(activity!!.supportFragmentManager, "Result")
    }

    override fun onStart() {
        super.onStart()
        recyclerView = view!!.findViewById(R.id.wordRecyclerView)
        searchEditText = view!!.findViewById(R.id.searchEditText)
        searchButton = view!!.findViewById(R.id.searchButton)
        spinner = activity!!.findViewById(R.id.modeSpinner)
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
            val name = spinner.selectedItem as String
            val mode = Mode.valueOf(name)
            if(mode == Mode.ES_GER){
                myViewModel.getWord(searchEditText.text.toString())
            }else{
                myViewModel.getWordByTranslation(searchEditText.text.toString())
            }
        }

        val modes = arrayOf( Mode.values().map { it.name } ).first()

        val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, modes)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
    }


    enum class Mode{
        ES_GER, GER_ES;
    }
}