package de.util.hamurasa.utility.util

import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast


inline fun <reified E : Enum<E>> Spinner.initAdapter(
    layout: Int = android.R.layout.simple_spinner_dropdown_item
) {
    val values = enumValues<E>()
    val array = values.map { it.toString() }
    adapter = ArrayAdapter(this.context, layout, array)

}

fun Spinner.initAdapter(
    array: Array<String>,
    layout: Int = android.R.layout.simple_spinner_dropdown_item
) {
    adapter = ArrayAdapter(context, layout, array)
}


fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}






