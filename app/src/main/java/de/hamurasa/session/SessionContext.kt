package de.hamurasa.session

import de.hamurasa.session.models.VocableWrapper
import io.reactivex.subjects.PublishSubject

object SessionContext {
    var running: PublishSubject<Boolean> = PublishSubject.create()
    lateinit var vocables: List<VocableWrapper>
    lateinit var sessionType: SessionType
    lateinit var activeVocable: VocableWrapper
    lateinit var sessionTypes: List<SessionType>
}


enum class SessionType {
    STANDARD, ALTERNATIVE, WRITING
}
