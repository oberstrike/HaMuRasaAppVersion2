package de.hamurasa.util

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import de.hamurasa.R

inline fun withDialog(
    activity: FragmentActivity,
    dialogLayout: Int,
    crossinline createView: (view: View) -> Unit = {},
    crossinline afterCreation: (AlertDialog.Builder) -> Unit = {}
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(activity)

    val inflater = activity.layoutInflater

    val view = inflater.inflate(dialogLayout, null)

    createView.invoke(view)

    builder.setView(view)

    afterCreation.invoke(builder)

    return builder
}


inline fun <reified E : Enum<E>> createSpinner(
    activity: Activity,
    spinner: Spinner,
    layout: Int = android.R.layout.simple_spinner_dropdown_item
) {
    val values = enumValues<E>()
    val array = values.map { it.toString() }
    with(spinner) {
        adapter = ArrayAdapter(activity, layout, array)
    }
}

fun createSpinner(
    activity: Activity,
    spinner: Spinner,
    layout: Int = android.R.layout.simple_spinner_dropdown_item,
    array: Array<String>
) {
    with(spinner) {
        adapter = ArrayAdapter(activity, layout, array)
    }


}


