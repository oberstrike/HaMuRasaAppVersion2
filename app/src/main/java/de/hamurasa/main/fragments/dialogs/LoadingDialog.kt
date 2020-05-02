package de.hamurasa.main.fragments.dialogs

import android.view.View
import de.hamurasa.R
import de.util.hamurasa.utility.AbstractDialog

class LoadingDialog : AbstractDialog(), View.OnClickListener {

    override fun getLayoutId() = R.layout.dialog_loading


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}