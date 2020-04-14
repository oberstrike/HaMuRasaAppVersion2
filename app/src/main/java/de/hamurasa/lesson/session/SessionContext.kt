package de.hamurasa.lesson.session

import de.hamurasa.lesson.fragments.VocableFragment
import de.hamurasa.lesson.model.vocable.Vocable

object SessionContext {
    var vocables: List<Vocable>? = null
    var vocableType: VocableFragment.Type = VocableFragment.Type.TRANSLATION_VALUE

}