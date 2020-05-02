package de.util.hamurasa.utility

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import kotlin.reflect.KMutableProperty0


fun CheckBox.bind(property: KMutableProperty0<Boolean>) {
    this.isChecked = property.get()
    this.setOnCheckedChangeListener { _, isChecked -> property.set(isChecked) }
}

inline fun <T> EditText.bind(
    property: KMutableProperty0<T>,
    crossinline toString: (T) -> String = { it.toString() },
    crossinline converter: (String) -> T
) {
    setText(toString.invoke(property.get()))
    afterTextChanged {
        if (it.isNotEmpty()) {
            val value = converter.invoke(it)
            property.set(value)
        }
    }
}

inline fun <T> Spinner.bind(
    property: KMutableProperty0<T>,
    crossinline toString: (T) -> String = { it.toString() },
    crossinline converter: (String) -> T
) {
    val active = toString.invoke(property.get())
    val items = getItems().map { it.toString() }
    val index = items.indexOf(active)
    this.setSelection(index)

    afterSelectedChanged {
        if (it.isNotEmpty()) {
            val value = converter.invoke(it)
            property.set(value)
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


fun EditText.bind(property: KMutableProperty0<String>) {
    bind(property) { value -> value }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

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