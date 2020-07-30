package de.hamurasa.util.widgets

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import kotlin.math.max
import kotlin.reflect.KMutableProperty0

//EditText
fun EditText.bind(property: KMutableProperty0<String?>, maxLength: Int = 25) {
    bind(maxLength, property) { value -> value }
}

fun EditText.bind(property: KMutableProperty0<String>) {
    bind(25, property) { value -> value }
}

fun EditText.bindInt(property: KMutableProperty0<Int>) {
    bind(maxLength = 25, property = property, converter = String::toInt, toString = Int::toString)
}

inline fun <T> EditText.bind(
    maxLength: Int = 25,
    property: KMutableProperty0<T>,
    crossinline toString: (T) -> String = { it.toString() },
    crossinline converter: (String) -> T
) {
    setText(toString.invoke(property.get()))
    filters = arrayOf(InputFilter.LengthFilter(maxLength))

    afterTextChanged {
        if (it.isNotEmpty() && it.isNotBlank()) {
            val value = converter.invoke(it)
            property.set(value)
        }
    }
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