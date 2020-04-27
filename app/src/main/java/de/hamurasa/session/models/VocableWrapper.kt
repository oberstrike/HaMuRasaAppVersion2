package de.hamurasa.session.models

import de.hamurasa.lesson.model.vocable.Vocable

class VocableWrapper(val vocable: Vocable) {
    var attempts: Int = 0

    var level: Int = 1
        set(value) {
            field = if (value > 0) value else 1
        }

    val value: String
        get() {
            return vocable.value
        }

    val translation: String
        get() {
            return vocable.translation.reduce { a, b -> "$a, $b" }
        }

}