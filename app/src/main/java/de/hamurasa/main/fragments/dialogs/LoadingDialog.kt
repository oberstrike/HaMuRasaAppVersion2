package de.hamurasa.main.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.R
import de.hamurasa.util.withDialog
import kotlinx.coroutines.*

class LoadingDialog(private val loadingHandler: LoadingHandler) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        withDialog(activity!!, R.layout.dialog_loading,
            createView = {
                Unit
            },
            afterCreation = {
                it.setCancelable(false)
            }).create()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                loadingHandler.process()
            }
            GlobalScope.launch(Dispatchers.Main) {
                loadingHandler.onProcessFinished()
                dismiss()
            }
        }


        return view
    }

}

@FunctionalInterface
interface LoadingHandler {
    suspend fun process(): Unit
    suspend fun onProcessFinished(): Unit
}
