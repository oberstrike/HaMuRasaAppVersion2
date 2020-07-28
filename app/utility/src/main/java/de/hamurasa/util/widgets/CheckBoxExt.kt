package de.hamurasa.util.widgets

import android.widget.CheckBox
import kotlin.reflect.KMutableProperty0

//Checkbox
fun CheckBox.bind(property: KMutableProperty0<Boolean>) {
    this.isChecked = property.get()
    this.setOnCheckedChangeListener { _, isChecked -> property.set(isChecked) }
}

