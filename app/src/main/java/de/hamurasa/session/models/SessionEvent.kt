package de.hamurasa.session.models


data class SessionEvent(
    var vocables: List<VocableWrapper> = listOf(),
    var time: Long = 0,
    var sessionType: SessionType = SessionType.STANDARD
) {
    lateinit var activeVocable: VocableWrapper
}


enum class SessionType {
    STANDARD, ALTERNATIVE, WRITING;
}
