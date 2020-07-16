package de.hamurasa.session.models

import de.hamurasa.model.vocable.Vocable
import de.hamurasa.model.vocable.VocableType

data class VocableWrapper(val vocable: Vocable) {
    var attempts: Int = 0

    var level: Int = 1
        set(value) {
            field = if (value > 0) value else 1
        }

    val value: String = vocable.value


    val translation: String = vocable.translation.reduce { a, b -> "$a, $b" }


    val type: VocableType = vocable.type
}