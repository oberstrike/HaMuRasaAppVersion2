package de.hamurasa.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitteloupe.solid.fragment.SolidFragment
import de.hamurasa.main.AbstractViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface IAbstractFragment {
    val myViewModel: AbstractViewModel

    fun getLayoutId(): Int
}

abstract class AbstractFragment : SolidFragment(),
    IAbstractFragment, CoroutineScope {

    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


}


abstract class AbstractSelfCleanupFragment : AbstractFragment() {

    fun launchJob(job: suspend () -> Unit) {
        myViewModel.launchJob {
            job.invoke()
        }
    }

    override fun onDestroyView() {
        myViewModel.onCleared()
        super.onDestroyView()
    }

}
