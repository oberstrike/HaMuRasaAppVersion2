package de.hamurasa.session.models

import de.hamurasa.data.vocable.Vocable
import de.hamurasa.data.vocable.VocableType

data class VocableWrapper(val vocable: de.hamurasa.data.vocable.Vocable) {
    var attempts: Int = 0

    var level: Int = 1
        set(value) {
            field = if (value > 0) value else 1
        }

    val value: String = vocable.value!!


    val translation: String = vocable.translation.reduce { a, b -> "$a, $b" }


    val type: de.hamurasa.data.vocable.VocableType = vocable.type
}