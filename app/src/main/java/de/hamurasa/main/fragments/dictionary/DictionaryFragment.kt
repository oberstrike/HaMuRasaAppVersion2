package de.hamurasa.main.fragments.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.VocableRecyclerViewAdapter
import de.util.hamurasa.utility.util.AbstractFragment
import de.util.hamurasa.utility.util.initAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dictionary_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : AbstractFragment(), VocableRecyclerViewAdapter.OnClickListener {
    private val myViewModel: DictionaryViewModel by sharedViewModel()

    private lateinit var vocableRecyclerViewAdapter: VocableRecyclerViewAdapter

    override fun getLayoutId() = R.layout.dictionary_fragment

    override fun onItemClick(position: Int) {
        val word = vocableRecyclerViewAdapter.items[position]
        val fragment =
            ResultAlertDialog(word)

        fragment.show(requireActivity().supportFragmentManager, "Result")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel.init()

        vocableRecyclerViewAdapter = VocableRecyclerViewAdapter(requireContext(), this)
        dictionary_word_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dictionary_word_recyclerView.adapter = vocableRecyclerViewAdapter

        initObserver()

        dictionary_search_button.setOnClickListener {
            myViewModel.getWord(dictionary_search_editText.text.toString())
            myViewModel.onCleared()
            initObserver()
        }
    }

    private fun initObserver() {
        myViewModel.observe(MainContext.DictionaryContext.words) {
            vocableRecyclerViewAdapter.setWords(it)
            vocableRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

}