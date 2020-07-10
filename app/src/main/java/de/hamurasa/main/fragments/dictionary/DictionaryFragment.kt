package de.hamurasa.main.fragments.dictionary

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.util.hamurasa.utility.util.AbstractFragment
import kotlinx.android.synthetic.main.dictionary_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : AbstractFragment(), VocableRecyclerViewAdapter.OnClickListener {
    override val myViewModel: DictionaryViewModel by sharedViewModel()
    private lateinit var vocableRecyclerViewAdapter: VocableRecyclerViewAdapter

    override fun getLayoutId() = R.layout.dictionary_fragment

    override fun onItemClick(position: Int) {

        val word = vocableRecyclerViewAdapter.items[position]
        val fragment = ResultAlertDialog(word)

        fragment.show(requireActivity().supportFragmentManager, "Result")
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vocableRecyclerViewAdapter = VocableRecyclerViewAdapter(requireContext(), this)
        dictionary_word_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dictionary_word_recyclerView.adapter = vocableRecyclerViewAdapter

        myViewModel.launchJob {
            initObserver()
        }

        dictionary_search_button.setOnClickListener {
            myViewModel.getWord(dictionary_search_editText.text.toString())
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.DictionaryContext.words.collect {
            withContext(Dispatchers.Main) {
                vocableRecyclerViewAdapter.setWords(it)
                vocableRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}


