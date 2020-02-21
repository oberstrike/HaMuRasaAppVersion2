package de.hamurasa.lesson

import de.hamurasa.lesson.fragments.VocableFragment
import de.hamurasa.lesson.model.Vocable

object LessonContext {
    var vocables: List<Vocable>? = null
    var vocableType: VocableFragment.Type = VocableFragment.Type.TRANSLATION_VALUE

}