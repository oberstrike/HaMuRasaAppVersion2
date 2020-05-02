package de.util.hamurasa.utility

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment

abstract class AbstractDialog : AppCompatDialogFragment() {

    @LayoutRes
    abstract fun getLayoutId(): Int

    internal var customView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        customView = activity!!.layoutInflater.inflate(getLayoutId(), null)
        return AlertDialog.Builder(context).setView(customView).create()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return customView
    }

    override fun onDestroyView() {
        customView = null
        super.onDestroyView()
    }
}

fun dialog(
    @LayoutRes layout: Int,
    onViewCreated: (view: View) -> Unit = {}
): AbstractDialog {
    return BasicDialog(layout, onViewCreated)
}

class BasicDialog(
    @LayoutRes val layout: Int,
    val onViewCreated: (view: View) -> Unit = {}
) :
    AbstractDialog() {
    override fun getLayoutId() = layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated.invoke(customView!!)
    }
}



