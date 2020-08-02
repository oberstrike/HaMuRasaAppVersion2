package de.hamurasa.main.fragments.dictionary

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mitteloupe.solid.recyclerview.SolidAdapter
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.adapters.*
import de.hamurasa.util.AbstractSelfCleaningFragment
import kotlinx.android.synthetic.main.dictionary_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DictionaryFragment : AbstractSelfCleaningFragment(),
    VocableOnClickListener {
    override val myViewModel: DictionaryViewModel by sharedViewModel()
    private lateinit var vocableRecyclerViewAdapter: SolidAdapter<SolidVocableViewHolder, de.hamurasa.data.vocable.Vocable>

    override val layoutId: Int = R.layout.dictionary_fragment

    override fun onItemClick(vocable: de.hamurasa.data.vocable.Vocable) {
        val fragment = DictionaryResultDialog(vocable)

        fragment.show(requireActivity().supportFragmentManager, "Result")
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vocableRecyclerViewAdapter = SolidAdapter(
            viewProvider = SolidVocableViewProvider(layoutInflater),
            viewBinder = SolidVocableViewBinder(this),
            viewHoldersProvider = { v, _ -> SolidVocableViewHolder(v) }
        )
        dictionary_word_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dictionary_word_recyclerView.adapter = vocableRecyclerViewAdapter

        myViewModel.launchJob {
            initObserver()
        }

        dictionary_search_button.setOnClickListener {
            myViewModel.launchJob {
                myViewModel.getWord(dictionary_search_editText.text.toString())
            }
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun initObserver() {
        MainContext.DictionaryContext.flow.collect {
            withContext(Dispatchers.Main) {
                vocableRecyclerViewAdapter.setItems(it)
                vocableRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}


