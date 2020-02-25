package de.hamurasa.main.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import de.hamurasa.lesson.model.Vocable

class ResultAlertDialog(val word: Vocable) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        with(builder){
            setTitle(word.value)
            setMessage(word.translation.toString())
        }
        return builder.create()
    }

}