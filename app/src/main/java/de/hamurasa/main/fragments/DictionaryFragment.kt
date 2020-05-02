package de.hamurasa.main.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.MainViewModel
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.hamurasa.main.fragments.dialogs.ResultAlertDialog
import de.util.hamurasa.utility.AbstractFragment
import de.util.hamurasa.utility.initAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dictionary_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : AbstractFragment(), VocableRecyclerViewAdapter.OnClickListener {
    private val myViewModel: MainViewModel by sharedViewModel()

    private lateinit var vocableRecyclerViewAdapter: VocableRecyclerViewAdapter


    override fun getLayoutId(): Int = R.layout.dictionary_fragment


    override fun onItemClick(position: Int) {
        val word = vocableRecyclerViewAdapter.items[position]
        val fragment = ResultAlertDialog(word)

        fragment.show(activity!!.supportFragmentManager, "Result")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vocableRecyclerViewAdapter = VocableRecyclerViewAdapter(context!!, this)
        dictionary_word_recyclerView.layoutManager = LinearLayoutManager(context!!)
        dictionary_word_recyclerView.adapter = vocableRecyclerViewAdapter

        myViewModel.observe(MainContext.DictionaryContext.words) {
            vocableRecyclerViewAdapter.setWords(it)
            vocableRecyclerViewAdapter.notifyDataSetChanged()
        }

        dictionary_search_button.setOnClickListener {
            myViewModel.getWord(dictionary_search_editText.text.toString())
        }
        modeSpinner.initAdapter(arrayOf("ES-GER", "GER-ES"))
    }

}