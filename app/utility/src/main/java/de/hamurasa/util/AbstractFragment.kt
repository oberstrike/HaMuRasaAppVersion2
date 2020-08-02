package de.hamurasa.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.mitteloupe.solid.fragment.SolidFragment
import de.hamurasa.main.AbstractViewModel
import de.hamurasa.util.epoxy.KotlinModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface IAbstractFragment {
    val myViewModel: AbstractViewModel
    val layoutId: Int
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
        return inflater.inflate(layoutId, container, false)
    }
}


abstract class AbstractSelfCleaningFragment : AbstractFragment() {

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
