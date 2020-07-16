package de.util.hamurasa.utility.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.util.hamurasa.utility.main.AbstractViewModel

interface IAbstractFragment {
    val myViewModel: AbstractViewModel

    fun getLayoutId(): Int


}

abstract class AbstractFragment : Fragment(),
    IAbstractFragment {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


}


abstract class AbstractSelfCleanupFragment : AbstractFragment() {

    override fun onDestroyView() {
        myViewModel.onCleared()
        super.onDestroyView()
    }

}
