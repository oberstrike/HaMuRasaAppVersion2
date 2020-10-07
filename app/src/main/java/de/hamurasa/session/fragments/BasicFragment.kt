package de.hamurasa.session.fragments

import android.os.Bundle
import android.view.View
import de.hamurasa.R
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.model.Settings
import de.hamurasa.util.AbstractFragment
import org.koin.android.ext.android.inject

interface IBasicFragment : View.OnClickListener {
    fun reset()
    fun init()
}

abstract class BasicFragment(
    open val activeVocable: VocableWrapper
) : AbstractFragment(), IBasicFragment {

    val settings: Settings by inject()

    override val layoutId: Int
        get() = R.layout.vocable_session_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

}


