package de.hamurasa.main.fragments

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.hamurasa.main.fragments.dialogs.ResultAlertDialog
import de.util.hamurasa.utility.AbstractFragment
import de.util.hamurasa.utility.createSpinner
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : AbstractFragment(), VocableRecyclerViewAdapter.OnClickListener {
    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var vocableRecyclerViewAdapter: VocableRecyclerViewAdapter
    private lateinit var searchValueEditText: EditText
    private lateinit var vocableRecyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var modeSpinner: Spinner

    override fun getLayoutId(): Int = R.layout.dictionary_fragment

    override fun init(view: View) {
        vocableRecyclerView = view.findViewById(R.id.wordRecyclerView)
        searchValueEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        modeSpinner = activity!!.findViewById(R.id.modeSpinner)
    }

    override fun onItemClick(position: Int) {
        val word = vocableRecyclerViewAdapter.items[position]
        val fragment = ResultAlertDialog(word)

        fragment.show(activity!!.supportFragmentManager, "Result")
    }

    override fun onStart() {
        super.onStart()
        initElements()
    }

    private fun initElements() {
        vocableRecyclerViewAdapter = VocableRecyclerViewAdapter(context!!, this)
        vocableRecyclerView.layoutManager = LinearLayoutManager(context!!)
        vocableRecyclerView.adapter = vocableRecyclerViewAdapter

        myViewModel.observe(MainContext.DictionaryContext.words) {
            vocableRecyclerViewAdapter.setWords(it)
            vocableRecyclerViewAdapter.notifyDataSetChanged()
        }

        searchButton.setOnClickListener {
            myViewModel.getWord(searchValueEditText.text.toString())
        }

        activity!!.createSpinner(modeSpinner, array = arrayOf("ES-GER", "GER-ES"))
    }

}