package de.hamurasa.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment

abstract class BaseDialog : AppCompatDialogFragment() {

    @LayoutRes
    abstract fun getLayoutId(): Int


    private var customView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        customView = requireActivity().layoutInflater.inflate(getLayoutId(), null)
        return AlertDialog.Builder(context).setView(customView).create()
    }

    fun <T : View?> findById(id: Int): T? {
        return view?.findViewById<T>(id)
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
    onViewCreated: (basicDialog: BasicDialog) -> Unit = {}
): BaseDialog {
    return BasicDialog(layout, onViewCreated)
}

class BasicDialog(
    @LayoutRes val layout: Int,
    val onViewCreated: (basicDialog: BasicDialog) -> Unit = {}
) :
    BaseDialog() {
    override fun getLayoutId() = layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated.invoke(this)
    }


}



