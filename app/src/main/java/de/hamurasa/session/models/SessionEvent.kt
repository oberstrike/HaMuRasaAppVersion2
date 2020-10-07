package de.hamurasa.session.models

import de.hamurasa.data.vocable.Vocable


data class SessionEvent(
    var listOfVocableWrapper: List<VocableWrapper> = listOf(),
    var vocables: List<Vocable> = listOf(),
    var time: Long = 0,
    var sessionType: SessionType = SessionType.STANDARD
) {
    lateinit var activeVocable: VocableWrapper
}


enum class SessionType {
    STANDARD, ALTERNATIVE, WRITING;
}
