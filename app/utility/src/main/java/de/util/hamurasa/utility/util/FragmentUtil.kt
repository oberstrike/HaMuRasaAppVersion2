package de.util.hamurasa.utility.util

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface IAbstractFragment {
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

internal class NewFragment(val layout: Int, val onViewCreated: () -> Unit) : AbstractFragment() {
    override fun getLayoutId() = layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated.invoke()
    }
}


fun fragment(@LayoutRes layoutId: Int, onViewCreated: () -> Unit): AbstractFragment {
    return NewFragment(layoutId, onViewCreated)
}