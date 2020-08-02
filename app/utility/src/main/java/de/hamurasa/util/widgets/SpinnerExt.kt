package de.hamurasa.util.widgets

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlin.reflect.KMutableProperty0

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

//Spinner
fun Spinner.afterSelectedChanged(afterSelected: (String) -> Unit) {

    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val value = parent?.selectedItem?.toString()
            if (value != null) afterSelected.invoke(value)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }
}

inline fun <T> Spinner.bind(
    property: KMutableProperty0<T>,
    crossinline toString: (T) -> String = { it.toString() },
    crossinline converter: (String) -> T
) {
    bind(property = property, toString = toString, converter = converter, onValueChanged = {})
}

inline fun <T> Spinner.bind(
    property: KMutableProperty0<T>,
    crossinline toString: (T) -> String = { it.toString() },
    crossinline converter: (String) -> T,
    crossinline onValueChanged: (T) -> Unit = {}
) {
    val active = toString.invoke(property.get())
    val items = getItems().map { it.toString() }
    val index = items.indexOf(active)
    this.setSelection(index)

    afterSelectedChanged {
        if (it.isNotEmpty()) {
            val value = converter.invoke(it.trim())
            property.set(value)
            onValueChanged.invoke(value)
        }
    }
}

fun Spinner.getItems(): List<Any> {
    val count = this.adapter.count
    val items = mutableListOf<Any>()
    for (i in 0 until count) {
        items.add(getItemAtPosition(i))
    }
    return items.toList()
}
