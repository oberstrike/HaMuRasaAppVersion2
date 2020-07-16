package de.hamurasa.session.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hamurasa.R
import de.hamurasa.session.SessionViewModel
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.model.Settings
import de.util.hamurasa.utility.util.AbstractFragment
import org.koin.android.ext.android.inject

abstract class BasicFragment(
    open val activeVocable: VocableWrapper
) : AbstractFragment(), View.OnClickListener {

    val settings: Settings by inject()

    override fun getLayoutId(): Int = R.layout.vocable_session_fragment

    abstract fun reset()

    abstract fun init()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

}


